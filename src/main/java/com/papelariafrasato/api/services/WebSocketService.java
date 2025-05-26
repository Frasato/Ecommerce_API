package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Chat;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WebSocketService {

    private final Map<String, Chat> activeChats = new ConcurrentHashMap<>();

    public String createChat(String userId) {
        String chatId = UUID.randomUUID().toString();
        Chat chat = new Chat(chatId, userId, true);
        activeChats.put(chatId, chat);
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
}
