package com.example.epet.web.controller;

import com.example.epet.model.entities.AdoptPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.service.AdoptPetService;
import com.example.epet.service.PetOwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class AdoptPetController {

    private final AdoptPetService adoptPetService;
    private final PetOwnerService petOwnerService;

    public AdoptPetController(AdoptPetService adoptPetService, PetOwnerService petOwnerService) {
        this.adoptPetService = adoptPetService;
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/adoptPets")
    public String getAdoptPage(Model model, HttpSession session){
        List<AdoptPet> adoptPets = null;
        model.addAttribute("typeADOPT", PetType.valueOf(PetType.ADOPT.name()));
        model.addAttribute("typeADOPTED", PetType.valueOf(PetType.ADOPTED.name()));
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        if (petOwner == null){
            return "redirect:/login";
        }
        int messages = this.petOwnerService.loadMessageCount(petOwner.getId());
        model.addAttribute("messages",messages);
        model.addAttribute("loggedIn",true);
        model.addAttribute("user",petOwner);
        List<AdoptPet> foundAdoptPetsLike = (List<AdoptPet>) session.getAttribute("foundAdoptPetsLike");
        if (foundAdoptPetsLike == null){
            adoptPets = this.adoptPetService.listAll();
        }
        else {
            adoptPets = foundAdoptPetsLike;
            session.removeAttribute("foundAdoptPetsLike");
        }
        model.addAttribute("adoptPets", adoptPets);
        return "adoptPets";
    }

    @PostMapping("/adoptPets")
    public String petPage(@RequestParam String fragment,
                          HttpSession session){
        List<AdoptPet> foundAdoptPetsLike = this.adoptPetService.findAllByNameLike(fragment);
        session.setAttribute("foundAdoptPetsLike",foundAdoptPetsLike);
        return "redirect:/adoptPets";
    }

    @GetMapping("/adoptPet")
    public String addPet(Model model){
        model.addAttribute("types",PetType.values());
        return "adoptPet";
    }

    @PostMapping("/adoptPet")
    public RedirectView savePet(@RequestParam String name,
                                @RequestParam String description,
                                @RequestParam String location,
                                @RequestParam String phoneNumber,
                                HttpSession session,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        Long ownerId = petOwner.getId();

        AdoptPet savedAdoptPet = this.adoptPetService.create(name,description,ownerId,fileName,location,phoneNumber);

        String uploadDir = "src/main/resources/pet-photos/adopt-pets/" + savedAdoptPet.getId();
        com.example.epet.web.FileUpload.FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        return new RedirectView("/adoptPets",true);
    }

    @GetMapping("/adoptPet/{petId}")
    public String getEditPet(@PathVariable Long petId, Model model){
        AdoptPet adoptPet = this.adoptPetService.findById(petId);
        model.addAttribute("foundAdoptPet",adoptPet);
        model.addAttribute("types",PetType.values());
        return "adoptPet";
    }

    @PostMapping("/adoptPet/{petId}")
    public String editPet(@RequestParam String name,
                          @RequestParam String description,
                          @RequestParam String location,
                          @RequestParam String phoneNumber,
                          @RequestParam(value = "image",required = false) MultipartFile multipartFile,
                          @PathVariable Long petId,
                          @RequestParam PetType type) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        AdoptPet savedAdoptPet = this.adoptPetService.update(petId,name,description,fileName,location,phoneNumber,type);

        String uploadDir = "src/main/resources/pet-photos/adopt-pets/" + savedAdoptPet.getId();
        com.example.epet.web.FileUpload.FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        return "redirect:/adoptPets";
    }

    @GetMapping("/deleteAdoptPet/{petId}")
    public String deletePet(@PathVariable Long petId){
        this.adoptPetService.deleteById(petId);
        return "redirect:/profile";
    }

    @GetMapping("/adoptForm/{petId}")
    public String getForm(@PathVariable Long petId, Model model, HttpSession session){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        Long ownerId = petOwner.getId();
        this.adoptPetService.adoptAPet(petId,ownerId);
        return "redirect:/adoptPets";
    }

}
