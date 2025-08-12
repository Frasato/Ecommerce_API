package com.papelariafrasato.api.dtos;

public record ProductPayload(
        int quantity,
        double height,
        double length,
        double width,
        double weight
){}
