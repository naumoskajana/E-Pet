package com.example.epet.web.controller;

import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.Product;
import com.example.epet.service.PetOwnerService;
import com.example.epet.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final PetOwnerService petOwnerService;

    public ProductController(ProductService productService, PetOwnerService petOwnerService) {
        this.productService = productService;
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/products")
    public String getProductsPage(Model model, HttpSession session){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        if (petOwner == null){
            return "redirect:/login";

        }
        int messages = this.petOwnerService.loadMessageCount(petOwner.getId());
        model.addAttribute("messages",messages);
        model.addAttribute("loggedIn",true);
        List<Product> products = this.productService.findAll();
        model.addAttribute("products",products);
        return "products";
    }

}
