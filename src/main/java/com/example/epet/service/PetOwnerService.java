package com.example.epet.service;

//import com.example.epet.model.entities.AdoptedPet;
import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.SeenPet;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;

import java.util.List;

public interface PetOwnerService extends UserDetailsService{
    List<PetOwner> listAll();
    PetOwner findById(Long id);
    PetOwner deleteById(Long id);
    PetOwner create(String email, String password, String name, String surname);
    PetOwner processOAuthPostLogin(String username,  String name);
    PetOwner login(String username, String password);
    void confirmAdoption(Long idPet, Long idUser, String confirm);
    List<SeenPet> seenPet(Long idUser);
    List<AdoptPet> adoptionNotAcceptedList(Long ownerId);
    List<AdoptPet> adoptedPetsList(Long ownerId);
    void setMessageCount(Long id);
    int loadMessageCount(Long id);
}
