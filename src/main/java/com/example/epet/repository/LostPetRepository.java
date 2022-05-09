package com.example.epet.repository;

import com.example.epet.model.entities.LostPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LostPetRepository extends JpaRepository<LostPet,Long> {
    List<LostPet> findAllByOwnerId (Long petOwnerId);
    List<LostPet> findAllByNameContaining (String text);
}
