package com.example.epet.service.implementation;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.SeenPet;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.model.enumeration.ProviderType;
import com.example.epet.model.enumeration.UserType;
import com.example.epet.model.exceptions.InvalidPetIdException;
import com.example.epet.model.exceptions.InvalidPetOwnerIdException;
import com.example.epet.repository.AdoptPetRepository;
import com.example.epet.repository.PetOwnerRepository;
import com.example.epet.repository.SeenPetRepository;
import com.example.epet.service.PetOwnerService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetOwnerServiceImpl implements PetOwnerService {

    private final PetOwnerRepository petOwnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdoptPetRepository adoptPetRepository;
    private final SeenPetRepository seenPetRepository;

    public PetOwnerServiceImpl(PetOwnerRepository petOwnerRepository, PasswordEncoder passwordEncoder, AdoptPetRepository adoptPetRepository, SeenPetRepository seenPetRepository) {
        this.petOwnerRepository = petOwnerRepository;
        this.passwordEncoder = passwordEncoder;
        this.adoptPetRepository = adoptPetRepository;
        this.seenPetRepository = seenPetRepository;
    }

    @Override
    public List<PetOwner> listAll() {
        return this.petOwnerRepository.findAll();
    }

    @Override
    public PetOwner findById(Long id) {
        return this.petOwnerRepository.findById(id).orElseThrow(InvalidPetOwnerIdException::new);
    }

    @Override
    public PetOwner deleteById(Long id) {
        PetOwner petOwner = this.petOwnerRepository.findById(id).orElseThrow(InvalidPetOwnerIdException::new);
        this.petOwnerRepository.delete(petOwner);
        return petOwner;
    }

    @Override
    public PetOwner create(String email, String password, String name, String surname) {
        UserType type = UserType.OWNER;
        PetOwner petOwner = new PetOwner(email,passwordEncoder.encode(password),name,surname,type, ProviderType.LOCAL);
        return this.petOwnerRepository.save(petOwner);
    }

    @Override
    public PetOwner processOAuthPostLogin(String username, String name) {
        PetOwner existOwner = this.petOwnerRepository.getUserByUsername(username);

        if (existOwner == null) {
            PetOwner newOwner = new PetOwner();
            newOwner.setName(name);
            newOwner.setEmail(username);
            newOwner.setProviderType(ProviderType.GOOGLE);
            newOwner.setType(UserType.OWNER);

            return this.petOwnerRepository.save(newOwner);
        }
        return existOwner;
    }

    @Override
    public PetOwner login(String username, String password) {
        return this.petOwnerRepository.findByEmailAndPassword(username,passwordEncoder.encode(password));
    }

    @Override
    public void confirmAdoption(Long idPet, Long idUser, String confirm) {
        AdoptPet adoptPet = this.adoptPetRepository.findById(idPet).orElseThrow(InvalidPetIdException::new);
        PetOwner petOwner = this.petOwnerRepository.findById(idUser).orElseThrow(InvalidPetOwnerIdException::new);
        if (confirm.equals("true")){
            adoptPet.getWantToAdopt().clear();
            adoptPet.getOwner().getAdoptPets().remove(adoptPet);
            adoptPet.setOwner(petOwner);
            adoptPet.setType(PetType.ADOPTED);
            this.adoptPetRepository.save(adoptPet);
            int ownerMessages = petOwner.getUnopenedMessages();
            petOwner.setUnopenedMessages(ownerMessages+1);
            petOwner.getAdoptedPets().add(adoptPet);
            this.petOwnerRepository.save(petOwner);
        }
        else {
            adoptPet.getWantToAdopt().remove(petOwner);
            this.adoptPetRepository.save(adoptPet);
            petOwner.getAdoptionNotAccepted().add(adoptPet);
            int ownerMessages = petOwner.getUnopenedMessages();
            petOwner.setUnopenedMessages(ownerMessages+1);
            this.petOwnerRepository.save(petOwner);
        }
    }

    @Override
    public List<SeenPet> seenPet(Long idUser) {
        return this.seenPetRepository.findAllByLostPet_OwnerId(idUser);
    }

    @Override
    public List<AdoptPet> adoptionNotAcceptedList(Long ownerId) {
        PetOwner petOwner = this.petOwnerRepository.findById(ownerId).orElseThrow(InvalidPetOwnerIdException::new);
        return petOwner.getAdoptionNotAccepted();
    }

    @Override
    public List<AdoptPet> adoptedPetsList(Long ownerId) {
        PetOwner petOwner = this.petOwnerRepository.findById(ownerId).orElseThrow(InvalidPetOwnerIdException::new);
        return petOwner.getAdoptedPets();
    }

    @Override
    public void setMessageCount(Long id) {
        PetOwner petOwner = this.petOwnerRepository.findById(id).orElseThrow(InvalidPetOwnerIdException::new);
        petOwner.setUnopenedMessages(0);
        this.petOwnerRepository.save(petOwner);
    }

    @Override
    public int loadMessageCount(Long id) {
        PetOwner petOwner = this.petOwnerRepository.findById(id).orElseThrow(InvalidPetOwnerIdException::new);
        return petOwner.getUnopenedMessages();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.petOwnerRepository.findByEmail(username);
    }
}
