package com.example.shopphile;

public class OrderItem {
    private final String productImage;
    private final String brandName;
    private final String productName;
    private final double productPrice;
    private final String orderDate;

    public OrderItem(String productImage, String brandName, String productName, double productPrice, String orderDate) {
        this.productImage = productImage;
        this.brandName = brandName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.orderDate = orderDate;
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

    public String getOrderDate() {
        return orderDate;
    }
}
