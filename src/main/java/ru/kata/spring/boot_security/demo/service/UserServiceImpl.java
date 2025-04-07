package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    // если реально нужно, пусть возвращает getUserById
    @Override
    public User getUser(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public void update(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userDao.deleteUser(id);
    }

    @Override
    public void saveUser(User user) {
        // Если пароль ещё не кодирован — кодируем
        // (зависит от логики, можно проверять, matches(...) ?)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.saveUser(user);
    }
}
