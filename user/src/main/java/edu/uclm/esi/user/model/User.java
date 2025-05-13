package edu.uclm.esi.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios")
public class User {
    @Id
    private String email;

    @Column(name = "Username", nullable = false) // Map to the correct column name
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private int credit;

    public User(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.credit= 0; // Default credit value
    }

    public User() {
        // Default constructor required by JPA
    }

    public String getName() {
        return name;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
