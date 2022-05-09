package com.example.epet.service;

import com.example.epet.model.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    public List<Product> findAll();
    public Product findById(Long id);
    public Product findByName(String name);
    public Product save(String name, Double price, String photo);
    public void deleteById(Long id);
}
