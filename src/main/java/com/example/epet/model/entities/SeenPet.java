package com.example.epet.model.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class SeenPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private LostPet lostPet;

    private LocalDateTime seenTime;

    private String location;

    private Float coordinate1;

    private Float coordinate2;

    private String address;

    private LocalDate date;

    public SeenPet() {
    }

    public SeenPet(LostPet lostPet, LocalDateTime seenTime) {
        this.lostPet = lostPet;
        this.seenTime = seenTime;
    }

    public SeenPet(LostPet lostPet, LocalDateTime seenTime, String location, Float coordinate1, Float coordinate2, String address, LocalDate date) {
        this.lostPet = lostPet;
        this.seenTime = seenTime;
        this.location = location;
        this.coordinate1 = coordinate1;
        this.coordinate2 = coordinate2;
        this.address = address;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LostPet getLostPet() {
        return lostPet;
    }

    public void setLostPet(LostPet lostPet) {
        this.lostPet = lostPet;
    }

    public LocalDateTime getSeenTime() {
        return seenTime;
    }

    public void setSeenTime(LocalDateTime seenTime) {
        this.seenTime = seenTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
