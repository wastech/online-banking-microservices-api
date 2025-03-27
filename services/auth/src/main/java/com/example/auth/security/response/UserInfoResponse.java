package com.example.auth.security.response;

import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String jwtToken;
    private String username;
    private String email;  // Added email field
    private List<String> roles;

    // Constructor with email, jwtToken
    public UserInfoResponse(Long id, String email, String username, List<String> roles, String jwtToken) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.jwtToken = jwtToken;
    }

    // Constructor with email, without jwtToken
    public UserInfoResponse(Long id, String email, String username, List<String> roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

    // Existing getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // New getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}