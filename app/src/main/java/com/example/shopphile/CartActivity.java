package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartUpdateListener {
    private RecyclerView recyclerViewCart;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private TextView totalCount, totalAmount, shippingAmount, payableAmount;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Initialize views
        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        totalCount = findViewById(R.id.total_count);
        totalAmount = findViewById(R.id.total_amount);
        shippingAmount = findViewById(R.id.shipping_amount);
        payableAmount = findViewById(R.id.payable_amount);

        // Get cart data from Firestore
        loadCartItems();

        // BACK BUTTON
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // CHECKOUT BUTTON
        Button checkoutButton = findViewById(R.id.checkout_button);
        checkoutButton.setOnClickListener(v -> {
            checkout();
            Intent intent = new Intent(CartActivity.this, OrdersActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onCartUpdated() {
        calculateTotals();
    }

    private void loadCartItems() {
        // Get the current user's email
        String userEmail = mAuth.getCurrentUser().getEmail();

        if (userEmail != null) {
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            // Fetch the cart array from the user's document
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        List<?> cartItemsList = (List<?>) document.get("cart");

                        if (cartItemsList != null) {
                            // Convert HashMap to CartItem objects
                            cartItems = new ArrayList<>();
                            for (Object obj : cartItemsList) {
                                if (obj instanceof HashMap) {
                                    HashMap<String, Object> itemMap = (HashMap<String, Object>) obj;

                                    // Extract fields with null checks and default values
                                    String productName = (String) itemMap.getOrDefault("productName", "Unknown Product");
                                    String productCategory = (String) itemMap.getOrDefault("productCategory", "Unknown Category");
                                    String productSeller = (String) itemMap.getOrDefault("productSeller", "Unknown Seller");
                                    String productDescription = (String) itemMap.getOrDefault("productDescription", "No description");
                                    double productPrice = itemMap.containsKey("productPrice") && itemMap.get("productPrice") != null
                                            ? ((Number) itemMap.get("productPrice")).doubleValue()
                                            : 0.0;
                                    int productStock = itemMap.containsKey("productStock") && itemMap.get("productStock") != null
                                            ? ((Number) itemMap.get("productStock")).intValue()
                                            : 0;
                                    String productImage = (String) itemMap.getOrDefault("productImage", "");
                                    int quantity = itemMap.containsKey("quantity") && itemMap.get("quantity") != null
                                            ? ((Number) itemMap.get("quantity")).intValue()
                                            : 1;

                                    // Create the CartItem object
                                    CartItem cartItem = new CartItem(
                                            productName,
                                            productSeller,
                                            productPrice,
                                            productStock,
                                            productImage,
                                            productDescription,
                                            productCategory
                                    );
                                    cartItems.add(cartItem);
                                }
                            }

                            // Set up the adapter
                            cartAdapter = new CartAdapter(cartItems, this, this);
                            recyclerViewCart.setAdapter(cartAdapter);
                            calculateTotals();
                        } else {
                            Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CartActivity.this, "No cart found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Error getting cart items: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    private void checkout() {
        String orderDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        if (cartItems.isEmpty()) {
            return;
        }

        // Add the order to the user's orders array
        db.collection("users")
                .document(mAuth.getCurrentUser().getEmail())
                .update("orders", FieldValue.arrayUnion(orderDate)) // Add order to orders array
                .addOnSuccessListener(aVoid -> {
                    // Clear the cart after checkout
                    db.collection("users")
                            .document(mAuth.getCurrentUser().getEmail())
                            .update("cart", new ArrayList<>()) // Clear the cart array
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(CartActivity.this, "Checkout successful", Toast.LENGTH_SHORT).show();
                                cartItems.clear();
                                cartAdapter.notifyDataSetChanged();
                                calculateTotals();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(CartActivity.this, "Error clearing cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(CartActivity.this, "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void calculateTotals() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProductPrice();
        }
        totalCount.setText(String.valueOf(cartItems.size()));
        totalAmount.setText(String.format("$%.2f", total));
        double shipping = cartItems.isEmpty() ? 0 : 30;
        shippingAmount.setText(String.format("$%.2f", shipping));
        double payable = total + shipping;
        payableAmount.setText(String.format("$%.2f", payable));
    }
}
