package com.test.testAssignment.model;

import com.test.testAssignment.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testValidUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testInvalidEmail() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testMissingFirstName() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testBirthDateInFuture() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date(System.currentTimeMillis() + 3600000)); // Дата у майбутньому
        user.setPhoneNumber("1234567890");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidPhoneNumber() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("invalid-phone-number");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
    }
}
