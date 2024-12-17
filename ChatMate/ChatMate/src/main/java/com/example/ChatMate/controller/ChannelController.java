package com.example.ChatMate.controller;

import com.example.ChatMate.model.Channel;
import com.example.ChatMate.model.User;
import com.example.ChatMate.service.ChannelService;
import com.example.ChatMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private UserService userService; // За да се добавят/премахват потребители и роли в канала

    // Получаване на всички канали
    @GetMapping
    public List<Channel> getAllChannels() {
        return channelService.findAll();
    }

    // Получаване на канал по ID
    @GetMapping("/{id}")
    public ResponseEntity<Channel> getChannelById(@PathVariable Long id) {
        Channel channel = channelService.findById(id);
        return channel != null ? ResponseEntity.ok(channel) : ResponseEntity.notFound().build();
    }

    // Създаване на нов канал
    @PostMapping
    public Channel createChannel(@RequestBody Channel channel) {
        // Възможно е да добавите проверка за уникалност на името на канала
        return channelService.save(channel);
    }

    // Обновяване на канал по ID
    @PutMapping("/{id}")
    public ResponseEntity<Channel> updateChannel(@PathVariable Long id, @RequestBody Channel channelDetails) {
        Channel channel = channelService.findById(id);
        if (channel != null) {
            // Може да актуализирате името на канала, ако е необходимо
            channel.setName(channelDetails.getName());  // Поставяне на новото име на канала
            return ResponseEntity.ok(channelService.save(channel));
        }
        return ResponseEntity.notFound().build();
    }

    // Изтриване на канал
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChannel(@PathVariable Long id) {
        Channel channel = channelService.findById(id);
        if (channel != null) {
            // Може да добавите проверка дали текущия потребител има право да изтрие канала
            channelService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Добавяне на потребител в канал
    @PostMapping("/{channelId}/addUser/{userId}")
    public ResponseEntity<Channel> addUserToChannel(@PathVariable Long channelId, @PathVariable Long userId) {
        Channel channel = channelService.findById(channelId);
        if (channel != null) {
            // Проверка дали потребителят съществува
            User user = userService.findUserByUsername(userId);
            if (user != null) {
                channel.getUsers().add(user);
                channelService.save(channel);
                return ResponseEntity.ok(channel);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Премахване на потребител от канал
    @DeleteMapping("/{channelId}/removeUser/{userId}")
    public ResponseEntity<Channel> removeUserFromChannel(@PathVariable Long channelId, @PathVariable Long userId) {
        Channel channel = channelService.findById(channelId);
        if (channel != null) {
            // Проверка дали потребителят е част от канала
            User user = userService.findUserByUsername(userId);
            if (user != null && channel.getUsers().contains(user)) {
                channel.getUsers().remove(user);
                channelService.save(channel);
                return ResponseEntity.ok(channel);
            }
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}