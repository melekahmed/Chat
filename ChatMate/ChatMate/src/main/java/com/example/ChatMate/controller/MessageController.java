package com.example.ChatMate.controller;

import com.example.ChatMate.model.Message;
import com.example.ChatMate.service.ChannelService;
import com.example.ChatMate.service.MessageService;
import com.example.ChatMate.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    // Получаване на всички съобщения с пагинация
    @GetMapping
    public List<Message> getAllMessages(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return messageService.findAllPaged(page, size);  // Пагинация на съобщенията
    }

    // Получаване на съобщение по ID
    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Long id) {
        Message message = messageService.findById(id);
        return message != null ? ResponseEntity.ok(message) : ResponseEntity.notFound().build();
    }

    // Създаване на ново съобщение
    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody @Valid Message message) {
        if (userService.findById(message.getUser().getId()) == null) {
            throw new ResourceNotFoundException("User not found with ID: " + message.getUser().getId());
        }

        if (channelService.findById(message.getChannel().getId()) == null) {
            throw new ResourceNotFoundException("Channel not found with ID: " + message.getChannel().getId());
        }

        Message createdMessage = messageService.save(message);
        return ResponseEntity.ok(createdMessage);
    }

    // Актуализиране на съобщение по ID
    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable Long id, @RequestBody Message messageDetails) {
        Message message = messageService.findById(id);
        if (message != null) {
            message.setContent(messageDetails.getContent());
            if (messageDetails.getUser() != null) {
                message.setUser(messageDetails.getUser());
            }
            if (messageDetails.getChannel() != null) {
                message.setChannel(messageDetails.getChannel());
            }
            Message updatedMessage = messageService.save(message);
            return ResponseEntity.ok(updatedMessage);
        }
        return ResponseEntity.notFound().build();
    }

    // Изтриване на съобщение по ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        if (messageService.findById(id) != null) {
            messageService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}