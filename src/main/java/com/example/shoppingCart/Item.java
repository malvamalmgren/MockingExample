package com.example.shoppingCart;

public class Item {
    private String id;
    private String name;
    private double price;
    private double discount;

    public Item(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.discount = 0;
    }

    public double getPrice() {
        return price - (price * discount);
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
