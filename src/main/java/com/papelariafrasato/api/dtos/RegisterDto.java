package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterDto(
        @Schema(description = "Complete name", example = "Steven Smith")
        String name,
        @Schema(description = "Password to logon", example = "123")
        String password,
        @Schema(description = "Email to logon", example = "steven@gmail.com")
        String email,
        @Schema(description = "Phone number", example = "+1 55 99825771")
        String phone,
        @Schema(description = "User CPF", example = "11111111111")
        String cpf,
        @Schema(description = "Street name", example = "Av. Steven Smith")
        String street,
        @Schema(description = "House number", example = "32")
        String number,
        @Schema(description = "Country you live", example = "Washington... São Paulo...")
        String countryState,
        @Schema(description = "The district your house stay", example = "Crawford St... Av. 9 de julho")
        String district,
        @Schema(description = "City name", example = "Buenos Aires")
        String city,
        @Schema(description = "CEP number of the city", example = "111111111")
        String CEP) {
}
