package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.RequestCardDto;
import com.papelariafrasato.api.dtos.RequestPixDto;
import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.utils.JsonRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JsonRequest jsonRequest;
    @Value("${api.payment.url}")
    private String paymentUrl;
    @Value("${api.payment.token}")
    private String paymentToken;
    @Value("${api.payment.x.id}")
    private String paymentId;
    private final WebClient webClient;

    private PaymentService(){
        this.webClient = WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public ResponseEntity<?> pixPayment(RequestPixDto pixDto){
        if(pixDto.orderId().isEmpty() || pixDto.userId().isEmpty()) {
            return ResponseEntity.badRequest().body(new BadRequestException());
        }

        User user = userRepository.findById(pixDto.userId())
                .orElseThrow(() -> new UserNotFoundException(pixDto.userId()));
        Order order = orderRepository.findById(pixDto.orderId())
                .orElseThrow(() -> new OrderNotFoundException(pixDto.orderId()));

        Map<String, Object> body = jsonRequest.buildBodyPix(user, order);

        try {
            var response = webClient.post()
                    .uri(paymentUrl)
                    .header("Authorization", "Bearer " + paymentToken)
                    .header("X-Idempotency-Key", paymentId)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.status(201).body(response);
        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public ResponseEntity<?> cardPayment(RequestCardDto cardDto){
        if(cardDto.orderId().isEmpty() || cardDto.userId().isEmpty()) {
            return ResponseEntity.badRequest().body(new BadRequestException());
        }

        User user = userRepository.findById(cardDto.userId())
                .orElseThrow(() -> new UserNotFoundException(cardDto.userId()));
        Order order = orderRepository.findById(cardDto.orderId())
                .orElseThrow(() -> new OrderNotFoundException(cardDto.orderId()));

        Map<String, Object> body = jsonRequest.buildBodyCard(user, order, cardDto.installments(), cardDto.paymentMethodId(), cardDto.cardToken());

        try {
            var response = webClient.post()
                    .uri(paymentUrl)
                    .header("Authorization", "Bearer " + paymentToken)
                    .header("X-Idempotency-Key", order.getId())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return ResponseEntity.status(201).body(response);
        }catch(Exception exception){
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

}
