package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

public interface UserService {
    User findById(Long id);
    User findByEmail(String email);

    User getUser(Long id); // если реально нужен
    void update(User user);
    void deleteUser(Long id);
    void saveUser(User user);
}
