package com.example.ChatMate.service;

import com.example.ChatMate.model.Channel;
import com.example.ChatMate.model.Message;
import com.example.ChatMate.model.User;
import com.example.ChatMate.repository.ChannelRepository;
import com.example.ChatMate.repository.MessageRepository;
import com.example.ChatMate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    // Изпращане на съобщение на потребител
    public Message sendMessageToUser(String senderUsername, String receiverUsername, String content) {
        User sender = userRepository.findByUsername(senderUsername);
        User receiver = userRepository.findByUsername(receiverUsername);

        if (sender == null) {
            throw new IllegalArgumentException("Потребителят с име " + senderUsername + " не съществува.");
        }

        if (receiver == null) {
            throw new IllegalArgumentException("Потребителят с име " + receiverUsername + " не съществува.");
        }

        Message message = new Message();
        message.setContent(content);
        message.setUser(sender); // задаване на изпращача
        // Предполага се, че съобщението е към конкретен потребител (не към канал)
        message.setReceiver(receiver); // задаване на получателя на съобщението
        return messageRepository.save(message);
    }

    // Изпращане на съобщение в канал
    public Message sendMessageToChannel(String senderUsername, String channelName, String content) {
        User sender = userRepository.findByUsername(senderUsername);
        Channel channel = channelRepository.findByName(channelName);

        if (sender == null) {
            throw new IllegalArgumentException("Потребителят с име " + senderUsername + " не съществува.");
        }

        if (channel == null) {
            throw new IllegalArgumentException("Канал с име " + channelName + " не съществува.");
        }

        // Проверка дали потребителят е член на канала
        if (!channel.getUsers().contains(sender)) {
            throw new IllegalArgumentException("Потребителят " + senderUsername + " не е член на канала.");
        }

        Message message = new Message();
        message.setContent(content);
        message.setUser(sender); // задаване на изпращача
        message.setChannel(channel); // задаване на канала
        return messageRepository.save(message);
    }

    // Четене на съобщения от канал
    public List<Message> getMessagesFromChannel(String channelName) {
        Channel channel = channelRepository.findByName(channelName);

        if (channel == null) {
            throw new IllegalArgumentException("Канал с име " + channelName + " не съществува.");
        }

        return messageRepository.findByChannel(channel);
    }

    // Четене на съобщения между двама потребители
    public List<Message> getMessagesBetweenUsers(String username1, String username2) {
        User user1 = userRepository.findByUsername(username1);
        User user2 = userRepository.findByUsername(username2);

        if (user1 == null || user2 == null) {
            throw new IllegalArgumentException("Един от потребителите не съществува.");
        }

        return messageRepository.findByUserAndReceiver(user1, user2);
    }
}
