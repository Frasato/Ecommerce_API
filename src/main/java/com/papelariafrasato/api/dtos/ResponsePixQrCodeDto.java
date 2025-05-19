package com.papelariafrasato.api.dtos;

public record ResponsePixQrCodeDto(
        String encodedImage,
        String copyAndPaste
) {
}
