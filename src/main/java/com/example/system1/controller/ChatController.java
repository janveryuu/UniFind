package com.example.system1.controller;

import com.example.system1.model.Message;
import com.example.system1.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;

@Controller
public class ChatController {

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message chatMessage) {
        chatMessage.setTimestamp(LocalDateTime.now());
        Message savedMessage = messageRepository.save(chatMessage);
        return savedMessage;
    }

    @MessageMapping("/chat.replyMessage")
    @SendTo("/topic/public")
    public Message replyMessage(@Payload Message chatMessage) {
        Message existingMessage = messageRepository.findById(chatMessage.getId()).orElse(null);
        if (existingMessage != null) {
            existingMessage.setAdminReply(chatMessage.getAdminReply());
            Message savedMessage = messageRepository.save(existingMessage);
            return savedMessage;
        }
        return chatMessage;
    }
}
