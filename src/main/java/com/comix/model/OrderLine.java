package com.comix.model;

public class OrderLine {
    public int itemId;
    public int comicId;
    public String title;
    public int quantity;
    public double priceAtPurchase;

    public OrderLine(int itemId, int comicId, String title, int quantity, double priceAtPurchase) {
        this.itemId = itemId;
        this.comicId = comicId;
        this.title = title;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }
}
