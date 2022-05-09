package com.example.epet.repository;

import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.ShoppingCart;
import com.example.epet.model.enumeration.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    Optional<ShoppingCart> findByUserAndShoppingCartStatus(PetOwner user, ShoppingCartStatus status);
}

