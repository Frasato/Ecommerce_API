package com.papelariafrasato.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    private String chatId;
    private String userId;
    private boolean active;
    private LocalDateTime createdAt;

    public Chat(String chatId, String userId, boolean active){
        this.chatId = chatId;
        this.userId = userId;
        this.active = active;
        this.createdAt = LocalDateTime.now();
    }
}
