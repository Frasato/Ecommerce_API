package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Payment;
import com.papelariafrasato.api.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public List<Payment> listAllPayments(){
        return paymentRepository.findAll();
    }

    public ResponseEntity<?> setPayment(String paymentName, double value, String method){
        //SEND TO PAYMENT API

         return ResponseEntity.ok().build();
    }

}
