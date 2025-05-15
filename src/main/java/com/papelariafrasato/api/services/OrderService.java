package com.papelariafrasato.api.services;

import com.papelariafrasato.api.exceptions.EmptyCartException;
import com.papelariafrasato.api.exceptions.InvalidOrderStatusException;
import com.papelariafrasato.api.exceptions.OrderNotFoundException;
import com.papelariafrasato.api.models.*;
import com.papelariafrasato.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ResponseEntity<?> createOrder(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carrinho não encontrado"));

        if (cart.getCartItem() == null || cart.getCartItem().isEmpty()) {
            throw new EmptyCartException();
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotalPrice(cart.getTotalPrice());
        order.setOrderItems(new ArrayList<>());

        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        // Limpar o carrinho após criar o pedido
        cart.setCartItem(null);
        cart.setTotalPrice(0);
        cartRepository.save(cart);

        return ResponseEntity.status(201).body(order);
    }

    public ResponseEntity<?> getAllOrders(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return ResponseEntity.ok().body(orders);
    }

    public ResponseEntity<?> getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return ResponseEntity.ok().body(order);
    }

    @Transactional
    public ResponseEntity<?> updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!isValidStatus(status)) {
            throw new InvalidOrderStatusException(status);
        }

        // Validação de transição de status
        validateStatusTransition(order.getStatus(), status);

        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok().body(order);
    }

    private boolean isValidStatus(String status) {
        return status.equals("PENDING") || 
               status.equals("PAID") || 
               status.equals("DELIVERY") || 
               status.equals("FINISH");
    }

    private void validateStatusTransition(String currentStatus, String newStatus) {
        if (currentStatus.equals("FINISH")) {
            throw new InvalidOrderStatusException("Não é possível alterar o status de um pedido finalizado");
        }

        if (currentStatus.equals("PENDING") && !newStatus.equals("PAID")) {
            throw new InvalidOrderStatusException("Um pedido pendente só pode ser alterado para PAID");
        }

        if (currentStatus.equals("PAID") && !newStatus.equals("DELIVERY")) {
            throw new InvalidOrderStatusException("Um pedido pago só pode ser alterado para DELIVERY");
        }

        if (currentStatus.equals("DELIVERY") && !newStatus.equals("FINISH")) {
            throw new InvalidOrderStatusException("Um pedido em entrega só pode ser alterado para FINISH");
        }
    }
} 