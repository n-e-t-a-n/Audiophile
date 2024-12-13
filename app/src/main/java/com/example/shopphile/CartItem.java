package com.example.shopphile;

public class CartItem {
    private int id;
    private String productName;
    private String brandName;
    private double productPrice;
    private String description;   // Added description
    private String category;      // Added category
    private int quantity;
    private String productImage;

    // Constructors
    public CartItem(String productName, String brandName, double productPrice, int quantity, String productImage, String description, String category) {
        this.productName = productName;
        this.brandName = brandName;
        this.productPrice = productPrice;
        this.quantity = quantity;
        this.productImage = productImage;
        this.description = description;  // Initialize description
        this.category = category;        // Initialize category
    }

    // Getters
    public int getId() { return id; }
    public String getProductName() { return productName; }
    public String getBrandName() { return brandName; }
    public double getProductPrice() { return productPrice; }
    public String getDescription() { return description; }  // Getter for description
    public String getCategory() { return category; }         // Getter for category
    public int getQuantity() { return quantity; }
    public String getProductImage() { return productImage; }

    // Setters
    public void setProductName(String productName) { this.productName = productName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public void setDescription(String description) { this.description = description; }  // Setter for description
    public void setCategory(String category) { this.category = category; }                  // Setter for category
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setProductImage(String productImage) { this.productImage = productImage; }
}
