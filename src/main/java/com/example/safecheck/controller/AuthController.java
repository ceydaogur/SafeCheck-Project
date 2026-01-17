package com.example.safecheck.controller;

import com.example.safecheck.dto.*;
import com.example.safecheck.entity.Log;
import com.example.safecheck.entity.User;
import com.example.safecheck.repository.LogRepository;
import com.example.safecheck.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          LogRepository logRepository,
                          BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ REGISTER
    @PostMapping("/register")
    @Transactional
    public String register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.email)) {
            throw new RuntimeException("Email zaten kayıtlı");
        }

        User user = new User();
        user.setName(request.name);
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));

        userRepository.save(user);

        // 📝 REGISTER LOG
        Log log = new Log();
        log.setUser(user);
        log.setActionType("REGISTER");
        log.setDescription("Kullanıcı sisteme kayıt oldu");
        log.setLogDate(LocalDate.now());
        log.setLogTime(LocalTime.now());

        logRepository.save(log);

        return "Kayıt başarılı";
    }

    // ✅ LOGIN
    @PostMapping("/login")
    @Transactional
    public AuthResponse login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.password, user.getPassword())) {
            throw new RuntimeException("Şifre hatalı");
        }

        // 📝 LOGIN LOG
        Log log = new Log();
        log.setUser(user);
        log.setActionType("LOGIN");
        log.setDescription("Kullanıcı giriş yaptı");
        log.setLogDate(LocalDate.now());
        log.setLogTime(LocalTime.now());

        logRepository.save(log);

        String token = "dummy-token-" + user.getId();

        return new AuthResponse(
                user.getId(),
                token,
                user.getName(),
                user.getEmail()
        );
    }


    @PostMapping("/logout/{userId}")
    @Transactional
    public String logout(@PathVariable Long userId) {

        System.out.println(">>> LOGOUT ENDPOINT CALISTI: " + userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Log log = new Log();
        log.setUser(user);
        log.setActionType("LOGOUT");
        log.setDescription("Kullanıcı çıkış yaptı");
        log.setLogDate(LocalDate.now());
        log.setLogTime(LocalTime.now());

        logRepository.save(log);

        return "Çıkış yapıldı";
    }




}
