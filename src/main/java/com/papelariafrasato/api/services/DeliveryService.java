package com.papelariafrasato.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.ProductPayload;
import com.papelariafrasato.api.exceptions.CartNotFoundException;
import com.papelariafrasato.api.exceptions.EmptyCartException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.*;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.repositories.DeliveryPackageRepository;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.utils.JsonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeliveryService {

    @Value("${api.superfrete.url}")
    private String apiUrl;
    @Value("${api.superfrete.token}")
    private String apiToken;
    @Autowired
    private JsonRequest jsonRequest;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DeliveryPackageRepository deliveryPackageRepository;
    @Autowired
    private OrderRepository orderRepository;

    private final WebClient webClient = WebClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .build();

    public String deliveryCost(String userId) {
        System.out.println("Entrou no delivery cost");
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> cartItems = cart.getCartItem();
        if (cartItems == null || cartItems.isEmpty()) {
            throw new EmptyCartException();
        }

        System.out.println("Pegou os itens do carrinho: " +cartItems);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found to id: " + userId));

        List<ProductPayload> items = cartItems.stream().map(ci -> {
            Product pr = ci.getProduct();
            return new ProductPayload(
                    ci.getQuantity(),
                    parseDouble(pr.getHeight()),
                    parseDouble(pr.getProduct_length()),
                    parseDouble(pr.getWidth()),
                    parseDouble(pr.getWeight())
            );
        }).toList();

        Map<String, Object> body = jsonRequest.buildDeliveryCost(user.getAddress().getCEP(), items);

        System.out.println("Montou o corpo da requisição: " + body);
        var response = webClient.post()
                .uri(apiUrl + "/api/v0/calculator")
                .header("Authorization", "Bearer " + apiToken)
                .header("accept", "application/json")
                .header("User-Agent", "Shalom/1.0")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .block();

        System.out.println("Terminou a requisição: " + response.getBody() + " | " + response.getStatusCode());
        return response.getBody();
    }

    public ResponseEntity<?> generateDeliveryTicket(String userId, String deliveryId, String invoiceCode){
        DeliveryPackage deliveryPackage = deliveryPackageRepository.findById(deliveryId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Map<String, Object> body = jsonRequest.buildDeliveryTicket(user, deliveryPackage, invoiceCode);

        var response = webClient.post()
                .uri(apiUrl+"/api/v0/cart")
                .header("Authorization", "Bearer " + apiToken)
                .header("accept", "application/json")
                .header("User-Agent", "Shalom/1.0")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .block();

        Order order = deliveryPackage.getOrder();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(String.valueOf(response));
            String key = root.asText("id");
            String status = root.asText("stauts");

            deliveryPackage.setDeliveryTicketKey(key);
            deliveryPackageRepository.save(deliveryPackage);

            order.setStatus(status);
            orderRepository.save(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String paidStatus = sendToDelivery(deliveryPackage.getDeliveryTicketKey());
        order.setStatus(paidStatus);

        orderRepository.save(order);

        return ResponseEntity.ok().body(response.getBody());
    }

    public String sendToDelivery(String deliveryTicketKey){

        Map<String, Object> body = jsonRequest.generateTicket(deliveryTicketKey);

        var response = webClient.post()
            .uri(apiUrl+"/api/v0/checkout")
            .header("Authorization", "Bearer " + apiToken)
            .header("accept", "application/json")
            .header("User-Agent", "Shalom/1.0")
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .toEntity(String.class)
            .block();

        ObjectMapper mapper = new ObjectMapper();
        try{
            JsonNode root = mapper.readTree(String.valueOf(response));
            boolean success = root.path("success").asBoolean();
            String status = root.path("purchase").path("status").asText();

            if(success){
                return status;
            }else{
                throw new RuntimeException("Error on send ticket to delivery");
            }
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getPrintLink(String deliveryPackageId){
        DeliveryPackage deliveryPackage = deliveryPackageRepository.findById(deliveryPackageId)
                .orElseThrow(() -> new RuntimeException("Error on get DeliveryPackage"));

        Map<String, String> body = new HashMap<>();
        body.put("orders", deliveryPackage.getDeliveryTicketKey());

        var response = webClient.post()
            .uri(apiUrl+"/api/v0/checkout")
            .header("Authorization", "Bearer " + apiToken)
            .header("accept", "application/json")
            .header("User-Agent", "Shalom/1.0")
            .header("Content-Type", "application/json")
            .bodyValue(body)
            .retrieve()
            .toEntity(String.class)
            .block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(String.valueOf(response));
            String url = root.path("url").asText();

            return ResponseEntity.status(201).body(url);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> allDeliveryOrders(){
        List<DeliveryPackage> deliveryPackages = deliveryPackageRepository.findByPendingStatus();
        return ResponseEntity.ok().body(deliveryPackages);
    }

    public ResponseEntity<?> allDeliveryOrdersNotPending(){
        List<DeliveryPackage> deliveryPackages = deliveryPackageRepository.findByOtherStatus();
        return ResponseEntity.ok().body(deliveryPackages);
    }

    private static double parseDouble(String s) {
        if (s == null) throw new IllegalArgumentException("Valor numérico nulo");
        try {
            return Double.parseDouble(s.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Valor numérico inválido: " + s, e);
        }
    }
}
