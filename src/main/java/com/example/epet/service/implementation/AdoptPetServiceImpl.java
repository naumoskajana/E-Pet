package com.example.epet.service.implementation;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.model.exceptions.InvalidPetIdException;
import com.example.epet.model.exceptions.InvalidPetOwnerIdException;
import com.example.epet.repository.AdoptPetRepository;
import com.example.epet.repository.PetOwnerRepository;
import com.example.epet.service.AdoptPetService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdoptPetServiceImpl implements AdoptPetService {

    private final AdoptPetRepository adoptPetRepository;
    private final PetOwnerRepository petOwnerRepository;

    public AdoptPetServiceImpl(AdoptPetRepository adoptPetRepository, PetOwnerRepository petOwnerRepository) {
        this.adoptPetRepository = adoptPetRepository;
        this.petOwnerRepository = petOwnerRepository;
    }

    @Override
    public List<AdoptPet> listAll() {
        return this.adoptPetRepository.findAll();
    }

    @Override
    public AdoptPet findById(Long id) {
        return this.adoptPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
    }

    @Override
    public void deleteById(Long id) {
        AdoptPet adoptPet = this.adoptPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
        PetOwner petOwner = adoptPet.getOwner();
        petOwner.getAdoptPets().remove(adoptPet);
        this.petOwnerRepository.save(petOwner);
        this.adoptPetRepository.delete(adoptPet);
    }

    @Override
    public AdoptPet create(String name, String description, Long ownerId, String photo, String location, String phoneNumber) {
        PetType type = PetType.ADOPT;
        PetOwner petOwner = this.petOwnerRepository.findById(ownerId).orElseThrow(InvalidPetOwnerIdException::new);
        AdoptPet adoptPet = new AdoptPet(name,description,type,petOwner,photo,location,phoneNumber);
        petOwner.getAdoptPets().add(adoptPet);
        this.adoptPetRepository.save(adoptPet);
        this.petOwnerRepository.save(petOwner);
        return adoptPet;
    }

    @Override
    public AdoptPet update(Long id, String name, String description, String photo, String location, String phoneNumber, PetType type) {
        AdoptPet adoptPet = this.adoptPetRepository.findById(id).orElseThrow(InvalidPetIdException::new);
        adoptPet.setName(name);
        adoptPet.setDescription(description);
        if (photo != null){
            adoptPet.setPhoto(photo);
        }
        adoptPet.setLocation(location);
        adoptPet.setPhoneNumber(phoneNumber);
        adoptPet.setType(type);
        this.adoptPetRepository.save(adoptPet);
        return adoptPet;
    }

    @Override
    public List<AdoptPet> findAllByPetOwner(Long petOwnerId) {
        return this.adoptPetRepository.findAllByOwnerId(petOwnerId);
    }

    @Override
    public List<AdoptPet> findAllByNameLike(String text) {
        return this.adoptPetRepository.findAllByNameContaining(text);
    }

    @Override
    public void adoptAPet(Long idPet, Long idUser) {
        AdoptPet adoptPet = this.adoptPetRepository.findById(idPet).orElseThrow(InvalidPetIdException::new);
        PetOwner petOwner = this.petOwnerRepository.findById(idUser).orElseThrow(InvalidPetOwnerIdException::new);
        adoptPet.getWantToAdopt().add(petOwner);
        this.adoptPetRepository.save(adoptPet);
        int ownerMessages = adoptPet.getOwner().getUnopenedMessages();
        adoptPet.getOwner().setUnopenedMessages(ownerMessages+1);
        this.petOwnerRepository.save(adoptPet.getOwner());
    }

    @Override
    public List<AdoptPet> wantToBeAdopted(Long petOwnerId) {
        PetOwner petOwner = this.petOwnerRepository.findById(petOwnerId).orElseThrow(InvalidPetOwnerIdException::new);
        List<AdoptPet> pets = petOwner.getAdoptPets();
        return pets.stream().filter(p -> !p.getWantToAdopt().isEmpty()).collect(Collectors.toList());
    }

}
