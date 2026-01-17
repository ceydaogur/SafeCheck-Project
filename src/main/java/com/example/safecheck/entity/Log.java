package com.example.safecheck.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer id;

    // USER
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // PRODUCT
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // ALLERGY
    @ManyToOne
    @JoinColumn(name = "allergy_id")
    private Allergy allergy;

    @Column(name = "action_type")
    private String actionType;

    private String description;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "log_time")
    private LocalTime logTime;

    // ---- GETTER / SETTER ----
    public Integer getId() { return id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Allergy getAllergy() { return allergy; }
    public void setAllergy(Allergy allergy) { this.allergy = allergy; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getLogDate() { return logDate; }
    public void setLogDate(LocalDate logDate) { this.logDate = logDate; }

    public LocalTime getLogTime() { return logTime; }
    public void setLogTime(LocalTime logTime) { this.logTime = logTime; }
}
