package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.exceptions.UserNotFoundException;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.User;
import com.papelariafrasato.api.repositories.OrderRepository;
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
import java.time.Instant;
import java.util.Date;

@Service
public class PayService {

    @Value("${api.payment.key}")
    private String url;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<?> generatePix(String userId, String orderId) throws IOException, InterruptedException {

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
                .method("POST", HttpRequest.BodyPublishers.ofString(
                        "{\"billingType\":\"PIX\",\"value\":" + value +",\"dueDate\":\""+ Date.from(Instant.now()) +"\",\"customer\":\""+ customer +"\"}"
                ))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        return ResponseEntity.status(201).body(response.body());
    }
}
