package com.papelariafrasato.api.dtos;

import com.papelariafrasato.api.models.Chat;

import java.util.List;

public record ResponseListChatsDto(
        List<Chat> chats
){}
