package com.example.ChatMate.controller;

import com.example.ChatMate.model.User;
import com.example.ChatMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Получаване на всички потребители
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    // Получаване на потребител по ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Създаване на нов потребител
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        // Проверка дали потребител с такова потребителско име или имейл вече съществува
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body(null); // Потребител с това име вече съществува
        }
        if (userService.findByEmail(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body(null); // Потребител с този имейл вече съществува
        }

        User createdUser = userService.save(user);
        return ResponseEntity.ok(createdUser);  // Връщаме създадения потребител
    }

    // Актуализиране на потребител
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        User user = userService.findById(id);
        if (user != null) {
            // Може да се добавят проверки и за други полета, например:
            // user.setUsername(userDetails.getUsername());
            // user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());  // Пример за актуализиране на парола
            User updatedUser = userService.save(user);  // Записваме актуализирания потребител
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();  // Ако потребителят не съществува
    }

    // Изтриване на потребител
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user != null) {
            userService.deleteById(id);  // Изтриване на потребителя
            return ResponseEntity.noContent().build();  // Успешно изтриване
        }
        return ResponseEntity.notFound().build();  // Ако потребителят не съществува
    }

    // Търсене на потребител по потребителско име
    @GetMapping("/search")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // Търсене на потребител по имейл
    @GetMapping("/search/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = userService.findByEmail(email);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }
}