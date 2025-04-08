package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void saveUser(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }

    @Override
    public void updateUser(User user) {
        em.merge(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        if (user != null) {
            em.remove(user);
        }
    }

    @Override
    public User getUserByName(String name) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            return em.createQuery(
                            "SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.email = :email", User.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            System.out.println("Ошибка: Найдено несколько пользователей с email: " + email);
            return null;
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return em.createQuery("SELECT u FROM User u WHERE u.id = :id", User.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst();
    }
}
