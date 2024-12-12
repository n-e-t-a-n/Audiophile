package com.example.shopphile;

public class CartItem {
    private int id;
    private String productName;
    private String brandName;
    private double productPrice;
    private int quantity;
    private int productImage;

    // CONSTRUCTORS
    public CartItem(String productName, String brandName, double productPrice, int quantity, int productImage) {
        this.productName = productName;
        this.brandName = brandName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    public CartItem(int id, String productName, String brandName, double productPrice, int quantity, int productImage) {
        this.id = id;
        this.productName = productName;
        this.brandName = brandName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productImage = productImage;
    }

    // GETTERS
    public int getId() { return id; }
    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public double getProductPrice() { return productPrice; }
    public int getQuantity() { return quantity; }
    public int getProductImage() { return productImage; }

    // SETTERS
    public void setProductName(String productName) { this.productName = productName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setProductImage(int productImage) { this.productImage = productImage; }
}
