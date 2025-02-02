package com.example.shoppingCart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartTest {
    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
    }

    //Lägga till varor
    @Test
    @DisplayName("ShoppingCart should contain added item")
    void shoppingCartShouldContainAddedItem() {
        Item tomatoItem = new Item("item-1", "Tomato", 5);
        shoppingCart.addItem(tomatoItem);
        assertThat(shoppingCart.hasItem(tomatoItem)).isTrue();
    }

    @Test
    @DisplayName("Should throw exception if added item is null")
    void shouldThrowExceptionIfAddedItemIsNull() {
        assertThrows(IllegalArgumentException.class, () -> shoppingCart.addItem(null));
    }

    //Ta bort varor
    @Test
    @DisplayName("ShoppingCart should not contain removed item")
    void shoppingCartShouldNotContainRemovedItems() {
        Item tomatoItem = new Item("item-1", "Tomato", 5);
        shoppingCart.addItem(tomatoItem);
        shoppingCart.removeItem(tomatoItem);
        assertThat(shoppingCart.hasItem(tomatoItem)).isFalse();
    }

    //Beräkna totalpris
    //Applicera rabatter
    //Hantera kvantitetsuppdateringar

}
