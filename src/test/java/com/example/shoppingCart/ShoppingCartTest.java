package com.example.shoppingCart;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

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

    //Konstruktor
    @ParameterizedTest
    @MethodSource("invalidItemsProvider")
    @DisplayName("Should throw exception if parameters are invalid when creating new item")
    void shouldThrowExceptionIfParametersAreInvalidWhenCreatingNewItem(String id, String name, double price) {
        assertThrows(IllegalArgumentException.class, () -> new Item(id, name, price));
    }

    static Stream<Object[]> invalidItemsProvider() {
        return Stream.of(
                new Object[]{null, "Lettuce", 5.0},
                new Object[]{"", "Lettuce", 5.0},
                new Object[]{"item-4", null, 5.0},
                new Object[]{"item-4", "", 5.0},
                new Object[]{"item-4", "Lettuce", 0},
                new Object[]{"item-4", "Lettuce", -5}
        );
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

    @Test
    @DisplayName("Should throw exception if removed item is null")
    void shouldThrowExceptionIfRemovedItemIsNull() {
        assertThrows(IllegalArgumentException.class, () -> shoppingCart.removeItem(null));
    }

    @Test
    @DisplayName("Should throw exception when removing item that does not exist in cart")
    void shouldThrowExceptionWhenRemovingItemThatDoesNotExistInCart() {
        Item tomatoItem = new Item("item-1", "Tomato", 5);
        assertThrows(NoSuchElementException.class, () -> shoppingCart.removeItem(tomatoItem));
    }

    //Beräkna totalpris
    @Test
    @DisplayName("Should return accurate total price")
    void shouldReturnAccurateTotalPrice() {
        Item tomatoItem = new Item("item-1", "Tomato", 5);
        Item garlicItem = new Item("item-2", "Garlic", 1.5);
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        shoppingCart.addItem(tomatoItem);
        shoppingCart.addItem(garlicItem);
        shoppingCart.addItem(oliveOilItem);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(56.5);
    }

    //Applicera rabatter
    @Test
    @DisplayName("Should apply item discount when calculating item price")
    void shouldApplyItemDiscountWhenCalculatingItemPrice() {
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        oliveOilItem.setDiscount(0.2);
        assertThat(oliveOilItem.getPrice()).isEqualTo(40);
    }

    @ParameterizedTest
    @CsvSource({"-0.1", "-1", "1", "1.5"})
    @DisplayName("Should throw exception if discount is invalid")
    void shouldThrowExceptionIfDiscountIsInvalid(double discount) {
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        assertThrows(IllegalArgumentException.class, () -> oliveOilItem.setDiscount(discount));
    }

    @Test
    @DisplayName("Should apply item discounts when calculating total price")
    void shouldApplyItemDiscountsWhenCalculatingTotalPrice() {
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        Item coffeeItem = new Item("item-4", "Coffee", 45);
        oliveOilItem.setDiscount(0.2);
        coffeeItem.setDiscount(0.1);
        shoppingCart.addItem(oliveOilItem);
        shoppingCart.addItem(coffeeItem);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(80.5);
    }

    //Hantera kvantitetsuppdateringar
    @Test
    @DisplayName("Should calculate total price based on item quantity in cart")
    void shouldCalculateTotalPriceBasedOnItemQuantityInCart() {
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        shoppingCart.addItem(oliveOilItem);
        shoppingCart.setQuantity(oliveOilItem, 5);
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(250);
    }

    @Test
    @DisplayName("Should throw exception if quantity is invalid")
    void shouldThrowExceptionIfQuantityIsInvalid() {
        Item oliveOilItem = new Item("item-3", "Olive Oil", 50);
        shoppingCart.addItem(oliveOilItem);
        assertThrows(IllegalArgumentException.class, () -> shoppingCart.setQuantity(oliveOilItem, -5));
    }

}
