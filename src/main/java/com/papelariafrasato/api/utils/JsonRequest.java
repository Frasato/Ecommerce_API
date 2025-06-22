package com.papelariafrasato.api.utils;

import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonRequest {

    public Map<String, Object> buildBodyPix(User user, Order order){
        Map<String, Object> body = new HashMap<>();
        body.put("transaction_amount", order.getTotalPrice());
        body.put("description", "Pedido número: " + order.getId());
        body.put("payment_method_id", "pix");

        Map<String, Object> payer = new HashMap<>();
        payer.put("email", user.getEmail());
        payer.put("first_name", user.getName());
        payer.put("last_name", user.getName());

        Map<String, Object> identification = new HashMap<>();
        identification.put("type", "CPF");
        identification.put("number", user.getCpf());

        payer.put("identification", identification);
        body.put("payer", payer);

        return body;
    }

    public Map<String, Object> buildBodyCard(User user, Order order, int installments, String paymentMethod, String cardToken){
        Map<String, Object> body = new HashMap<>();
        body.put("transaction_amount", order.getTotalPrice());
        body.put("token", cardToken);
        body.put("description", "Pedido número: " + order.getId());
        body.put("payment_method_id", paymentMethod);
        body.put("installments", installments);

        Map<String, Object> payer = new HashMap<>();
        payer.put("email", user.getEmail());
        payer.put("first_name", user.getName());
        payer.put("last_name", user.getName());

        Map<String, Object> identification = new HashMap<>();
        identification.put("type", "CPF");
        identification.put("number", user.getCpf());

        payer.put("identification", identification);
        body.put("payer", payer);

        return body;
    }

}
