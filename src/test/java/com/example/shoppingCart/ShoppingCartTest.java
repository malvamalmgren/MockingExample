package com.example.shoppingCart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    @DisplayName("Should return true if item is added")
    void shouldReturnTrueIfItemIsAdded() {
        Item tomatoItem = new Item("item-1", "Tomato", 5);
        shoppingCart.addItem(tomatoItem);
        assertThat(shoppingCart.hasItem(tomatoItem)).isTrue();

    }

    //Ta bort varor
    //Beräkna totalpris
    //Applicera rabatter
    //Hantera kvantitetsuppdateringar

}
