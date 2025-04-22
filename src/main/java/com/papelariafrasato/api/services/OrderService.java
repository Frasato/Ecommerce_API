package com.papelariafrasato.api.services;

import com.papelariafrasato.api.models.Order;
import com.papelariafrasato.api.models.OrderItem;
import com.papelariafrasato.api.repositories.OrderItemRepository;
import com.papelariafrasato.api.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }

    public List<Order> getAllOrdersByUserId(String userId){
        return orderRepository.findByUserId(userId);
    }

    public ResponseEntity<?> createOrder(String userId, String productName, String productDescription, double price, String category, int quantity){
        try{
            // Validação dos parâmetros de entrada
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR: UserId é obrigatório");
            }
            if (productName == null || productName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("ERROR: Nome do produto é obrigatório");
            }
            if (price <= 0) {
                return ResponseEntity.badRequest().body("ERROR: Preço deve ser maior que zero");
            }
            if (quantity <= 0) {
                return ResponseEntity.badRequest().body("ERROR: Quantidade deve ser maior que zero");
            }

            // Buscar pedido aberto do usuário
            List<Order> userOrders = orderRepository.findByUserId(userId);
            Optional<Order> openOrder = userOrders.stream()
                .filter(order -> "open".equals(order.getStatus()))
                .findFirst();

            if(openOrder.isEmpty()){
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct_name(productName);
                orderItem.setProduct_description(productDescription);
                orderItem.setPrice(price);
                orderItem.setCategory(category);
                orderItem.setQuantity(quantity);

                return ResponseEntity.ok().build();
            }else{
                Order order = openOrder.get();
                String orderId = order.getOrder_id();
                
                if(orderId == null || orderId.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("ERROR: ID do pedido inválido");
                }

                List<OrderItem> orderItems = orderItemRepository.findOrderItemByIdOrderId(orderId);
                if(orderItems == null) {
                    orderItems = new ArrayList<>();
                }

                // Verificar se o item já existe no pedido
                boolean itemExists = orderItems.stream()
                    .anyMatch(item -> productName.equals(item.getProduct_name()));
                
                if(itemExists) {
                    return ResponseEntity.badRequest().body("ERROR: Produto já existe no pedido");
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setProduct_name(productName);
                orderItem.setProduct_description(productDescription);
                orderItem.setPrice(price);
                orderItem.setCategory(category);
                orderItem.setQuantity(quantity);

                orderItems.add(orderItem);
                order.setOrder_item(orderItems);
                orderItemRepository.save(orderItem);
                orderRepository.save(order);

                return ResponseEntity.ok().build();
            }
        }catch(Exception exception){
            // Log detalhado do erro
            exception.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro ao processar o pedido: " + exception.getMessage());
        }
    }

}
