package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AdminService;
import ru.kata.spring.boot_security.demo.service.RoleService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final RoleService roleService;
    private final AdminService adminService;

    @Autowired
    public AdminController(RoleService roleService, AdminService adminService) {
        this.roleService = roleService;
        this.adminService = adminService;
    }


    @GetMapping
    public String showAdminPage(Model model,
                                @AuthenticationPrincipal User authUser) {
        model.addAttribute("currentUser", authUser);
        model.addAttribute("users", adminService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "adminPage";
    }


    @PostMapping("/userForm")
    public String saveUser(
            @ModelAttribute("newUser") User user,
            @RequestParam(value = "roles", required = false) List<String> roleNames
    ) {
        if (roleNames != null) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roleNames) {
                Role found = roleService.getRoleByName(roleName);
                if (found != null) {
                    roleSet.add(found);
                }
            }
            user.setRoles(roleSet);
        }
        adminService.addUser(user);
        return "redirect:/admin";
    }
    @PostMapping("/update")
    public String updateUser(
            @ModelAttribute("newUser") User user,
            @RequestParam(value = "roles", required = false) List<String> roleNames
    ) {
        if (roleNames != null) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roleNames) {
                Role found = roleService.getRoleByName(roleName);
                if (found != null) {
                    roleSet.add(found);
                }
            }
            user.setRoles(roleSet);
        }
        adminService.update(user);
        return "redirect:/admin";
    }
    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin";
    }
}

