package com.revshop.model;

public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String role; // "buyer", "seller", or "admin"
    private int securityQuestion;     // 1, 2, or 3
    private String securityAnswer;    // Answer to security question

    public User() {}

    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(int securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer != null ? securityAnswer.toLowerCase() : null;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username +
                ", email=" + email + ", role=" + role + "]";
    }
}