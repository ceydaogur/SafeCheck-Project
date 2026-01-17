package com.example.safecheck.controller;

import com.example.safecheck.dto.AddAllergyRequest;
import com.example.safecheck.entity.Allergy;
import com.example.safecheck.entity.User;
import com.example.safecheck.entity.UserAllergy;
import com.example.safecheck.repository.AllergyRepository;
import com.example.safecheck.repository.UserAllergyRepository;
import com.example.safecheck.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-allergies")
@CrossOrigin
public class UserAllergyController {

    private final UserAllergyRepository userAllergyRepository;
    private final AllergyRepository allergyRepository;
    private final UserRepository userRepository;

    public UserAllergyController(
            UserAllergyRepository userAllergyRepository,
            AllergyRepository allergyRepository,
            UserRepository userRepository
    ) {
        this.userAllergyRepository = userAllergyRepository;
        this.allergyRepository = allergyRepository;
        this.userRepository = userRepository;
    }

    // 🔹 KULLANICIYA ALERJİ EKLE
    @PostMapping
    public void addAllergy(@RequestBody AddAllergyRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 🔍 Alerji var mı?
        Allergy allergy = allergyRepository
                .findByName(request.getAllergyName())
                .orElseGet(() -> {
                    Allergy newAllergy = new Allergy();
                    newAllergy.setName(request.getAllergyName());
                    return allergyRepository.save(newAllergy);
                });

        // 🔥 USER_ALLERGY KAYDI
        UserAllergy userAllergy = new UserAllergy();
        userAllergy.setUser(user);
        userAllergy.setAllergy(allergy);
        userAllergy.setSeverity(request.getSeverity());
        userAllergy.setNotes(request.getNotes());

        userAllergyRepository.save(userAllergy);
    }

    // 🔹 KULLANICININ ALERJİLERİNİ GETİR
    @GetMapping("/user/{userId}")
    public List<UserAllergy> getUserAllergies(@PathVariable Long userId) {
        return userAllergyRepository.findByUserId(userId);
    }

    // 🔹 ALERJİ SİL
    @DeleteMapping("/{id}")
    public void deleteUserAllergy(@PathVariable Long id) {
        userAllergyRepository.deleteById(id);
    }
}
