package com.example.ChatMate.repository;


import com.example.ChatMate.model.Channel;
import com.example.ChatMate.model.Message;
import com.example.ChatMate.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Намери всички съобщения в конкретен канал
    List<Message> findByChannel(Channel channel);

    // Намери всички съобщения от конкретен потребител
    List<Message> findByUser(User user);

    // Намери всички съобщения в канал, изпратени от конкретен потребител
    List<Message> findByChannelAndUser(Channel channel, User user);

}