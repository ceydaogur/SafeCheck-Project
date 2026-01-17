package com.example.safecheck.controller;

import com.example.safecheck.entity.*;
import com.example.safecheck.repository.ProductRepository;
import com.example.safecheck.repository.UserRepository;
import org.springframework.web.bind.annotation.*;
import com.example.safecheck.repository.LogRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.transaction.Transactional;



import java.util.*;

@Transactional
@RestController
@RequestMapping("/api/scanner")
@CrossOrigin
public class ScannerController {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public ScannerController(ProductRepository productRepository,
                             UserRepository userRepository,
                             LogRepository logRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @GetMapping("/product/{productId}/user/{userId}")
    public Map<String, Object> scanByProductId(
            @PathVariable Integer productId,
            @PathVariable Long userId
    ) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        List<String> triggeredAllergies = new ArrayList<>();

        for (Ingredient ingredient : product.getIngredients()) {
            if (ingredient.getAllergies() == null) continue;

            for (Allergy ingredientAllergy : ingredient.getAllergies()) {
                for (UserAllergy userAllergy : user.getAllergies()) {

                    if (ingredientAllergy.getId()
                            .equals(userAllergy.getAllergy().getId())) {

                        triggeredAllergies.add(ingredientAllergy.getName());
                    }
                }
            }
        }

        boolean risky = !triggeredAllergies.isEmpty();


        Log log = new Log();
        log.setUser(user);
        log.setProduct(product);
        log.setActionType(risky ? "RISKY_SCAN" : "SAFE_SCAN");
        log.setDescription(
                risky ? "Ürün riskli bulundu" : "Ürün güvenli bulundu"
        );
        log.setLogDate(LocalDate.now());
        log.setLogTime(LocalTime.now());

        logRepository.saveAndFlush(log);


        return Map.of(
                "risky", risky,
                "triggeredAllergies", triggeredAllergies
        );
    }


    @Transactional
    @GetMapping("/barcode/{barcode}/user/{userId}")
    public Map<String, Object> scanProduct(
            @PathVariable String barcode,
            @PathVariable Long userId
    ) {

        // 🔍 DEBUG - METOT ÇALIŞIYOR MU?
        System.out.println(">>> scanProduct CALISTI <<<");

        // 1️⃣ Ürünü bul
        Product product = productRepository
                .findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        // 2️⃣ Kullanıcıyı bul
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 3️⃣ Kullanıcının alerjileri
        List<UserAllergy> userAllergies = user.getAllergies();

        // 4️⃣ Ürün içerikleri üzerinden kontrol
        for (Ingredient ingredient : product.getIngredients()) {

            if (ingredient.getAllergies() == null) continue;

            for (Allergy ingredientAllergy : ingredient.getAllergies()) {
                for (UserAllergy userAllergy : userAllergies) {

                    if (ingredientAllergy.getId()
                            .equals(userAllergy.getAllergy().getId())) {

                        // 🔴 LOG KAYDI
                        Log log = new Log();
                        log.setUser(user);
                        log.setProduct(product);
                        log.setAllergy(ingredientAllergy);
                        log.setActionType("RISKY_SCAN");
                        log.setDescription(
                                "Ürün '" + product.getName() +
                                        "' alerjen içeriyor: " + ingredientAllergy.getName()
                        );
                        log.setLogDate(LocalDate.now());
                        log.setLogTime(LocalTime.now());

                        logRepository.saveAndFlush(log);



                        // ❌ RİSKLİ
                        return Map.of(
                                "safe", false,
                                "message", "⚠️ Bu ürün alerjiniz olan '"
                                        + ingredientAllergy.getName()
                                        + "' maddesini içeriyor.",
                                "ingredient", ingredient.getName()
                        );
                    }
                }
            }
        }

        // 🟢 GÜVENLİ LOG
        Log log = new Log();
        log.setUser(user);
        log.setProduct(product);
        log.setActionType("SAFE_SCAN");
        log.setDescription("Ürün güvenli bulundu");
        log.setLogDate(LocalDate.now());
        log.setLogTime(LocalTime.now());

        logRepository.saveAndFlush(log);




        // ✅ GÜVENLİ
        return Map.of(
                "safe", true,
                "message", "✅ Bu ürün sizin için güvenli"
        );
    }
}

