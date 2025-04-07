package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "userList";
    }

    @GetMapping("/userForm")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "userForm";
    }

    @PostMapping("/userForm")
    public String saveUser(@ModelAttribute("user") User user, @RequestParam(value = "roles", required = false) List<String> roles) {
        if (roles != null) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roles) {
                roleSet.add(roleService.getRoleByName(roleName));
            }
            user.setRoles(roleSet);
        }
        userService.addUser(user);
        return "redirect:/admin";
    }
    @GetMapping()
    public String adminPage() {
        return "admin";
    }
    @GetMapping("/adminPage")
    public String getAdminProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "adminPage";
    }

}
