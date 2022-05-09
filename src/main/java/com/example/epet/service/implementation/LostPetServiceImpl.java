package com.example.epet.service.implementation;

import com.example.epet.model.entities.LostPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.SeenPet;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.model.exceptions.InvalidPetIdException;
import com.example.epet.model.exceptions.InvalidPetOwnerIdException;
import com.example.epet.repository.LostPetRepository;
import com.example.epet.repository.PetOwnerRepository;
import com.example.epet.repository.SeenPetRepository;
import com.example.epet.service.LostPetService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LostPetServiceImpl implements LostPetService {

    private final LostPetRepository lostPetRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final SeenPetRepository seenPetRepository;

    public LostPetServiceImpl(LostPetRepository lostPetRepository, PetOwnerRepository petOwnerRepository, SeenPetRepository seenPetRepository) {
        this.lostPetRepository = lostPetRepository;
        this.petOwnerRepository = petOwnerRepository;
        this.seenPetRepository = seenPetRepository;
    }

    @Override
    public List<LostPet> listAll() {
        return this.lostPetRepository.findAll();
    }

    @Override
    public LostPet findById(Long id) {
        return this.lostPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
    }

    @Override
    public void deleteById(Long id) {
        LostPet lostPet = this.lostPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
        PetOwner petOwner = lostPet.getOwner();
        petOwner.getLostPets().remove(lostPet);
        this.lostPetRepository.delete(lostPet);
    }

    @Override
    public LostPet create(String name, Integer age, String description, Long ownerId, String photo, String location, Float coordinate1, Float coordinate2, String phoneNumber, LocalDate date, String address) {
        PetType type = PetType.LOST;
        PetOwner petOwner = this.petOwnerRepository.findById(ownerId).orElseThrow(InvalidPetOwnerIdException::new);
        LostPet lostPet = new LostPet(name,age,description,type,petOwner,photo,location,coordinate1,coordinate2,phoneNumber,date,address);
        petOwner.getLostPets().add(lostPet);
        this.lostPetRepository.save(lostPet);
        return lostPet;
    }

    @Override
    public LostPet update(Long id, String name, Integer age, String description, String photo, String location, Float coordinate1, Float coordinate2, String phoneNumber, LocalDate date, PetType type, String address) {
        LostPet lostPet = this.lostPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
        lostPet.setName(name);
        lostPet.setAge(age);
        lostPet.setDescription(description);
        if (photo != null){
            lostPet.setPhoto(photo);
        }
        lostPet.setLocation(location);
        lostPet.setCoordinate1(coordinate1);
        lostPet.setCoordinate2(coordinate2);
        lostPet.setType(type);
        lostPet.setPhoneNumber(phoneNumber);
        lostPet.setDate(date);
        lostPet.setAddress(address);
        this.lostPetRepository.save(lostPet);
        return lostPet;
    }

    @Override
    public List<LostPet> findAllByPetOwner(Long petOwnerId) {
        return this.lostPetRepository.findAllByOwnerId(petOwnerId);
    }

    @Override
    public List<LostPet> findAllByNameLike(String text) {
        return this.lostPetRepository.findAllByNameContaining(text);
    }

    @Override
    public void seenPet(Long id, String confirm) {
        SeenPet seenPet = this.seenPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
        if (confirm.equals("true")){
            LostPet lostPet = this.lostPetRepository.findById(seenPet.getLostPet().getId()).orElseThrow(InvalidPetIdException::new);
            lostPet.setLocation(seenPet.getLocation());
            lostPet.setCoordinate1(seenPet.getCoordinate1());
            lostPet.setCoordinate2(seenPet.getCoordinate2());
            lostPet.setDate(seenPet.getDate());
            lostPet.setAddress(seenPet.getAddress());
            this.lostPetRepository.save(lostPet);
            this.seenPetRepository.delete(seenPet);
        }
        else {
            this.seenPetRepository.delete(seenPet);
        }
    }
}
