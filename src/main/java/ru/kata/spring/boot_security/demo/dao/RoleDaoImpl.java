package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> getAllRoles() {
        return em.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public Role getRoleByName(String roleName) {
        return em.createQuery("SELECT r FROM Role r WHERE r.name = :name", Role.class)
                .setParameter("name", roleName)
                .getSingleResult();
    }

    @Override
    public void saveRole(Role role) {
        if (role.getId() == null) {
            em.persist(role);
        } else {
            em.merge(role);
        }
    }

    @Override
    public List<Role> findByNameIn(List<String> names) {
        TypedQuery<Role> query = em.createQuery("SELECT r FROM Role r WHERE r.name IN :names", Role.class);
        query.setParameter("names", names);
        return query.getResultList();
    }
}
