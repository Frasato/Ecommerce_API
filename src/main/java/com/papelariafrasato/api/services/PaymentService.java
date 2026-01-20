package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.RequestCardDto;
import com.papelariafrasato.api.dtos.RequestMoneyPaymentDto;
import com.papelariafrasato.api.dtos.RequestPixDto;
import com.papelariafrasato.api.exceptions.CityIsNotQualifiedException;
import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.Payment;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.PaymentRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.utils.JsonRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    @Autowired
    private CardTokenService cardToken;
    @Autowired
    private PaymentRepository paymentRepository;
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
        Payment payment = order.getPayment();

        Map<String, Object> body = jsonRequest.buildBodyPix(user, order);

        try {
            var response = webClient.post()
                    .uri(paymentUrl)
                    .header("Authorization", "Bearer " + paymentToken)
                    .header("X-Idempotency-Key", order.getId() + LocalDateTime.now())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            payment.setPaymentId(root.get("id").asText());
            payment.setPaymentType("PIX");
            payment.setStatus(root.get("status").asText());

            paymentRepository.save(payment);

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
        Payment payment = order.getPayment();

        Map<String, String> responseCardToken = cardToken.createCardToken(cardDto.cardNumber(),
                cardDto.cardholderName(),
                cardDto.expirationMonth(),
                cardDto.expirationYear(),
                cardDto.securityCode(),
                user.getCpf());

        if(!responseCardToken.get("cardStatus").equals("active")) return ResponseEntity.badRequest().body(responseCardToken.get("cardStatus"));

        Map<String, Object> body = jsonRequest.buildBodyCard(user, order, cardDto.installments(), responseCardToken.get("cardToken"));

        try {
            var response = webClient.post()
                    .uri(paymentUrl)
                    .header("Authorization", "Bearer " + paymentToken)
                    .header("X-Idempotency-Key", order.getId() + LocalDateTime.now())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            payment.setPaymentId(root.get("id").asText());
            payment.setPaymentType("CARD");
            payment.setStatus("pending");

            return ResponseEntity.status(201).body(response);
        }catch (WebClientResponseException ex) {
            System.out.println("Erro MP payments status=" + ex.getRawStatusCode() + " body=" + ex.getResponseBodyAsString());
            return ResponseEntity.status(ex.getRawStatusCode()).body(ex.getResponseBodyAsString());
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    public String getPaymentStatus(String id) {

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String url = "https://api.mercadopago.com/v1/payments/"+id;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer "+paymentToken);
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class);


            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            return jsonResponse.get("stauts").asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter status do pagamento: " + e.getMessage());
        }
    }

    public ResponseEntity<?> moneyPayment(RequestMoneyPaymentDto moneyDto){
        User user = userRepository.findById(moneyDto.userId())
                .orElseThrow(() -> new UserNotFoundException(moneyDto.userId()));

        if(!user.getAddress().getCity().equalsIgnoreCase("severínia")) throw new CityIsNotQualifiedException(user.getAddress().getCity());

        Order order = orderRepository.findById(moneyDto.orderId())
                .orElseThrow(() -> new OrderNotFoundException(moneyDto.orderId()));

        Payment payment = new Payment();
        payment.setPaymentType("MONEY");
        payment.setStatus("AUTHORIZED");
        payment.setChangeFor(moneyDto.changeFor());

        order.setPayment(payment);
        orderRepository.save(order);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
