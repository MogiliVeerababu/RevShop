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
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // ✅ SECURITY FIELDS GETTERS/SETTERS
    public int getSecurityQuestion() {
        return super.getSecurityQuestion();
    }
    public void setSecurityQuestion(int securityQuestion) {
        super.setSecurityQuestion(securityQuestion);
    }
    public String getSecurityAnswer() {
        return super.getSecurityAnswer();
    }
    public void setSecurityAnswer(String securityAnswer) {
        super.setSecurityAnswer(securityAnswer);
    }

    @Override
    public String toString() {
        return "Buyer [userId=" + getUserId() + ", username=" + getUsername() +
                ", email=" + getEmail() + ", firstName=" + firstName +
                ", lastName=" + lastName + ", phone=" + phone + "]";
    }
}