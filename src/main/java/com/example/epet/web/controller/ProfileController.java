package com.example.epet.web.controller;

import com.example.epet.model.entities.PetOwner;
import com.example.epet.service.PetOwnerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class ProfileController {

    private final PetOwnerService petOwnerService;

    public ProfileController(PetOwnerService petOwnerService) {
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @PostMapping("/login")
    public String LoginPage(){
        return "redirect:/lostPets";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }

    @GetMapping("/addOwner")
    public String addOwner(){
        return "addOwner";
    }

    @PostMapping("/addOwner")
    public String createOwner(@RequestParam String name,
                              @RequestParam String surname,
                              @RequestParam String username,
                              @RequestParam String password,
                              HttpSession session){
        PetOwner owner = this.petOwnerService.create(username,password,name,surname);
        session.setAttribute("user", owner);
        return "redirect:/lostPets";
    }

}
