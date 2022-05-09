package com.example.epet.service;

import com.example.epet.model.entities.LostPet;
import com.example.epet.model.enumeration.PetType;

import java.time.LocalDate;
import java.util.List;

public interface LostPetService {
    List<LostPet> listAll();
    LostPet findById(Long id);
    void deleteById(Long id);
    LostPet create(String name, Integer age, String description, Long ownerId, String photo, String location, Float coordinate1, Float coordinate2, String phoneNumber, LocalDate date, String address);
    LostPet update(Long id, String name, Integer age, String description, String photo, String location, Float coordinate1, Float coordinate2, String phoneNumber, LocalDate date, PetType type, String address);
    List<LostPet> findAllByPetOwner(Long petOwnerId);
    List<LostPet> findAllByNameLike (String text);
    void seenPet(Long id, String confirm);
}
