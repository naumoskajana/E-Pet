package com.example.epet.service;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.enumeration.PetType;

import java.util.List;

public interface AdoptPetService {
    List<AdoptPet> listAll();
    AdoptPet findById(Long id);
    void deleteById(Long id);
    AdoptPet create(String name, String description, Long ownerId, String photo, String location, String phoneNumber);
    AdoptPet update(Long id, String name, String description, String photo, String location, String phoneNumber, PetType type);
    List<AdoptPet> findAllByPetOwner(Long petOwnerId);
    List<AdoptPet> findAllByNameLike (String text);
    void adoptAPet(Long idPet, Long idUser);
    List<AdoptPet> wantToBeAdopted(Long petOwnerId);
}
