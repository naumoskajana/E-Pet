package com.example.epet.web.controller;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.LostPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.SeenPet;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.service.AdoptPetService;
import com.example.epet.service.LostPetService;
import com.example.epet.service.PetOwnerService;
import com.example.epet.service.SeenPetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PetOwnerController {

    private final PetOwnerService petOwnerService;
    private final LostPetService lostPetService;
    private final SeenPetService seenPetService;
    private final AdoptPetService adoptPetService;

    public PetOwnerController(PetOwnerService petOwnerService, LostPetService lostPetService, SeenPetService seenPetService, AdoptPetService adoptPetService) {
        this.petOwnerService = petOwnerService;
        this.lostPetService = lostPetService;
        this.seenPetService = seenPetService;
        this.adoptPetService = adoptPetService;
    }

    @GetMapping("/profile")
    public String getProfile(HttpSession session,
                             Model model){
        PetOwner owner = (PetOwner) session.getAttribute("user");
        if (owner == null){
            return "redirect:/login";
        }
        int messages = this.petOwnerService.loadMessageCount(owner.getId());
        model.addAttribute("messages",messages);
        model.addAttribute("loggedIn",true);
        Long ownerId = owner.getId();
        List<LostPet> lostPets = this.lostPetService.findAllByPetOwner(ownerId);
        List<AdoptPet> adoptPets = this.adoptPetService.findAllByPetOwner(ownerId);
        model.addAttribute("pets", lostPets);
        model.addAttribute("adopts",adoptPets);
        model.addAttribute("typeLOST", PetType.valueOf(PetType.LOST.name()));
        model.addAttribute("typeADOPT", PetType.valueOf(PetType.ADOPT.name()));
        return "profile";
    }

    @GetMapping("/profile/confirmation/{petId}/{ownerId}")
    public String confirmationForm(@PathVariable Long petId,
                                   @PathVariable Long ownerId,
                                   Model model){
        AdoptPet wantToBeAdopted = this.adoptPetService.findById(petId);
        PetOwner petOwner = this.petOwnerService.findById(ownerId);
        model.addAttribute("wantToAdopt",petOwner);
        model.addAttribute("wantAdopted", wantToBeAdopted);
        return "confirmationForm";
    }

    @PostMapping("/profile/confirmation/{petId}/{ownerId}")
    public String confirmation(@PathVariable Long petId,
                               @PathVariable Long ownerId,
                               @RequestParam String confirmation){
        PetOwner petOwner = this.petOwnerService.findById(ownerId);
        this.petOwnerService.confirmAdoption(petId,petOwner.getId(),confirmation);
        return "redirect:/profile";
    }

    @GetMapping("/profile/messages")
    public String messages(Model model, HttpSession session){
        PetOwner owner = (PetOwner) session.getAttribute("user");
        if (owner == null){
            return "redirect:/login";
        }
        model.addAttribute("loggedIn",true);
        List<AdoptPet> wantToBeAdopted = this.adoptPetService.wantToBeAdopted(owner.getId());
        List<AdoptPet> adoptedPets = this.petOwnerService.adoptedPetsList(owner.getId());
        List<AdoptPet> adoptionNotAccepteds = this.petOwnerService.adoptionNotAcceptedList(owner.getId());
        List<SeenPet> seenPets = this.petOwnerService.seenPet(owner.getId());
        if (wantToBeAdopted != null){
            model.addAttribute("want", wantToBeAdopted);
        }
        if (adoptedPets != null){
            model.addAttribute("adoptedPets",adoptedPets);
        }
        if (adoptionNotAccepteds != null){
            model.addAttribute("notAdoptedPets",adoptionNotAccepteds);
        }
        if (seenPets != null){
            model.addAttribute("seenPets",seenPets);
        }
        this.petOwnerService.setMessageCount(owner.getId());
        int messages = this.petOwnerService.loadMessageCount(owner.getId());
        model.addAttribute("messages",messages);
        return "messages";
    }

    @GetMapping("/profile/seenPets/{petId}")
    public String editForm(@PathVariable Long petId,
                           Model model){
        SeenPet seenPet = this.seenPetService.findById(petId);
        model.addAttribute("seenPet", seenPet);
        return "seenPet";
    }

    @PostMapping("/profile/seenPets/{petId}")
    public String edit(@PathVariable Long petId,
                       @RequestParam String confirmation,
                       Model model){
        this.lostPetService.seenPet(petId,confirmation);
        return "redirect:/profile/messages";
    }

}
