package com.example.epet.service;

import com.example.epet.model.entities.Product;
import com.example.epet.model.entities.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<Product> listAllProductsInShoppingCart(Long cartId);
    ShoppingCart getActiveShoppingCart(String username);
    void addProductToShoppingCart(String username, Long productId);
    Double price(ShoppingCart shoppingCart);
    void checkoutShoppingCart(Long id);
    void removeProductFromShoppingCart(String username, Long productId);
}
