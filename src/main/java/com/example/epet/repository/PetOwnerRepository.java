package com.example.epet.repository;

import com.example.epet.model.entities.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetOwnerRepository extends JpaRepository<PetOwner,Long> {
    @Query("SELECT u FROM PetOwner u WHERE u.email = :email")
    PetOwner getUserByUsername(@Param("email") String username);
    PetOwner findByEmail (String email);
    PetOwner findByEmailAndPassword (String email, String password);
}
