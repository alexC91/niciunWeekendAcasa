package com.linkDatabase;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_name")
    private String lastName;

    @Column(name="first_name")
    private String firstName;

    @Column(name="email")
    private String email;

    @Column(name="pass")
    private String password;

    @Column(name="is_activated")
    private byte isActivated;

    @Column(name="is_disabled")
    private byte isDisabled;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public byte getIsActivated() {
        return isActivated;
    }
    public void setIsActivated(byte isActivated) {
        this.isActivated = isActivated;
    }

    public byte getIsDisabled() {
        return isDisabled;
    }
    public void setIsDisabled(byte isDisabled) {
        this.isDisabled = isDisabled;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    }

