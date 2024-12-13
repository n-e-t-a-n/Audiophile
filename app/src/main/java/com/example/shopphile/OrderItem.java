package com.example.shopphile;

public class OrderItem {
    private final String productImage;
    private final String brandName;
    private final String productName;
    private final double productPrice;
    private final int quantity;

    public OrderItem(String productImage, String brandName, String productName, double productPrice, int quantity) {
        this.productImage = productImage;
        this.brandName = brandName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }
}
