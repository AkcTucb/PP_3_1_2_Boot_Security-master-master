package ru.kata.spring.boot_security.demo.DTO;

import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password;
    private List<String> roleNames; // список названий ролей

    // Пустой конструктор нужен для THymeleaf/сериализации
    public UserDTO() {
    }

    // Конструктор "из сущности"
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    // Геттеры/сеттеры всех полей

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<String> getRoleNames() {
        return roleNames;
    }
    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
}

