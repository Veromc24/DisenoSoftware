package edu.uclm.esi.circuits.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.circuits.model.Circuit;

@Repository
public interface CircuitDao extends JpaRepository<Circuit, String> {
    // JpaRepository provides basic CRUD operations
    // You can define custom query methods here if needed

}