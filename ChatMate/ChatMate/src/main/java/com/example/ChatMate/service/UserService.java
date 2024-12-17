package com.example.ChatMate.service;

import com.example.ChatMate.model.Role;
import com.example.ChatMate.model.User;
import com.example.ChatMate.repository.RoleRepository;
import com.example.ChatMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Създаване на нов потребител
    public User createUser(String username, String password, String email) {
        // Проверки дали потребителят вече съществува
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("Потребител с това име вече съществува.");
        }

        if (userRepository.findByEmail(email) != null) {
            throw new IllegalArgumentException("Потребител с този имейл вече съществува.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // Препоръчително е да хеширате паролите
        user.setEmail(email);

        return userRepository.save(user);
    }

    // Намери потребител по име
    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Потребителят с това име не съществува.");
        }
        return user;
    }

    // Добавяне на роля към потребител
    public User addRoleToUser(String username, String roleName) {
        User user = findUserByUsername(username);
        Role role = roleRepository.findByName(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Роля с това име не съществува.");
        }

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.add(role);  // Добавяне на новата роля към потребителя
        user.setRoles(roles);

        return userRepository.save(user); // Записваме потребителя с новата роля
    }

    // Изтриване на роля от потребител
    public User removeRoleFromUser(String username, String roleName) {
        User user = findUserByUsername(username);
        Role role = roleRepository.findByName(roleName);

        if (role == null) {
            throw new IllegalArgumentException("Роля с това име не съществува.");
        }

        Set<Role> roles = new HashSet<>(user.getRoles());
        roles.remove(role); // Премахваме ролята
        user.setRoles(roles);

        return userRepository.save(user); // Записваме потребителя без тази роля
    }

    // Връща всички роли на потребител
    public Set<Role> getRolesForUser(String username) {
        User user = findUserByUsername(username);
        return user.getRoles();
    }
}