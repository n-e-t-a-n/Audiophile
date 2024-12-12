package com.example.shopphile;

import java.util.List;

public class Order {
    private int orderId;
    private String orderDate;
    private List<OrderItem> orderItems;

    public Order(int orderId, String orderDate, List<OrderItem> orderItems) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.orderItems = orderItems;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }
}
