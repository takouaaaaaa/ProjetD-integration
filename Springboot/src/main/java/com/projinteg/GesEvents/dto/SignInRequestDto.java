package com.projinteg.GesEvents.dto;

public class SignInRequestDto {
    private String username;  // This field will be used for Admin's username and Company’s email
    private String password;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
