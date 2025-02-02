package com.example.shoppingCart;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private final Map<Item, Integer> cartItems;

    public ShoppingCart() {
        cartItems = new HashMap<Item, Integer>();
    }
    public void addItem(Item item) {
        cartItems.put(item, 1);
    }

    public boolean hasItem(Item item) {
        return cartItems.containsKey(item);
    }
}
