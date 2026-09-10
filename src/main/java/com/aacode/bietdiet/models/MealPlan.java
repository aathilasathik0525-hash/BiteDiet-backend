package com.aacode.bietdiet.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_plans")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "patient_type")
    private String patientType;

    @Column(name = "plan_data", columnDefinition = "LONGTEXT")
    private String planData;

    @Column(name = "updated_at")
    private String updatedAt;

    public MealPlan() {
    }

    public MealPlan(String email, String patientType, String planData, String updatedAt) {
        this.email = email;
        this.patientType = patientType;
        this.planData = planData;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getPlanData() {
        return planData;
    }

    public void setPlanData(String planData) {
        this.planData = planData;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
