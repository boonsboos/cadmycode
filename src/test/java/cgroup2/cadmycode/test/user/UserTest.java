package cgroup2.cadmycode.test.user;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import cgroup2.cadmycode.user.User;

public class UserTest {
    @Test
    void testCheckIsValidPostCode() {
        String postCode = "1234AB";
        assertTrue(User.validatePostcode(postCode));
    }

    @Test
    void testCheckIsInvalidPostCode() {
        String postCode = "1234";
        assertFalse(User.validatePostcode(postCode));
    }
}
