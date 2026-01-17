package com.example.safecheck.controller;

import com.example.safecheck.entity.Product;
import com.example.safecheck.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }



    @GetMapping("/barcode/{barcode}")
    public Product getByBarcode(@PathVariable String barcode) {
        return productRepository
                .findByBarcode(barcode.trim())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }






}
