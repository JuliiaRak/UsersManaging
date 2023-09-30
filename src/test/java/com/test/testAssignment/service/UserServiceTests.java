package com.test.testAssignment.service;

import com.test.testAssignment.exception.NotFoundException;
import com.test.testAssignment.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userService.getAllUsers().clear();
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getFirstName(), createdUser.getFirstName());
        assertEquals(user.getLastName(), createdUser.getLastName());
        assertEquals(user.getBirthDate(), createdUser.getBirthDate());
        assertEquals(user.getPhoneNumber(), createdUser.getPhoneNumber());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        User createdUser = userService.createUser(user);
        Long userId = createdUser.getId();

        Optional<User> retrievedUser = userService.getUserById(userId);

        assertTrue(retrievedUser.isPresent());
        assertEquals(userId, retrievedUser.get().getId());
    }

    @Test
    public void testUpdateUserById() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");

        User createdUser = userService.createUser(user);
        Long userId = createdUser.getId();

        User updatedUser = new User();
        updatedUser.setFirstName("Updated");
        updatedUser.setLastName("User");
        updatedUser.setBirthDate(new Date());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPhoneNumber("9876543210");

        User result = userService.updateUserById(userId, updatedUser);

        assertEquals(userId, result.getId());
        assertEquals(updatedUser.getFirstName(), result.getFirstName());
        assertEquals(updatedUser.getLastName(), result.getLastName());
        assertEquals(updatedUser.getBirthDate(), result.getBirthDate());
        assertEquals(updatedUser.getEmail(), result.getEmail());
        assertEquals(updatedUser.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    public void testUpdateUserByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            userService.updateUserById(999L, new User());
        });
    }

    @Test
    void testUpdateUserFields() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");
        User createdUser = userService.createUser(user);

        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("firstName", "UpdatedFirstName");
        updateFields.put("lastName", "UpdatedLastName");

        User updatedUser = userService.updateUserFields(createdUser.getId(), updateFields);

        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
        assertEquals("UpdatedLastName", updatedUser.getLastName());
    }

    @Test
    void testDeleteUserById() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setBirthDate(new Date());
        user.setPhoneNumber("1234567890");
        User createdUser = userService.createUser(user);

        userService.deleteUserById(createdUser.getId());

        assertTrue(userService.getAllUsers().isEmpty());
    }

    @Test
    void testGetUsersByBirthDateRange() {
        Date fromDate = createDate(2000, 1, 1);
        Date toDate = createDate(2015, 1, 1);

        User user1 = new User();
        user1.setEmail("user@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setBirthDate(createDate(2007, 6, 7));
        user1.setPhoneNumber("1234567890");
        userService.createUser(user1);

        User user2 = new User();
        user2.setEmail("user@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Smith");
        user2.setBirthDate(createDate(2010, 5 , 10));
        user2.setPhoneNumber("1234567890");
        userService.createUser(user2);

        List<User> result = userService.getUsersByBirthDateRange(fromDate, toDate);
        assertEquals(2, result.size());
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }
}
