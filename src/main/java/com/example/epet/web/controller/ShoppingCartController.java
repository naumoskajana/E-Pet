package com.example.epet.web.controller;

import com.example.epet.model.entities.PetOwner;
import com.example.epet.model.entities.ShoppingCart;
import com.example.epet.service.PetOwnerService;
import com.example.epet.service.ShoppingCartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final PetOwnerService petOwnerService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, PetOwnerService petOwnerService) {
        this.shoppingCartService = shoppingCartService;
        this.petOwnerService = petOwnerService;
    }

    @GetMapping("/shopping-cart")
    public String getShoppingCartPage(HttpSession session, Model model){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        if (petOwner == null){
            return "redirect:/login";
        }
        String username = petOwner.getUsername();
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        Double price = this.shoppingCartService.price(shoppingCart);
        model.addAttribute("products", this.shoppingCartService.listAllProductsInShoppingCart(shoppingCart.getId()));
        int messages = this.petOwnerService.loadMessageCount(petOwner.getId());
        model.addAttribute("messages",messages);
        model.addAttribute("loggedIn",true);
        model.addAttribute("price",price);
        model.addAttribute("shoppingCart",shoppingCart);
        return "shopping-cart";
    }

    @PostMapping("/shopping-cart/add-product/{id}")
    public String addProductToShoppingCart(@PathVariable Long id, HttpSession session){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        String username = petOwner.getUsername();
        this.shoppingCartService.addProductToShoppingCart(username,id);
        return "redirect:/products";
    }

    @GetMapping("/shopping-cart/checkout/{id}")
    public String checkout(@PathVariable Long id){
        this.shoppingCartService.checkoutShoppingCart(id);
        return "products";
    }

    @GetMapping("/shopping-cart/delete-product/{id}")
    public String removeProductFromShoppingCart(@PathVariable Long id, HttpSession session){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        String username = petOwner.getUsername();
        this.shoppingCartService.removeProductFromShoppingCart(username,id);
        return "redirect:/shopping-cart";
    }
}
