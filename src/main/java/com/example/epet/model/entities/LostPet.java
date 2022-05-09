package com.example.epet.model.entities;

import com.example.epet.model.enumeration.PetType;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class LostPet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private String description;

    @Enumerated(EnumType.STRING)
    private PetType type;

    @ManyToOne
    private PetOwner owner;

    private String photo;

    private String location;

    private Float coordinate1;

    private Float coordinate2;

    private String phoneNumber;

    private LocalDate date;

    private String address;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/src/main/resources/pet-photos/lost-pets/" + id + "/" + photo;
    }

    public LostPet() {
    }

    public LostPet(String name, Integer age, String description, PetType type, PetOwner owner, String photo, String location, Float coordinate1, Float coordinate2, String phoneNumber, LocalDate date, String address) {
        this.name = name;
        this.age = age;
        this.description = description;
        this.type = type;
        this.owner = owner;
        this.photo = photo;
        this.location = location;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.address = address;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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

    public Float getCoordinate1() {
        return coordinate1;
    }

    public void setCoordinate1(Float coordinate1) {
        this.coordinate1 = coordinate1;
    }

    public Float getCoordinate2() {
        return coordinate2;
    }

    public void setCoordinate2(Float coordinate2) {
        this.coordinate2 = coordinate2;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
