package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.ProductPayload;
import com.papelariafrasato.api.exceptions.CartNotFoundException;
import com.papelariafrasato.api.models.Cart;
import com.papelariafrasato.api.models.CartItem;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.repositories.CartRepository;
import com.papelariafrasato.api.utils.JsonRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    private final WebClient webClient = WebClient.builder()
            .defaultHeader("Content-Type", "application/json")
            .build();

    public ResponseEntity<?> deliveryCost(String userId, String toPostalCode) {
        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> cartItems = cart.getCartItem();
        if (cartItems == null || cartItems.isEmpty()) {
            return ResponseEntity.badRequest().body("Carrinho está vazio");
        }

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

        Map<String, Object> body = jsonRequest.buildDeliveryCost(toPostalCode, items);

        var response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiToken)
                .header("accept", "application/json")
                .header("User-Agent", "Minha Loja Api ")
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .toEntity(String.class)
                .block();

        return ResponseEntity.ok().body(response.getBody());
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
