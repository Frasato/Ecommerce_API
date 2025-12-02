package com.papelariafrasato.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class CardTokenService {

    @Value("${api.payment.token}")
    private String accessToken;
    @Value("${api.payment.public.key}")
    private String publicKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> createCardToken(
            String cardNumber,
            String cardholderName,
            String expirationMonth,
            String expirationYear,
            String securityCode,
            String docNumber
    ) {
        try {
            String url = "https://api.mercadopago.com/v1/card_tokens";

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("card_number", cardNumber.replaceAll("\\s+", ""));
            requestBody.put("expiration_month", Integer.parseInt(expirationMonth));
            requestBody.put("expiration_year", Integer.parseInt(expirationYear));
            requestBody.put("security_code", securityCode);

            Map<String, Object> cardholder = new HashMap<>();
            cardholder.put("name", cardholderName);

            Map<String, String> identification = new HashMap<>();
            identification.put("type", "CPF");
            identification.put("number", docNumber.replaceAll("[^0-9]", ""));
            cardholder.put("identification", identification);

            requestBody.put("cardholder", cardholder);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + accessToken);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            System.out.println("Resposta no card token generator: -------- " + response.getBody());

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            //String paymentMethodId = getPaymentMethodId(jsonResponse.get("first_six_digits").asText());

            Map<String, String> result = new HashMap<>();
            result.put("cardToken", jsonResponse.get("id").asText());
            //result.put("paymentMethodId", paymentMethodId);
            result.put("cardStatus", jsonResponse.get("status").asText());

            System.out.println("Retorno no card token generator: -------- " + result);
            return result;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar token do cartão: " + e.getMessage());
        }
    }

    private String getPaymentMethodId(String bin) {
        try {
            String url = "https://api.mercadopago.com/v1/payment_methods/card_bins/" + bin + "&public_key=" + publicKey;

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            System.out.println("Resposta no get payment method: -------- " + response.getBody() + " | BIN: " + bin);

            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            JsonNode results = jsonResponse.get("results");

            if (results != null && results.isArray() && results.size() > 0) {
                for (JsonNode result : results) {
                    String paymentType = result.get("payment_type_id").asText();
                    if ("credit_card".equals(paymentType)) {
                        System.out.println("Achou a bandeira certa aqui em: -------------------- " + result.get("id").asText());
                        return result.get("id").asText();
                    }
                }
            }

            throw new RuntimeException("Bandeira do cartão não identificada");

        } catch (Exception e) {
            throw new RuntimeException("Erro ao obter payment method: " + e.getMessage());
        }
    }
}