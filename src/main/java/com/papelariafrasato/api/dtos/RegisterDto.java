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
        String cpf)
{}
