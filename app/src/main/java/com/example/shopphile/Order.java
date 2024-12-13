package com.example.shopphile;

import java.util.List;

public class Order {
    private final int orderId;
    private final String orderDate;
    private final List<OrderItem> orderItems;

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
