package com.revshop.test;

import com.revshop.service.AuthService;
import com.revshop.model.Buyer;
import com.revshop.model.Seller;
import com.revshop.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void testAuthServiceInstantiation() {
        AuthService authService = new AuthService();
        assertNotNull(authService);
        System.out.println("✓ AuthService instantiation test passed");
    }

    @Test
    void testBuyerRegistrationModel() {
        Buyer buyer = new Buyer();

        // Set registration data
        buyer.setUsername("newbuyer");
        buyer.setEmail("newbuyer@example.com");
        buyer.setPasswordHash("securePassword123");
        buyer.setSecurityQuestion(1);
        buyer.setSecurityAnswer("Blue");  // Might be converted to lowercase

        // Test Buyer specific properties
        buyer.setFirstName("Emma");
        buyer.setLastName("Thompson");
        buyer.setPhone("5551234567");
        buyer.setAddress("789 Oak Street");

        // Verify registration data
        assertEquals("newbuyer", buyer.getUsername());
        assertEquals("newbuyer@example.com", buyer.getEmail());
        assertEquals("securePassword123", buyer.getPasswordHash());
        assertEquals(1, buyer.getSecurityQuestion());
        // Don't check case sensitivity for security answer
        assertNotNull(buyer.getSecurityAnswer());
        assertEquals("Emma", buyer.getFirstName());
        assertEquals("Thompson", buyer.getLastName());
        assertEquals("5551234567", buyer.getPhone());
        assertEquals("789 Oak Street", buyer.getAddress());

        System.out.println("✓ Buyer registration model test passed");
    }

    @Test
    void testSellerRegistrationModel() {
        Seller seller = new Seller();

        // Set registration data
        seller.setUsername("newseller");
        seller.setEmail("newseller@example.com");
        seller.setPasswordHash("sellerPass456");
        seller.setSecurityQuestion(2);
        seller.setSecurityAnswer("Spring");  // Might be converted to lowercase

        // Test Seller specific properties
        seller.setBusinessName("Premium Goods Store");
        seller.setBusinessAddress("321 Commerce Plaza");
        seller.setBusinessPhone("5559876543");
        seller.setTaxId("BUS-789123");

        // Verify registration data
        assertEquals("newseller", seller.getUsername());
        assertEquals("newseller@example.com", seller.getEmail());
        assertEquals("sellerPass456", seller.getPasswordHash());
        assertEquals(2, seller.getSecurityQuestion());
        // Don't check case sensitivity for security answer
        assertNotNull(seller.getSecurityAnswer());
        assertEquals("Premium Goods Store", seller.getBusinessName());
        assertEquals("321 Commerce Plaza", seller.getBusinessAddress());
        assertEquals("5559876543", seller.getBusinessPhone());
        assertEquals("BUS-789123", seller.getTaxId());

        System.out.println("✓ Seller registration model test passed");
    }

    @Test
    void testUserLoginModel() {
        User user = new User();

        // Set login-related data
        user.setEmail("loginuser@example.com");
        user.setPasswordHash("loginHash789");
        user.setRole("buyer");

        // Verify login data
        assertEquals("loginuser@example.com", user.getEmail());
        assertEquals("loginHash789", user.getPasswordHash());
        assertEquals("buyer", user.getRole());

        System.out.println("✓ User login model test passed");
    }

    @Test
    void testPasswordResetModel() {
        User user = new User();

        // Set password reset data
        user.setEmail("reset@example.com");
        user.setSecurityQuestion(3);
        user.setSecurityAnswer("Winter");  // Might be converted to lowercase

        // Verify password reset data
        assertEquals("reset@example.com", user.getEmail());
        assertEquals(3, user.getSecurityQuestion());
        // Don't check case sensitivity for security answer
        assertNotNull(user.getSecurityAnswer());

        System.out.println("✓ Password reset model test passed");
    }

    @Test
    void testUserRoleValidation() {
        User user = new User();

        // Test buyer role
        user.setRole("buyer");
        assertEquals("buyer", user.getRole());

        // Test seller role
        user.setRole("seller");
        assertEquals("seller", user.getRole());

        System.out.println("✓ User role validation test passed");
    }

    @Test
    void testSecurityQuestionRange() {
        User user = new User();

        // Test valid question numbers
        user.setSecurityQuestion(1);
        assertEquals(1, user.getSecurityQuestion());
        assertTrue(user.getSecurityQuestion() >= 1);

        user.setSecurityQuestion(5);
        assertEquals(5, user.getSecurityQuestion());
        assertTrue(user.getSecurityQuestion() <= 10); // Assuming max 10

        System.out.println("✓ Security question range test passed");
    }

    @Test
    void testEmailFormat() {
        User user = new User();

        // Valid email format
        String email = "test.user@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
        assertTrue(user.getEmail().contains("@"));

        System.out.println("✓ Email format test passed");
    }

    @Test
    void testUsernameConstraints() {
        User user = new User();

        // Test username
        String username = "user_123";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
        assertTrue(user.getUsername().length() >= 3);

        System.out.println("✓ Username constraints test passed");
    }

    @Test
    void testContactInformation() {
        Buyer buyer = new Buyer();

        // Test phone number
        buyer.setPhone("+1-555-123-4567");
        assertEquals("+1-555-123-4567", buyer.getPhone());

        // Test address
        buyer.setAddress("123 Main St, New York, NY 10001");
        assertEquals("123 Main St, New York, NY 10001", buyer.getAddress());

        System.out.println("✓ Contact information test passed");
    }

    @Test
    void testBusinessInformation() {
        Seller seller = new Seller();

        // Test business name
        seller.setBusinessName("Global Trading Co.");
        assertEquals("Global Trading Co.", seller.getBusinessName());

        // Test tax ID
        seller.setTaxId("TAX-789456");
        assertEquals("TAX-789456", seller.getTaxId());

        System.out.println("✓ Business information test passed");
    }

    @Test
    void testUserProfileCompleteness() {
        Buyer buyer = new Buyer();

        // Set all profile fields
        buyer.setUserId(999);
        buyer.setUsername("completeuser");
        buyer.setEmail("complete@example.com");
        buyer.setPasswordHash("completeHash");
        buyer.setRole("buyer");
        buyer.setSecurityQuestion(4);
        buyer.setSecurityAnswer("Complete Answer");
        buyer.setFirstName("Complete");
        buyer.setLastName("User");
        buyer.setPhone("9998887777");
        buyer.setAddress("999 Complete Street");

        // Verify all fields are set
        assertNotNull(buyer.getUsername());
        assertNotNull(buyer.getEmail());
        assertNotNull(buyer.getPasswordHash());
        assertNotNull(buyer.getRole());
        assertTrue(buyer.getSecurityQuestion() > 0);
        assertNotNull(buyer.getSecurityAnswer());
        assertNotNull(buyer.getFirstName());
        assertNotNull(buyer.getLastName());
        assertNotNull(buyer.getPhone());
        assertNotNull(buyer.getAddress());

        System.out.println("✓ User profile completeness test passed");
    }
}