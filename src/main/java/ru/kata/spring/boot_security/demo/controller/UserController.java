package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        return "userform";
    }

    @GetMapping("/user")
    public String userPage(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User user, Principal principal) {
        User existingUser = userService.findByEmail(principal.getName());
        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }
        userService.update(existingUser);
        return "redirect:/user";
    }
    @PostMapping("/delete")
    public String deleteProfile(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        userService.deleteUser(user.getId());
        return "redirect:/logout";
    }

}

