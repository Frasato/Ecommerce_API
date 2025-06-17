package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.RequestPaymentCardDto;
import com.papelariafrasato.api.dtos.ResponsePaymentCardDto;
import com.papelariafrasato.api.utils.BuildXml;
import com.papelariafrasato.api.utils.PaymentHttp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private BuildXml buildXml;
    @Autowired
    private PaymentHttp paymentHttp;

    public ResponseEntity<?> processPayment(RequestPaymentCardDto paymentCardDto){

        if(paymentCardDto.cardNumber().isEmpty() ||
                paymentCardDto.cardName().isEmpty() ||
                paymentCardDto.expirationMonth().isEmpty() ||
                paymentCardDto.expirationYear().isEmpty() ||
                paymentCardDto.cvv().isEmpty())
        {
            return ResponseEntity.badRequest().body("Card fields can't be empty!");
        }

        try{
            String xmlRequest = buildXml.build(paymentCardDto);
            String xmlResponse = paymentHttp.sendXmlRequest(xmlRequest);
            ResponsePaymentCardDto response = paymentHttp.parseXmlResponse(xmlResponse);

            if(response.status().equals("ERROR")){
                return ResponseEntity.internalServerError().body("Status: " + response.status() + " message: " + response.message());
            }

            return ResponseEntity.ok(response);

        }catch(Exception e){
            return ResponseEntity.internalServerError().body("Error on process the payment: " + e.getMessage());
        }
    }

}
