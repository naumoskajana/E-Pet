package com.example.epet.model.entities;

import com.example.epet.model.enumeration.ProviderType;
import com.example.epet.model.enumeration.UserType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
public class PetOwner implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String surname;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @OneToMany
    private List<LostPet> lostPets;

    @OneToMany
    private List<AdoptPet> adoptPets;

    @OneToMany
    private List<AdoptPet> adoptedPets;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany
    private List<AdoptPet> adoptionNotAccepted;

    private int unopenedMessages;

    public PetOwner() {
    }

    public PetOwner(String email, String password, String name, String surname, UserType type, ProviderType providerType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.type = type;
        this.providerType = providerType;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(type);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public List<LostPet> getLostPets() {
        return lostPets;
    }

    public void setLostPets(List<LostPet> lostPets) {
        this.lostPets = lostPets;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public List<AdoptPet> getAdoptPets() {
        return adoptPets;
    }

    public void setAdoptPets(List<AdoptPet> adoptPets) {
        this.adoptPets = adoptPets;
    }

    public List<AdoptPet> getAdoptionNotAccepted() {
        return adoptionNotAccepted;
    }

    public void setAdoptionNotAccepted(List<AdoptPet> adoptionNotAccepted) {
        this.adoptionNotAccepted = adoptionNotAccepted;
    }

    public List<AdoptPet> getAdoptedPets() {
        return adoptedPets;
    }

    public void setAdoptedPets(List<AdoptPet> adoptedPets) {
        this.adoptedPets = adoptedPets;
    }

    public int getUnopenedMessages() {
        return unopenedMessages;
    }

    public void setUnopenedMessages(int unopenedMessages) {
        this.unopenedMessages = unopenedMessages;
    }
}
