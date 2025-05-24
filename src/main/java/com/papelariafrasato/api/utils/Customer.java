package com.papelariafrasato.api.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class Customer {

    @Value("${api.payment.url}")
    private String url;
    @Value("${api.payment.key}")
    private String key;

    public String create(String name, String cpfCnpj) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url+"/customers"))
                .header("accept", "application/json")
                .header("content-type", "application/json")
                .header("access_token", key)
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"name\":\""+ name.toLowerCase() +"\",\"cpfCnpj\":\""+cpfCnpj+"\"}"))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.body());
        return root.get("id").asText();
    }

}
