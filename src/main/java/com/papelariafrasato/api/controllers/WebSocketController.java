package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.ChatDto;
import com.papelariafrasato.api.services.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private WebSocketService webSocketService;

    @MessageMapping("/user/chat")
    public void handleUserMessage(ChatDto chatDto){
        webSocketService.saveMessage(chatDto);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.chatId(), chatDto);
        simpMessagingTemplate.convertAndSend("/topic/admin/chats", chatDto);
    }

    @MessageMapping("/admin/chat")
    public void handleAdminMessage(ChatDto chatDto){
        webSocketService.saveMessage(chatDto);
        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatDto.chatId(), chatDto);
    }

    @MessageMapping("/admin/close")
    public void closeChat(String chatId){
        webSocketService.closeChat(chatId);

        ChatDto closeNotification = new ChatDto(
                chatId, null, "Chat encerrado", "SYSTEM", false, LocalDateTime.now().toString()
        );

        simpMessagingTemplate.convertAndSend("/topic/chat/" + chatId, closeNotification);
        simpMessagingTemplate.convertAndSend("/topic/admin/chats", closeNotification);
    }
}
