package com.example.epet.repository;

import com.example.epet.model.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    Product findByName(String name);
    List<Product> findAllByOrderByIdAsc();
}

