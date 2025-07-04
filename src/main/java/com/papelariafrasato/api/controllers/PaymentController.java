package com.papelariafrasato.api.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@Tag(
        name = "Payment",
        description = "EndPoints to create and process payment requests"
)
public class PaymentController {


}
