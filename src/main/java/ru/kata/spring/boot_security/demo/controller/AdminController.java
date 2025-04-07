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
import ru.kata.spring.boot_security.demo.service.UserService;

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

    @GetMapping("/admin")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
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
        adminService.addUser(user);
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

    @GetMapping("/user/add")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "userform";
    }

    @PostMapping("/user/add")
    public String addUser(@ModelAttribute("user") User user) {
        adminService.addUser(user);
        return "redirect:/users";
    }

    @DeleteMapping("/user/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        adminService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping("/user/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        System.out.println("ID пользователя: " + user.getId());
        adminService.update(user);
        return "redirect:/users";
    }

}
