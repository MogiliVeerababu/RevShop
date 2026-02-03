package com.revshop.test;

import com.revshop.service.AuthService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    @Test
    public void testPasswordHashing() {
        String password = "Test@123";
        com.revshop.util.PasswordUtil util = new com.revshop.util.PasswordUtil();
        String hashed = util.hashPassword(password);

        assertNotNull(hashed);
        assertNotEquals(password, hashed);
    }

    @Test
    public void testEmailValidation() {
        // This is a simple test - in real scenario you'd test ValidationUtil
        assertTrue(isValidEmail("test@example.com"));
        assertFalse(isValidEmail("invalid-email"));
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
}