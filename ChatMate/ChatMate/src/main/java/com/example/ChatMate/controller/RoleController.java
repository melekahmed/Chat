package com.example.ChatMate.controller;

import com.example.ChatMate.model.Role;
import com.example.ChatMate.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Получаване на всички роли
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.findAll();
    }

    // Получаване на роля по ID
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return role != null ? ResponseEntity.ok(role) : ResponseEntity.notFound().build();
    }

    // Създаване на нова роля
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        // Проверка дали роля с такова име вече съществува
        if (roleService.findByName(role.getName()) != null) {
            return ResponseEntity.badRequest().build();  // Ако вече съществува роля с такова име
        }

        Role createdRole = roleService.save(role);
        return ResponseEntity.ok(createdRole);  // Връщаме новосъздадената роля
    }

    // Актуализиране на роля
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role role = roleService.findById(id);
        if (role != null) {
            role.setName(roleDetails.getName());  // Актуализиране на името на ролята
            Role updatedRole = roleService.save(role);  // Записваме актуализираната роля
            return ResponseEntity.ok(updatedRole);
        }
        return ResponseEntity.notFound().build();  // Ако роля не бъде намерена
    }

    // Изтриване на роля
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        Role role = roleService.findById(id);
        if (role != null) {
            roleService.deleteById(id);
            return ResponseEntity.noContent().build();  // Успешно изтриване
        }
        return ResponseEntity.notFound().build();  // Ако роля не бъде намерена
    }
}