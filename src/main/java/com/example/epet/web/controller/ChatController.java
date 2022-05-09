package com.example.epet.web.controller;

import com.example.epet.config.webSocket.ChatMessage;
import com.example.epet.model.entities.PetOwner;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class ChatController {
    @GetMapping("/")
    public String getChat(Model model, HttpSession session){
        PetOwner petOwner = (PetOwner) session.getAttribute("user");
        if (petOwner == null){
            return "redirect:/login";
        }
        String name = petOwner.getName();
        model.addAttribute("user", name);
        return "index";
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor, HttpSession session) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}
