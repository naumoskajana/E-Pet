package com.example.epet.service.implementation;

import com.example.epet.model.entities.Product;
import com.example.epet.model.exceptions.ProductNotFoundException;
import com.example.epet.repository.ProductRepository;
import com.example.epet.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAllByOrderByIdAsc();
    }

    @Override
    public Product findById(Long id) {
        return this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    public Product save(String name, Double price, String photo) {
        Product product = new Product(name,price,photo);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
