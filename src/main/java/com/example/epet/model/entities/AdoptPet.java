package com.example.epet.model.entities;

import com.example.epet.model.enumeration.PetType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class AdoptPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @ManyToOne
    private PetOwner owner;

    private String photo;

    private String location;

    private String phoneNumber;

    @OneToMany
    private List<PetOwner> wantToAdopt;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/src/main/resources/pet-photos/adopt-pets/" + id + "/" + photo;
    }

    public AdoptPet() {
    }

    public AdoptPet(String name, String description, PetType type, PetOwner owner, String photo, String location, String phoneNumber) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.owner = owner;
        this.photo = photo;
        this.location = location;
        this.phoneNumber = phoneNumber;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PetType getType() {
        return type;
    }

    public void setType(PetType type) {
        this.type = type;
    }

    public PetOwner getOwner() {
        return owner;
    }

    public void setOwner(PetOwner owner) {
        this.owner = owner;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<PetOwner> getWantToAdopt() {
        return wantToAdopt;
    }

    public void setWantToAdopt(List<PetOwner> wantToAdopt) {
        this.wantToAdopt = wantToAdopt;
    }
}
