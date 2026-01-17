package com.example.safecheck.repository;

import com.example.safecheck.entity.ProductIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductIngredientRepository
        extends JpaRepository<ProductIngredient, Long> {

    // Bir ürüne ait tüm ingredient’ler
    List<ProductIngredient> findByProductId(Long productId);
}
