package com.example.safecheck.repository;

import com.example.safecheck.entity.IngredientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientAllergyRepository
        extends JpaRepository<IngredientAllergy, Long> {

    // ❗ DİKKAT: Ingredient_Id (alt çizgi var)
    List<IngredientAllergy> findByIngredient_Id(Long ingredientId);
}
