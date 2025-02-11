package com.example.shoppingCart;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class ShoppingCart {
    private final Map<Item, Integer> cartItems;

    public ShoppingCart() {
        cartItems = new HashMap<Item, Integer>();
    }
    public void addItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null");
        cartItems.put(item, 1);
    }

    public boolean hasItem(Item item) {
        return cartItems.containsKey(item);
    }

    public void removeItem(Item item) {
        if (item == null)
            throw new IllegalArgumentException("Item cannot be null");
        if (!cartItems.containsKey(item))
            throw new NoSuchElementException("Item does not exist");
        cartItems.remove(item);
    }

    public double getTotalPrice() {
        double sum = 0.0;
        for (Map.Entry<Item, Integer> cartItem : cartItems.entrySet()) {
            Item item = cartItem.getKey();
            int quantity = cartItem.getValue();
            sum += item.getPrice() * quantity;
        }

        return sum;
    }

    public void setQuantity(Item item, Integer quantity) {
        if (quantity <= 0)
            throw new IllegalArgumentException("Quantity must be greater than 0");
        cartItems.put(item, quantity);
    }
}
