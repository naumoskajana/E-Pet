package com.example.epet.repository;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptPetRepository extends JpaRepository<AdoptPet,Long> {
    List<AdoptPet> findAllByOwnerId (Long ownerId);
    List<AdoptPet> findAllByNameContaining (String text);
}
