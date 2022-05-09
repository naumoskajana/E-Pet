package com.example.epet.web.controller;

import com.example.epet.model.entities.LostPet;
import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.enumeration.PetType;
import com.example.epet.service.LostPetService;
import com.example.epet.service.PetOwnerService;
import com.example.epet.service.SeenPetService;
import com.google.gson.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
public class LostPetController {

    private final LostPetService lostPetService;
    private final SeenPetService seenPetService;
    private final PetOwnerService petOwnerService;

    public LostPetController(LostPetService lostPetService, SeenPetService seenPetService, PetOwnerService petOwnerService) {
        this.lostPetService = lostPetService;
        this.seenPetService = seenPetService;
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/lostPets")
    public String getPetPage(Model model, HttpSession session, HttpServletRequest request){
        List<LostPet> lostPets = null;
        model.addAttribute("type", PetType.valueOf(PetType.LOST.name()));
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        if (petOwner != null){
            int messages = this.petOwnerService.loadMessageCount(petOwner.getId());
            model.addAttribute("messages",messages);
            model.addAttribute("loggedIn",true);
        }
        List<LostPet> foundPetsLike = (List<LostPet>) session.getAttribute("foundPetsLike");
        if (foundPetsLike == null){
            lostPets = this.lostPetService.listAll();
        }
        else {
            lostPets = foundPetsLike;
            session.removeAttribute("foundPetsLike");
        }
        model.addAttribute("pets", lostPets);
        return "lostPets";
    }

    @PostMapping("/lostPets")
    public String petPage(@RequestParam String fragment,
                          HttpSession session){
        List<LostPet> foundPetsLike = this.lostPetService.findAllByNameLike(fragment);
        session.setAttribute("foundPetsLike",foundPetsLike);
        return "redirect:/lostPets";
    }

    @GetMapping("/lostPet")
    public String addPet(Model model){
        model.addAttribute("types",PetType.values());
        return "lostPet";
    }

    @PostMapping("/lostPet")
    public RedirectView savePet(@RequestParam String name,
                                @RequestParam Integer age,
                                @RequestParam String description,
                                @RequestParam String location,
                                @RequestParam String coordinates,
                                @RequestParam String phoneNumber,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                HttpSession session,
                                @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        Long ownerId = petOwner.getId();

        String address = String.join("+",coordinates.split(" "));
        String locationUrl = "https://nominatim.openstreetmap.org/search.php?q=" + address + "&format=jsonv2";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(locationUrl, String.class);
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(Objects.requireNonNull(result.getBody()));
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        JsonElement jsonElement1 = jsonObject.get("lat");
        Float coordinate1 = jsonElement1.getAsFloat();
        JsonElement jsonElement2 = jsonObject.get("lon");
        Float coordinate2 = jsonElement2.getAsFloat();

        LostPet savedPet = this.lostPetService.create(name,age,description,ownerId,fileName,location,coordinate1,coordinate2,phoneNumber,date,coordinates);

        String uploadDir = "src/main/resources/pet-photos/lost-pets/" + savedPet.getId();
        com.example.epet.web.FileUpload.FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);

        return new RedirectView("/lostPets",true);
    }

    @GetMapping("/lostPet/{petId}")
    public String getEditPet(@PathVariable Long petId, Model model){
        LostPet lostPet = this.lostPetService.findById(petId);
        model.addAttribute("foundPet",lostPet);
        model.addAttribute("types",PetType.values());
        return "lostPet";
    }

    @PostMapping("/lostPet/{petId}")
    public String editPet(@RequestParam String name,
                          @RequestParam Integer age,
                          @RequestParam String description,
                          @RequestParam String location,
                          @RequestParam String coordinates,
                          @RequestParam String phoneNumber,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          @RequestParam(value = "image",required = false) MultipartFile multipartFile,
                          @PathVariable Long petId,
                          @RequestParam PetType type) throws IOException {
        String fileName = null;
        if (!multipartFile.isEmpty()){
            fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        }

        String address = String.join("+",coordinates.split(" "));
        String locationUrl = "https://nominatim.openstreetmap.org/search.php?q=" + address + "&format=jsonv2";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(locationUrl, String.class);
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(Objects.requireNonNull(result.getBody()));
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        JsonElement jsonElement1 = jsonObject.get("lat");
        Float coordinate1 = jsonElement1.getAsFloat();
        JsonElement jsonElement2 = jsonObject.get("lon");
        Float coordinate2 = jsonElement2.getAsFloat();

        LostPet savedPet = this.lostPetService.update(petId,name,age,description,fileName,location,coordinate1,coordinate2,phoneNumber,date,type,coordinates);

        if (!multipartFile.isEmpty()){
            String uploadDir = "src/main/resources/pet-photos/lost-pets/" + savedPet.getId();
            com.example.epet.web.FileUpload.FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }

        return "redirect:/lostPets";
    }

    @GetMapping("/deletePet/{petId}")
    public String deletePet(@PathVariable Long petId){
        this.lostPetService.deleteById(petId);
        return "redirect:/profile";
    }

    @GetMapping("/lostPets/seenPet/{petId}")
    public String getSeenPetPage(@PathVariable Long petId, Model model){
        LostPet seenPet = this.lostPetService.findById(petId);
        model.addAttribute("seenPet",seenPet);
        return "inform";
    }

    @PostMapping("/lostPets/seenPet/{petId}")
    public String seenPet(@PathVariable Long petId,
                          @RequestParam String location,
                          @RequestParam String coordinates,
                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        String address = String.join("+",coordinates.split(" "));
        String locationUrl = "https://nominatim.openstreetmap.org/search.php?q=" + address + "&format=jsonv2";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = restTemplate.getForEntity(locationUrl, String.class);
        Gson gson = new Gson();
        JsonParser jsonParser = new JsonParser();
        JsonArray jsonArray = (JsonArray) jsonParser.parse(Objects.requireNonNull(result.getBody()));
        JsonObject jsonObject = (JsonObject) jsonArray.get(0);
        JsonElement jsonElement1 = jsonObject.get("lat");
        Float coordinate1 = jsonElement1.getAsFloat();
        JsonElement jsonElement2 = jsonObject.get("lon");
        Float coordinate2 = jsonElement2.getAsFloat();

        LocalDateTime seenTime = LocalDateTime.now();

        this.seenPetService.inform(petId,location,coordinate1,coordinate2,coordinates,date, seenTime);

        return "redirect:/lostPets";
    }

}
