package com.projinteg.GesEvents.entities;

import jakarta.persistence.*;

@Entity
public class Company {
    @Enumerated(EnumType.STRING)
    private final Role role = Role.ORGANIZATION;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private String responsable;
    private String category;

    private String email;
    private String numTel;

    private String password;

    @Column(nullable = false)
    private boolean confirmed = false;

    public Company() {

    }

    public Company(String password, String numTel, String email, String category, String responsable, String name) {
        this.password = password;
        this.numTel = numTel;
        this.email = email;
        this.category = category;
        this.responsable = responsable;
        this.name = name;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

}

