package com.example.epet.model.entities;

import com.example.epet.model.enumeration.ShoppingCartStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dateCreated;

    @ManyToOne
    private PetOwner user;

    @ManyToMany
    private List<Product> productList;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus shoppingCartStatus;

    public ShoppingCart() {

    }

    public ShoppingCart(PetOwner user) {
        this.dateCreated = LocalDateTime.now();
        this.user = user;
        this.productList = new ArrayList<>();
        this.shoppingCartStatus = ShoppingCartStatus.CREATED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public PetOwner getUser() {
        return user;
    }

    public void setUser(PetOwner user) {
        this.user = user;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public ShoppingCartStatus getShoppingCartStatus() {
        return shoppingCartStatus;
    }

    public void setShoppingCartStatus(ShoppingCartStatus shoppingCartStatus) {
        this.shoppingCartStatus = shoppingCartStatus;
    }
}
