package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponseUserDto(
        @Schema(description = "Name registered", example = "Steven Smith")
        String name,
        @Schema(description = "UUID user id", example = "adaw331-asdaf31-242fr2-qd31fe33")
        String userId,
        @Schema(description = "Json Web Token to authenticate", example = "asdasdaANNJANpajsnpjanduanuxxxsefnuDSFNSDFUSfnsDdDDsndfndSdnusndusn")
        String token) {
}
