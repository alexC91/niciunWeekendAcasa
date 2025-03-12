package com.linkDatabase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name="app_users")
public class Users {
    @Column(name="ID", nullable=false, unique=true)
    private Integer id;

    @Column(name="last_name", nullable=false)
    private String lastName;

    @Column(name="first_name", nullable=false)
    private String firstName;

    @Column(name="email", nullable=false)
    private String email;

    @Column(name="pass", nullable=false)
    private String password;

    @Column(name="is_activated")
    private Byte isActivated;

    @Column(name="is_disabled")
    private Byte isDisabled;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column
    private String role;

    public Byte getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Byte isDisabled) {
        this.isDisabled = isDisabled;
    }

    public Byte getIsActivated() {
        return isActivated;
    }

    public void setIsActivated(Byte isActivated) {
        this.isActivated = isActivated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
