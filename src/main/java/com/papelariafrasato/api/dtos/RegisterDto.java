package com.papelariafrasato.api.dtos;

public record RegisterDto(
        String name,
        String password,
        String email,
        String role,
        String street,
        int number,
        String city,
        int CEP) {
}
