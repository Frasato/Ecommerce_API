package com.papelariafrasato.api.controllers;

import com.papelariafrasato.api.dtos.RequestDeliveryCostDto;
import com.papelariafrasato.api.dtos.RequestGenerateDeliveryTicketDto;
import com.papelariafrasato.api.dtos.ResponseOrderDto;
import com.papelariafrasato.api.services.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@Tag(
        name = "Delivery",
        description = "EndPoints to calculate the cust to delivery"
)
public class DeliveryCostController {

    @Autowired
    private DeliveryService deliveryService;

    @PostMapping()
    @Operation(
            summary = "Calculate",
            description = "Show how much to delivery"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery cust calculated"),
    })
    public ResponseEntity<?> deliveryCost(@RequestBody RequestDeliveryCostDto deliveryCostDto){
        String body = deliveryService.deliveryCost(deliveryCostDto.userId());
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/ticket")
    @Operation(
            summary = "Generate Ticket",
            description = "Generate Ticket to send to delivery"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Delivery cust calculated"),
    })
    public ResponseEntity<?> deliveryGenerateTicket(@RequestBody RequestGenerateDeliveryTicketDto deliveryTicketDto){
        return deliveryService.generateDeliveryTicket(deliveryTicketDto.userId(), deliveryTicketDto.deliveryId(), deliveryTicketDto.invoiceCode());
    }

    @GetMapping("/pending")
    @Operation(
            summary = "Get deliveries",
            description = "Get all peding deliveries"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<?> getPendingDelivery(){
        return deliveryService.allDeliveryOrders();
    }

    @GetMapping()
    @Operation(
            summary = "Get all deliveries",
            description = "Get all deliveries"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
    })
    public ResponseEntity<?> getAllDelivery(){
        return deliveryService.allDeliveryOrdersNotPending();
    }
}
