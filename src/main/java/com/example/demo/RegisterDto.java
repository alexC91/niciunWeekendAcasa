package com.example.demo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterDto {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;


    @Size(min = 6, message = "Minimum Password length is 6 characters")
    private String password;

    private String confirmPassword;

    public @NotEmpty String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotEmpty String firstName) {
        this.firstName = firstName;
    }

    public @NotEmpty String getLastName() {
        return lastName;
    }

    public void setLastName(@NotEmpty String lastName) {
        this.lastName = lastName;
    }

    public @NotEmpty @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotEmpty @Email String email) {
        this.email = email;
    }

    public @Size(min = 6, message = "Minimum Password length is 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6, message = "Minimum Password length is 6 characters") String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
