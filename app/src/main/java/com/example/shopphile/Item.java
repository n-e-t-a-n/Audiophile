package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Item extends AppCompatActivity {
    private ImageView backButton, productImageView;
    private TextView productNameTextView, productCategoryTextView,
            productSellerTextView, productDescriptionTextView,
            productPriceTextView, productStockTextView;
    private Button orderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item);

        backButton = findViewById(R.id.back_button);
        productImageView = findViewById(R.id.product_image);
        productNameTextView = findViewById(R.id.product_name);
        productCategoryTextView = findViewById(R.id.product_category);
        productSellerTextView = findViewById(R.id.product_seller);
        productDescriptionTextView = findViewById(R.id.product_description);
        productPriceTextView = findViewById(R.id.product_price);
        productStockTextView = findViewById(R.id.product_stock);
        orderButton = findViewById(R.id.order_button);

        Intent intent = getIntent();

        String productName = intent.getStringExtra("productName");
        String productCategory = intent.getStringExtra("productCategory");
        String productSeller = intent.getStringExtra("productSeller");
        String productDescription = intent.getStringExtra("productDescription");
        double productPrice = intent.getDoubleExtra("productPrice", 0.0);
        int productStock = intent.getIntExtra("productStock", 0);
        String productImage = intent.getStringExtra("productImage");

        productNameTextView.setText(productName);
        productCategoryTextView.setText("Category: " + productCategory);
        productSellerTextView.setText("Seller: " + productSeller);
        productDescriptionTextView.setText(productDescription);
        productPriceTextView.setText(String.format("$%.2f", productPrice));
        productStockTextView.setText("Stock: " + productStock);

        Glide.with(this)
                .load(productImage)
                .into(productImageView);

        backButton.setOnClickListener(v -> onBackPressed());

        orderButton.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                String email = currentUser.getEmail();

                // Get the reference to the user's document in Firestore
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference userDocRef = db.collection("users").document(email);

                // Create a map for the cart item
                Map<String, Object> cartItem = new HashMap<>();
                cartItem.put("productName", productName);
                cartItem.put("productCategory", productCategory);
                cartItem.put("productSeller", productSeller);
                cartItem.put("productDescription", productDescription);
                cartItem.put("productPrice", productPrice);
                cartItem.put("productStock", productStock);
                cartItem.put("productImage", productImage);

                // Add the cart item to the cart array in the user's document
                userDocRef.update("cart", FieldValue.arrayUnion(cartItem))
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(Item.this, "Item added to cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Item.this, "Failed to add item to cart", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(Item.this, "Please log in to add items to your cart", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Item.this, LoginActivity.class));
            }
        });
    }
}
