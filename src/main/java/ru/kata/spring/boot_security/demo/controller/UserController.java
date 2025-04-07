package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.DTO.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // Показ профиля текущего пользователя
    @GetMapping
    public String userProfile(Model model, Principal principal) {
        // Ищем пользователя по email (Principal#getName() == user.getEmail())
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);
        return "user";  // user.html
    }

    @GetMapping("/edit")
    public String showEditForm(Principal principal, Model model) {
        User user = userService.findByEmail(principal.getName());
        UserDTO dto = new UserDTO(user);

        model.addAttribute("userDTO", dto);       // <-- имя userDTO
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "userform"; // userForm.html
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<String> updateProfile(
            @RequestParam("id") Long userId,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String rawPassword // nullable
    ) {
        // 1) Найти пользователя
        User existingUser = userService.findById(userId);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден по id=" + userId);
        }

        // 2) Обновить поля
        existingUser.setName(name);
        existingUser.setEmail(email);

        if (rawPassword != null && !rawPassword.isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(rawPassword));
        }

        // 3) Сохранить/обновить
        userService.update(existingUser);

        return ResponseEntity.ok("Профиль успешно обновлен");
    }


    // Удалить профиль целиком
    @PostMapping("/delete")
    public String deleteProfile(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        userService.deleteUser(user.getId());
        return "redirect:/logout";
    }
}
