package com.comix.model;

public class Comic {
    public int id;
    public String title;
    public double price;
    public int stock;
    public String category;
    public String story;

    public Comic(int id, String title, double price, int stock, String category, String story) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.story = story;
    }
}
