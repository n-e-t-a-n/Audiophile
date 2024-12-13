package com.example.shopphile;

public class CartItem {
    private final String productName;
    private final String brandName;
    private final double productPrice;
    private final String description;
    private final String category;
    private final int quantity;
    private final String productImage;

    public CartItem(String productName, String brandName, double productPrice, int quantity, String productImage, String description, String category) {
        this.productName = productName;
        this.brandName = brandName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productImage = productImage;
        this.description = description;
        this.category = category;
    }

    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public double getProductPrice() { return productPrice; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public int getQuantity() { return quantity; }
    public String getProductImage() { return productImage; }
}
