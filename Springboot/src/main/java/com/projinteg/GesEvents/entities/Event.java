package com.projinteg.GesEvents.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String description;
    private LocalDate date;
    private LocalTime time;
    private String localisation;
    private String imageName;
    private String imageType;
    private String animateur;

    @Enumerated(EnumType.STRING)
    private Etat etat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;

    public Event() {
    }

    public Event(String nom, String description, LocalDate date, LocalTime time, String localisation, String imageName, String imageType, byte[] imageData, String animateur, Etat etat, Company company) {
        this.nom = nom;
        this.description = description;
        this.date = date;
        this.time = time;
        this.localisation = localisation;
        this.imageName = imageName;
        this.imageType = imageType;
        this.imageData = imageData;
        this.animateur = animateur;
        this.etat = etat;
        this.company = company;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Long getId() {
        return id;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }


    public String getAnimateur() {
        return animateur;
    }

    public void setAnimateur(String animateur) {
        this.animateur = animateur;
    }

    public Etat getEtat() {
        return etat;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }


}
