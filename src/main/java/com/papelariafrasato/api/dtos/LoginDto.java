package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDto(
        @Schema(description = "Email to login", example = "steven@gmail.com")
        String email,
        @Schema(description = "Password to login", example = "123")
        String password) {
}
