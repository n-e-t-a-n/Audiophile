package com.example.shopphile;

public class OrderItem {
    private String productImage;
    private String brandName;
    private String productName;
    private double productPrice;
    private int quantity;
    private int orderId;

    public OrderItem(String productImage, String brandName, String productName, double productPrice, int quantity, int orderId) {
        this.productImage = productImage;
        this.brandName = brandName;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.orderId = orderId;
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

    public int getOrderId() {
        return orderId;
    }
}
