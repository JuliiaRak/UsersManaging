package com.test.testAssignment.service;

import com.test.testAssignment.exception.NotFoundException;
import com.test.testAssignment.model.User;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final List<User> users = new ArrayList<>();
    private long nextUserId = 1;

    public User createUser(User user) {
        user.setId(nextUserId++);
        users.add(user);
        return user;
    }

    public List<User> getAllUsers() {
        return users;
    }

    public Optional<User> getUserById(Long userId) {
        return users.stream().filter(user -> user.getId().equals(userId)).findFirst();
    }

    public User updateUserById(Long userId, User updatedUser) {
        Optional<User> optionalUser = getUserById(userId);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setBirthDate(updatedUser.getBirthDate());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setAddress(updatedUser.getAddress());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            return existingUser;
        } else {
            throw new NotFoundException("User not found with ID: " + userId);
        }
    }

    public User updateUserFields(Long userId, Map<String, Object> updateFields) {
        User user = getUserById(userId).orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        for (Map.Entry<String, Object> entry : updateFields.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            switch (fieldName) {
                case "email":
                    user.setEmail((String) fieldValue);
                case "firstName":
                    user.setFirstName((String) fieldValue);
                    break;
                case "lastName":
                    user.setLastName((String) fieldValue);
                    break;
                case "phoneNumber":
                    user.setPhoneNumber((String) fieldValue);
                    break;
                case "adress":
                    user.setAddress((String) fieldValue);
                    break;
            }
        }
        return user;
    }

    public void deleteUserById(Long userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }

    public List<User> getUsersByBirthDateRange(Date fromDate, Date toDate) {
        return users.stream().filter(user -> user.getBirthDate().after(fromDate) && user.getBirthDate().before(toDate)).toList();
    }
}

