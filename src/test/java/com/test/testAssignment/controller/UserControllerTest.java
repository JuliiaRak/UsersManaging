package com.test.testAssignment.controller;

import com.test.testAssignment.model.User;
import com.test.testAssignment.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    User user1 = new User(1L, "user1@example.com", "John", "Doe", new Date(2323223232L), "Address 1", "1234567890");
    User user2 = new User(2L, "user2@example.com", "Jane", "Smith", new Date(2323223232L), "Address 2", "9876543210");
    List<User> userList = Arrays.asList(user1, user2);

    @Value("${age.limit}")
    private int userAgeLimit = 18;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);
        return calendar.getTime();
    }

    @Test
    void testGetAllUsers() {
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        verify(userService, times(1)).getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userList, response.getBody());
    }

    @Test
    void testCreateUser_ValidUser() {
        when(userService.createUser(user1)).thenReturn(user1);

        ResponseEntity<?> response = userController.createUser(user1);

        verify(userService, times(1)).createUser(user1);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user1, response.getBody());
    }

    @Test
    void testCreateUser_UnderageUser() {
        User underageUser = new User(1L, "user1@example.com", "John", "Doe", createDate(2015, 6,5), "Address 1", "1234567890");

        ResponseEntity<?> response = userController.createUser(underageUser);

        verify(userService, never()).createUser(underageUser);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("User must be at least " + userAgeLimit + " years old."));
    }

    @Test
    void testDeleteUser() {
        User user = new User(5L, "user1@example.com", "John", "Doe", new Date(), "Address 1", "1234567890");

        ResponseEntity<?> response = userController.deleteUser(user.getId());

        verify(userService, times(1)).deleteUserById(user.getId());
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testSearchUsersByBirthDateRange() {
        Date fromDate = createDate(1900, 1, 1);
        Date toDate = createDate(2023, 1, 1);

        List<User> users = Arrays.asList(user1, user2);

        when(userService.getUsersByBirthDateRange(fromDate, toDate)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsersByBirthDateRange(fromDate, toDate);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testUpdateUserFields() {
        User updatedUser = new User(1L, "user1@example.com", "UpdateName", "UpdateLastName", new Date(), "Address 1", "1234567890");

        when(userService.updateUserFields(eq(user1.getId()), any())).thenReturn(updatedUser);
        when(userService.getUserById(user1.getId())).thenReturn(Optional.of(user1));

        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("firstName", "UpdateName");
        updateFields.put("lastName", "UpdateLastName");
        updateFields.put("birthDate", new Date());

        ResponseEntity<?> response = userController.updateUserFields(user1.getId(), updateFields);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());
    }

}
