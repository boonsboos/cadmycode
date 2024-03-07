package cgroup2.cadmycode.test.content;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import cgroup2.cadmycode.content.Module;

public class ModuleTest {
    @Test
    void testCheckIsValidContactEmail() {
        String contactEmail = "test@example.com";
        assertTrue(Module.validateContactEmail(contactEmail));
    }

    @Test
    void testCheckIsInvalidContactEmail() {
        String contactEmail = "invalid_email";
        assertFalse(Module.validateContactEmail(contactEmail));
    }
}
