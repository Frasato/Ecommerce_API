package com.papelariafrasato.api.utils;

import com.papelariafrasato.api.dtos.ProductPayload;
import com.papelariafrasato.api.models.DeliveryPackage;
import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.User;
import org.springframework.stereotype.Component;

import java.time.Instant;
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

        body.put("payer", payer);

        return body;
    }

    public Map<String, Object> buildBodyCard(User user, Order order, int installments, String cardToken){
        Map<String, Object> body = new HashMap<>();
        body.put("transaction_amount", order.getTotalPrice() / 100.0);
        body.put("token", cardToken);
        body.put("description", "Pedido número: " + order.getId().trim());
        body.put("installments", installments);

        Map<String, Object> payer = new HashMap<>();
        payer.put("email", user.getEmail());

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

    public Map<String, Object> buildDeliveryTicket(User user, DeliveryPackage deliveryPackage, String inovicekey) {
        Map<String, Object> body = new HashMap<>();

        Map<String, String> from = new HashMap<>();
        from.put("name", "Drogarias Shalom Popular");
        from.put("address", "Avenida Severino Sicchieri");
        from.put("number", "307");
        from.put("district", "Centro");
        from.put("city", "Severínia");
        from.put("state_abbr", "SP");
        from.put("postal_code", "14735000");

        Map<String, String> to = new HashMap<>();
        to.put("name", user.getName());
        to.put("address", user.getAddress().getStreet());
        to.put("number", user.getAddress().getNumber());
        to.put("district", user.getAddress().getDistrict());
        to.put("city", user.getAddress().getCity());
        to.put("state_abbr", user.getAddress().getCountryState());
        to.put("postal_code", user.getAddress().getCEP());

        Map<String, Object> volume = new HashMap<>();
        volume.put("height", deliveryPackage.getHeight());
        volume.put("width", deliveryPackage.getWidth());
        volume.put("length", deliveryPackage.getLength());
        volume.put("weight", deliveryPackage.getWeight());

        Map<String, Object> options = new HashMap<>();
        Map<String, String> invoice = new HashMap<>();
        invoice.put("number", inovicekey);
        options.put("invoice", invoice);

        body.put("from", from);
        body.put("to", to);
        body.put("service", deliveryPackage.getService());
        body.put("volumes", volume);
        body.put("options", options);

        return body;
    }

    public Map<String, Object> generateTicket(String deliveryTicketKey){
        Map<String, Object> body = new HashMap<>();

        ArrayList<String> orders = new ArrayList<>();
        orders.add(deliveryTicketKey);

        body.put("orders", orders);

        return body;
    }
}
