package com.example.epet.service.implementation;

import com.example.epet.model.entities.LostPet;
import com.example.epet.model.entities.SeenPet;
import com.example.epet.model.exceptions.InvalidPetIdException;
import com.example.epet.repository.LostPetRepository;
import com.example.epet.repository.PetOwnerRepository;
import com.example.epet.repository.SeenPetRepository;
import com.example.epet.service.SeenPetService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class SeenPetServiceImpl implements SeenPetService {

    private final SeenPetRepository seenPetRepository;
    private final LostPetRepository lostPetRepository;
    private final PetOwnerRepository petOwnerRepository;

    public SeenPetServiceImpl(SeenPetRepository seenPetRepository, LostPetRepository lostPetRepository, PetOwnerRepository petOwnerRepository) {
        this.seenPetRepository = seenPetRepository;
        this.lostPetRepository = lostPetRepository;
        this.petOwnerRepository = petOwnerRepository;
    }

    @Override
    public void inform(Long petId, String location, Float coordinate1, Float coordinate2, String address, LocalDate date, LocalDateTime seenTime) {
        LostPet lostPet = this.lostPetRepository.findById(petId).orElseThrow(InvalidPetIdException::new);
        SeenPet seenPet = new SeenPet(lostPet,seenTime,location,coordinate1,coordinate2,address,date);
        this.seenPetRepository.save(seenPet);
        int ownerMessages = lostPet.getOwner().getUnopenedMessages();
        lostPet.getOwner().setUnopenedMessages(ownerMessages+1);
        this.petOwnerRepository.save(lostPet.getOwner());
    }

    @Override
    public SeenPet findById(Long id) {
        return this.seenPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
    }
}
