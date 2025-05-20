package com.papelariafrasato.api.dtos;

public record RequestCardPaymentDto(
        String userId,
        String orderId,
        int parcel
) {
}
