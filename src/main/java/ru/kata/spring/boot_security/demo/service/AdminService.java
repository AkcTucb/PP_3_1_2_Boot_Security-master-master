package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface AdminService {
    List<User> getAllUsers();
    void addUser(User user);
    void deleteUser(Long id);
    void update(User user);

    User findById(Long id);
}
