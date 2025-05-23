package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDto(
        @Schema(description = "Complete name", example = "Steven Smith")
        String name,
        @Schema(description = "Password to logon", example = "123")
        String password,
        @Schema(description = "Email to logon", example = "steven@gmail.com")
        String email,
        @Schema(description = "User CPF", example = "11111111111")
        String cpf,
        @Schema(description = "Street name", example = "Av. Steven Smith")
        String street,
        @Schema(description = "House number", example = "32")
        String number,
        @Schema(description = "City name", example = "Buenos Aires")
        String city,
        @Schema(description = "CEP number of the city", example = "111111111")
        String CEP) {
}
