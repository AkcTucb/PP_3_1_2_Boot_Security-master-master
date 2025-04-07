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

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "userList";
    }

    @PostMapping("/userForm")
    public String saveUser(
            @ModelAttribute("user") User user,
            @RequestParam(value = "roles", required = false) List<String> roleNames
    ) {
        if (roleNames != null && !roleNames.isEmpty()) {
            Set<Role> roleSet = new HashSet<>();
            for (String roleName : roleNames) {
                Role found = roleService.getRoleByName(roleName);
                if (found != null) {
                    roleSet.add(found);
                }
            }
            user.setRoles(roleSet);
        }
        if (user.getId() == null) {
            adminService.addUser(user);
        } else {
            adminService.update(user);
        }

        return "redirect:/admin/list";
    }

    @GetMapping("/userForm")
    public String createOrEditUserForm(
            @RequestParam(value = "id", required = false) Long id,
            Model model
    ) {
        if (id == null) {
            model.addAttribute("user", new User());
        } else {
            User existingUser = adminService.findById(id);
            model.addAttribute("user", existingUser);
        }
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "userForm";
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

    @GetMapping("/addForm")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "userform";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") User user) {
        adminService.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/list";
    }

    @PostMapping("/edit")
    public String updateUser(@ModelAttribute("user") User user) {
        System.out.println("ID пользователя: " + user.getId());
        adminService.update(user);
        return "redirect:/users";
    }

}
