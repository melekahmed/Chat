package com.example.ChatMate.service;

import com.example.ChatMate.model.Channel;
import com.example.ChatMate.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    // Намиране на всички канали
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    // Намиране на канал по ID
    public Channel findById(Long id) {
        Optional<Channel> channel = channelRepository.findById(id);
        return channel.orElse(null);  // Ако не бъде намерен канал, връща null
    }

    // Създаване или обновяване на канал
    public Channel save(Channel channel) {
        return channelRepository.save(channel);  // Записва канала в базата данни
    }

    // Изтриване на канал по ID
    public void deleteById(Long id) {
        channelRepository.deleteById(id);  // Изтрива канала с дадено ID
    }

    // Намиране на канал по име
    public Channel findByName(String name) {
        return channelRepository.findByName(name);  // Извършва търсене по име
    }
}