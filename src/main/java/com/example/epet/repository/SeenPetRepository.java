package com.example.epet.repository;

import com.example.epet.model.entities.SeenPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeenPetRepository extends JpaRepository<SeenPet,Long> {
    List<SeenPet> findAllByLostPet_OwnerId(Long ownerId);
    List<SeenPet> findAllByLostPetId(Long petId);
}
