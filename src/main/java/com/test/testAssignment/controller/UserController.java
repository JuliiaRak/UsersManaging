package com.test.testAssignment.controller;
import com.test.testAssignment.model.User;
import com.test.testAssignment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
@ConfigurationProperties(prefix = "age")
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${age.limit}")
    private long userAgeLimit = 18;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok().body(allUsers);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        Date currentDate = new Date();
        long ageInMillis = currentDate.getTime() - user.getBirthDate().getTime();
        long ageInYears = ageInMillis / (1000L * 60 * 60 * 24 * 365);

        if (ageInYears < userAgeLimit) {
            return ResponseEntity.badRequest().body("User must be at least " + userAgeLimit + " years old.");
        }

        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserFields(
            @PathVariable Long userId,
            @RequestBody User updatedUser) {
        User updatedUserData = userService.updateUserById(userId, updatedUser);
        return ResponseEntity.ok().body(updatedUserData);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUserFields(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updateFields) {
        User updatedUser = userService.updateUserFields(userId, updateFields);
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        if (from.after(to)) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        List<User> usersInDateRange = userService.getUsersByBirthDateRange(from, to);
        return ResponseEntity.ok().body(usersInDateRange);
    }
}
