package com.papelariafrasato.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record ResponsePixQrCodeDto(
        @Schema(description = "Encoded QR Code Pix", example = "hunijdfvnskdjasdmaksdadnajnajnajsdadawasdagtbbbppllkrjjnsdnfakfnad")
        String encodedImage,
        @Schema(description = "Copy And Paste Pix Code", example = "hunijdfvnskdjasdmaksdadnajnajnajsdadawasdagtbbbppl")
        String copyAndPaste
) {
}
