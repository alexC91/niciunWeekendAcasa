package com.example.demo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {

    @NotEmpty(message = "Prenumele este obligatoriu")
    private String firstName;

    @NotEmpty(message = "Numele este obligatoriu")
    private String lastName;

    @NotEmpty(message = "Email-ul este obligatoriu")
    @Email(message = "Formatul email-ului este invalid")
    private String email;

    @NotEmpty(message = "Parola este obligatorie")
    @Size(min = 6, message = "Parola trebuie să aibă minim 6 caractere")
    private String password;

    @NotEmpty(message = "Confirmarea parolei este obligatorie")
    private String confirmPassword;

    // Getters and Setters
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
