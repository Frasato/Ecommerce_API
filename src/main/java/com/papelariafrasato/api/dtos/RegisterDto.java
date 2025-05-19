package com.papelariafrasato.api.dtos;

public record RegisterDto(
        String name,
        String password,
        String email,
        String cpf,
        String street,
        String number,
        String city,
        String CEP) {
}
