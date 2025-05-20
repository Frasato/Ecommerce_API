package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.ResponsePixQrCodeDto;
import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.Payment;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.OrderRepository;
import com.papelariafrasato.api.repositories.PaymentRepository;
import com.papelariafrasato.api.repositories.UserRepository;
import com.papelariafrasato.api.utils.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class PayService {

    @Value("${api.payment.url}")
    private String url;
    @Value("${api.payment.key}")
    private String key;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    public ResponseEntity<ResponsePixQrCodeDto> generatePix(String userId, String orderId) throws IOException, InterruptedException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        String customer;

        if(user.getCustomerId().isBlank()){
            Customer generateCustomer = new Customer();
            customer = generateCustomer.create(user.getName(), user.getCpf());
        }else {
            customer = user.getCustomerId();
        }

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        double value = order.getTotalPrice();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"/payments"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("access_token", key)
                .method("POST", HttpRequest.BodyPublishers.ofString(
                        "{\"billingType\":\"PIX\",\"value\":" + value +",\"dueDate\":\""+ Date.from(Instant.now()) +"\",\"customer\":\""+ customer +"\"}"
                ))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        String paymenteId = root.get("id").asText();

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentId(paymenteId);

        HttpRequest requestQrCode = HttpRequest.newBuilder()
                .uri(URI.create(url+"/payments/"+ paymenteId +"/pixQrCode"))
                .header("accept", "application/json")
                .header("access_token", key)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> responseQrCode = HttpClient.newHttpClient().send(requestQrCode, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapperQrCode = new ObjectMapper();
        JsonNode rootQrCode = mapperQrCode.readTree(responseQrCode.body());

        return ResponseEntity.status(201).body(new ResponsePixQrCodeDto(rootQrCode.get("encodedImage").asText(), rootQrCode.get("payload").asText()));
    }

    public ResponseEntity<?>  cardPaymente(String userId, String orderId, int parcels) throws IOException, InterruptedException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        String customerId;
        double value = order.getTotalPrice();
        Payment payment = new Payment();
        payment.setUser(user);

        if(user.getCustomerId().isEmpty()){
            Customer customer = new Customer();
            customerId = customer.create(user.getName(), user.getCpf());
        }else{
            customerId = user.getCustomerId();
        }

        if(parcels > 1){
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "/payments"))
                    .header("accept", "application/json")
                    .header("content-type", "application/json")
                    .header("access_token", key)
                    .method("POST", HttpRequest.BodyPublishers.ofString(
                            "{\"billingType\":\"PIX\",\"value\":" + value +"\",\"dueDate\":"
                                    + Date.from(Instant.now().plus(Duration.ofDays(4))) +"\",\"customer\":"+ customerId +"\",\"installmentCount\":"
                                    + parcels +"\"totalValue\":"+ value + value*0.1 +"}"))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.body());
            String paymentId = root.get("id").asText();
            payment.setPaymentId(paymentId);
            paymentRepository.save(payment);

            return ResponseEntity.status(201).build();
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"/payments"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("access_token", key)
                .method("POST", HttpRequest.BodyPublishers.ofString(
                "{\"billingType\":\"PIX\",\"value\":" + value +"\",\"dueDate\":"
                        + Date.from(Instant.now().plus(Duration.ofDays(4))) +"\",\"customer\":"+ customerId +"\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        String paymentId = root.get("id").asText();
        payment.setPaymentId(paymentId);
        paymentRepository.save(payment);
        return ResponseEntity.status(201).build();
    }
}
