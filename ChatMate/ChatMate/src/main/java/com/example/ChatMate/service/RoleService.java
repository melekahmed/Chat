package com.example.ChatMate.service;


import com.example.ChatMate.model.Role;
import com.example.ChatMate.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Създаване на роля
    public Role createRole(String roleName) {
        Role existingRole = roleRepository.findByName(roleName);
        if (existingRole != null) {
            throw new IllegalArgumentException("Роля с това име вече съществува.");
        }
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

    // Намиране на роля по име
    public Role findRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}