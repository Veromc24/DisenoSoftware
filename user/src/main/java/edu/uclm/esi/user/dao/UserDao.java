package edu.uclm.esi.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.user.model.User; // Asegúrate de que la clase User esté en este paquete

@Repository
public interface UserDao extends JpaRepository<User, String> {
    // JpaRepository proporciona operaciones CRUD básicas

    User findByName(String name);
    boolean existsByName(String name);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
