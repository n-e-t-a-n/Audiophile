package com.example.shopphile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/** @noinspection ALL*/
public class ShopActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPopularProducts;
    private ProductAdapter adapter;
    private List<CartItem> popularProducts;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText searchBarShop;

    private Button buttonAll, buttonGuitar, buttonBass, buttonKeyboard, buttonDrums;
    private List<Button> categoryButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop);

        initializeViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearchBar();
        setupCategoryButtons();
    }

    private void initializeViews() {
        recyclerViewPopularProducts = findViewById(R.id.recycler_view_popular_products);
        searchBarShop = findViewById(R.id.searchBarShop);

        buttonAll = findViewById(R.id.button);
        buttonGuitar = findViewById(R.id.button2);
        buttonBass = findViewById(R.id.button3);
        buttonKeyboard = findViewById(R.id.button4);
        buttonDrums = findViewById(R.id.button5);

        categoryButtons = new ArrayList<>();
        categoryButtons.add(buttonAll);
        categoryButtons.add(buttonGuitar);
        categoryButtons.add(buttonBass);
        categoryButtons.add(buttonKeyboard);
        categoryButtons.add(buttonDrums);
    }

    private void setupRecyclerView() {
        recyclerViewPopularProducts.setLayoutManager(new GridLayoutManager(this, 2));
        popularProducts = new ArrayList<>();

        fetchProducts();
    }

    private void fetchProducts() {
        db.collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        popularProducts.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String name = document.getString("name");
                            String seller = document.getString("seller");
                            String imageUrl = document.getString("imageUrl");
                            Long stock = document.getLong("stock");
                            Double price = document.getDouble("price");
                            String category = document.getString("category");
                            String description = document.getString("description");

                            if (name != null && seller != null && imageUrl != null && stock != null && price != null) {
                                popularProducts.add(new CartItem(name, seller, price, Math.toIntExact(stock), imageUrl, description, category));
                            }
                        }

                        adapter = new ProductAdapter(new ArrayList<>(popularProducts), this);
                        recyclerViewPopularProducts.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(e -> Log.w("Firestore", "Error getting documents.", e));
    }

    private void setupCategoryButtons() {
        buttonAll.setOnClickListener(v -> filterProductsByCategory("All", buttonAll));
        buttonGuitar.setOnClickListener(v -> filterProductsByCategory("guitar", buttonGuitar));
        buttonBass.setOnClickListener(v -> filterProductsByCategory("bass", buttonBass));
        buttonKeyboard.setOnClickListener(v -> filterProductsByCategory("keyboard", buttonKeyboard));
        buttonDrums.setOnClickListener(v -> filterProductsByCategory("drums", buttonDrums));
    }

    private void filterProductsByCategory(String category, Button selectedButton) {
        List<CartItem> filteredProducts;

        if (category.equals("All")) {
            filteredProducts = new ArrayList<>(popularProducts);
        } else {
            filteredProducts = new ArrayList<>();
            for (CartItem product : popularProducts) {
                if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)) {
                    filteredProducts.add(product);
                }
            }
        }

        adapter.updateList(filteredProducts);
        highlightSelectedButton(selectedButton);
    }

    private void highlightSelectedButton(Button selectedButton) {
        for (Button button : categoryButtons) {
            if (button.equals(selectedButton)) {
                button.setBackgroundResource(R.drawable.selected_button);
                button.setTextColor(getResources().getColor(R.color.white));
            } else {
                button.setBackgroundResource(R.drawable.normal_button);
                button.setTextColor(getResources().getColor(R.color.altText));
            }
        }
    }

    private void setupClickListeners() {
        ImageView cartButton = findViewById(R.id.cart_button);
        cartButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, CartActivity.class)));

        ImageView ordersButton = findViewById(R.id.orders_button);
        ordersButton.setOnClickListener(v -> startActivity(new Intent(ShopActivity.this, OrderActivity.class)));
    }

    private void setupSearchBar() {
        searchBarShop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                String query = charSequence.toString();
                adapter.filter(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }
}
