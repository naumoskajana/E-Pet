package com.example.epet.service;

import com.example.epet.model.entities.SeenPet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface SeenPetService {
    void inform(Long petId, String location, Float coordinate1, Float coordinate2, String address, LocalDate date, LocalDateTime seenTime);
    //    SeenPet findByPet(Long petId);
    SeenPet findById(Long id);
}
