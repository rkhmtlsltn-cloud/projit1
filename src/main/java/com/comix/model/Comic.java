package com.comix.model;

public class Comic {
    public int id;
    public String title;
    public double price;
    public int stock;

    public Comic(int id, String title, double price, int stock) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
    }
}
