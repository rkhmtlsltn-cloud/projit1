package com.comix.model;

import java.util.ArrayList;

public class FullOrderDescription {
    public int orderId;
    public int customerId;
    public String customerName;
    public double total;
    public ArrayList<OrderLine> items;

    public FullOrderDescription(int orderId, int customerId, String customerName, double total) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.total = total;
        this.items = new ArrayList<>();
    }
}
