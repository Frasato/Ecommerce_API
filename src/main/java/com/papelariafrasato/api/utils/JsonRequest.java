package com.papelariafrasato.api.utils;

import com.papelariafrasato.api.dtos.ProductPayload;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.Product;
import com.papelariafrasato.api.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Map<String, Object> buildDeliveryCost(String toPostalCode, List<ProductPayload> items) {
        Map<String, Object> body = new HashMap<>();

        Map<String, String> from = new HashMap<>();
        from.put("postal_code", "14735000");

        Map<String, String> to = new HashMap<>();
        to.put("postal_code", toPostalCode);

        Map<String, Object> options = new HashMap<>();
        options.put("own_hand", false);
        options.put("receipt", false);
        options.put("insurance_value", 0);
        options.put("use_insurance_value", false);

        List<Map<String, Object>> products = new ArrayList<>();
        for (ProductPayload p : items) {
            Map<String, Object> product = new HashMap<>();
            product.put("quantity", p.quantity());
            product.put("height", p.height());
            product.put("length", p.length());
            product.put("width", p.width());
            product.put("weight", p.weight());
            products.add(product);
        }

        body.put("from", from);
        body.put("to", to);
        body.put("services", "1,2,17");
        body.put("options", options);
        body.put("products", products);

        return body;
    }
}
