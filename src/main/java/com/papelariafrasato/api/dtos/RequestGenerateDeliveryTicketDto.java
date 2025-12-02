package com.papelariafrasato.api.dtos;

public record RequestGenerateDeliveryTicketDto(
        String userId,
        String deliveryId,
        String invoiceCode
){}
