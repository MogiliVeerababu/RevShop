package com.revshop.model;

public class Seller extends User {
    private String businessName;
    private String businessAddress;
    private String businessPhone;
    private String taxId;

    public Seller() {}

    public Seller(String username, String email, String passwordHash) {
        super(username, email, passwordHash, "seller");
    }

    // Getters and Setters
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public String getBusinessAddress() { return businessAddress; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }

    public String getBusinessPhone() { return businessPhone; }
    public void setBusinessPhone(String businessPhone) { this.businessPhone = businessPhone; }

    public String getTaxId() { return taxId; }
    public void setTaxId(String taxId) { this.taxId = taxId; }

    @Override
    public String toString() {
        return "Seller [userId=" + getUserId() + ", username=" + getUsername() +
                ", email=" + getEmail() + ", businessName=" + businessName +
                ", businessPhone=" + businessPhone + "]";
    }
}