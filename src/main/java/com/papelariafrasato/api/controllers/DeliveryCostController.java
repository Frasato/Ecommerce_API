package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestDeliveryCostDto;
import com.papelariafrasato.api.services.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryCostController {

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping()
    public ResponseEntity<?> deliveryCost(@RequestBody RequestDeliveryCostDto deliveryCostDto){
        return deliveryService.deliveryCost(deliveryCostDto.userId(), deliveryCostDto.toPostalCode());
    }

}
