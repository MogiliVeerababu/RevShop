package com.revshop.model;

public class Buyer extends User {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    public Buyer() {}

    public Buyer(String username, String email, String passwordHash) {
        super(username, email, passwordHash, "buyer");
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Inherited getters/setters from User
    public int getUserId() {
        return super.getUserId();
    }

    public void setUserId(int userId) {
        super.setUserId(userId);
    }

    public String getUsername() {
        return super.getUsername();
    }

    public void setUsername(String username) {
        super.setUsername(username);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    public void setPasswordHash(String passwordHash) {
        super.setPasswordHash(passwordHash);
    }

    public String getRole() {
        return super.getRole();
    }

    public void setRole(String role) {
        super.setRole(role);
    }

    @Override
    public String toString() {
        return "Buyer [userId=" + getUserId() + ", username=" + getUsername() +
                ", email=" + getEmail() + ", firstName=" + firstName +
                ", lastName=" + lastName + ", phone=" + phone + "]";
    }
}