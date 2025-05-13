package com.linkDatabase;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "app_users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(name = "pass")
    private String password;
    private boolean isActivated;
    private boolean isDisabled;
    private Date createdAt;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean getIsActivated() { return isActivated; }
    public void setIsActivated(boolean isActivated) { this.isActivated = isActivated; }
    public boolean getIsDisabled() { return isDisabled; }
    public void setIsDisabled(boolean isDisabled) { this.isDisabled = isDisabled; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
