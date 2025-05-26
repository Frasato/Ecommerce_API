package com.papelariafrasato.api.dtos;

public record ChatDto(
        String chatId,
        String userId,
        String message,
        String senderType,
        boolean isActive,
        String timestamp
){}
