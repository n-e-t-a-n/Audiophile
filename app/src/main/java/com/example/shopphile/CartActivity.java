package com.example.shopphile;

import android.annotation.SuppressLint;
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
import java.util.Objects;

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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerViewCart = findViewById(R.id.recycler_view_cart);
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));

        totalCount = findViewById(R.id.total_count);
        totalAmount = findViewById(R.id.total_amount);
        shippingAmount = findViewById(R.id.shipping_amount);
        payableAmount = findViewById(R.id.payable_amount);

        loadCartItems();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

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
        String userEmail = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        if (userEmail != null) {
            DocumentReference userDocRef = db.collection("users").document(userEmail);

            userDocRef.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(CartActivity.this, "Error getting cart items: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                DocumentSnapshot document = task.getResult();

                if (document == null || !document.exists()) {
                    Toast.makeText(CartActivity.this, "No cart found", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<?> cartItemsList = (List<?>) document.get("cart");

                if (cartItemsList == null) {
                    Toast.makeText(CartActivity.this, "Cart is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                cartItems = new ArrayList<>();
                for (Object obj : cartItemsList) {
                    if (obj instanceof HashMap) {
                        @SuppressWarnings("unchecked")
                        HashMap<String, Object> itemMap = (HashMap<String, Object>) obj;

                        String productName = (String) itemMap.getOrDefault("productName", "Unknown Product");
                        String productCategory = (String) itemMap.getOrDefault("productCategory", "Unknown Category");
                        String productSeller = (String) itemMap.getOrDefault("productSeller", "Unknown Seller");
                        String productDescription = (String) itemMap.getOrDefault("productDescription", "No description");
                        double productPrice = itemMap.containsKey("productPrice") && itemMap.get("productPrice") != null
                                ? ((Number) Objects.requireNonNull(itemMap.get("productPrice"))).doubleValue()
                                : 0.0;
                        int productStock = itemMap.containsKey("productStock") && itemMap.get("productStock") != null
                                ? ((Number) Objects.requireNonNull(itemMap.get("productStock"))).intValue()
                                : 0;
                        String productImage = (String) itemMap.getOrDefault("productImage", "");

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

                cartAdapter = new CartAdapter(cartItems, this, this);
                recyclerViewCart.setAdapter(cartAdapter);

                calculateTotals();
            });
        }
    }



    @SuppressLint("NotifyDataSetChanged")
    private void checkout() {
        String orderDate = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());

        if (cartItems.isEmpty()) {
            return;
        }

        db.collection("users")
                .document(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()))
                .update("orders", FieldValue.arrayUnion(orderDate))
                .addOnSuccessListener(aVoid ->
                    db.collection("users")
                            .document(mAuth.getCurrentUser().getEmail())
                            .update("cart", new ArrayList<>())
                            .addOnSuccessListener(aVoid1 -> {
                                Toast.makeText(CartActivity.this, "Checkout successful", Toast.LENGTH_SHORT).show();
                                cartItems.clear();
                                cartAdapter.notifyDataSetChanged();
                                calculateTotals();
                            })
                            .addOnFailureListener(e ->
                                Toast.makeText(CartActivity.this, "Error clearing cart: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                            )
                )
                .addOnFailureListener(e ->
                    Toast.makeText(CartActivity.this, "Error adding order: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

    @SuppressLint("DefaultLocale")
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
