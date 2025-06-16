package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.ChatDto;
import com.papelariafrasato.api.models.Chat;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WebSocketService {

    private final Map<String, Chat> activeChats = new ConcurrentHashMap<>();
    private final Map<String, List<ChatDto>> chatMessages = new ConcurrentHashMap<>();

    public String createChat(String userId) {
        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, userId, true);
        activeChats.put(chatId, chat);
        chatMessages.put(chatId, new ArrayList<>());
        return chatId;
    }

    public void closeChat(String chatId) {
        Chat chat = activeChats.get(chatId);
        if (chat != null) {
            chat.setActive(false);
        }
    }

    public List<Chat> getActiveChats() {
        return activeChats.values().stream()
                .filter(Chat::isActive)
                .collect(Collectors.toList());
    }

    public void saveMessage(ChatDto chatDto){
        chatMessages.computeIfAbsent(chatDto.chatId(), k -> new ArrayList<>()).add(chatDto);
    }

    public List<ChatDto> getChatHistory(String chatId){
        return chatMessages.getOrDefault(chatId, new ArrayList<>());
    }
}
