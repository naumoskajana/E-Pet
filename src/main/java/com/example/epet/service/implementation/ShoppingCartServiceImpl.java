package com.example.epet.service.implementation;

import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.Product;
import com.example.epet.model.entities.ShoppingCart;
import com.example.epet.model.enumeration.ShoppingCartStatus;
import com.example.epet.model.exceptions.ProductNotFoundException;
import com.example.epet.model.exceptions.ShoppingCartNotFoundException;
import com.example.epet.repository.PetOwnerRepository;
import com.example.epet.repository.ProductRepository;
import com.example.epet.repository.ShoppingCartRepository;
import com.example.epet.service.ShoppingCartService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final ProductRepository productRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, PetOwnerRepository petOwnerRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> listAllProductsInShoppingCart(Long cartId) {
        if(!this.shoppingCartRepository.findById(cartId).isPresent()){
            throw new ShoppingCartNotFoundException(cartId);
        }
        return this.shoppingCartRepository.findById(cartId).get().getProductList();
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        PetOwner user = this.petOwnerRepository.findByEmail(username);

        return this.shoppingCartRepository
                .findByUserAndShoppingCartStatus(user, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart cart = new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });
    }

    @Override
    public void addProductToShoppingCart(String username, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        if (!(shoppingCart.getProductList().stream().anyMatch(p -> p.getId().equals(productId)))){
            product.setQuantity(1);
            this.productRepository.save(product);
            shoppingCart.getProductList().add(product);
        }
        else {
            int quantity = product.getQuantity();
            product.setQuantity(quantity+1);
            this.productRepository.save(product);
        }
        this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public Double price(ShoppingCart shoppingCart) {
        double sum = 0.0;
        List<Product> products = shoppingCart.getProductList();
        for (Product product : products) {
            sum = sum + product.getPrice()*product.getQuantity();
        }
        return sum;
    }

    @Override
    public void checkoutShoppingCart(Long id) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findById(id).orElseThrow(() -> new ShoppingCartNotFoundException(id));
        this.shoppingCartRepository.delete(shoppingCart);
    }

    @Override
    public void removeProductFromShoppingCart(String username, Long productId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        shoppingCart.getProductList().remove(product);
        this.shoppingCartRepository.save(shoppingCart);
    }
}
