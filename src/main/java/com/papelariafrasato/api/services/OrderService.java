package com.papelariafrasato.api.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papelariafrasato.api.dtos.ResponseAllOrdersDto;
import com.papelariafrasato.api.dtos.ResponseOrderDto;
import com.papelariafrasato.api.exceptions.*;
import com.papelariafrasato.api.mappers.DeliveryOptions;
import com.papelariafrasato.api.models.*;
import com.papelariafrasato.api.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
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
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private DeliveryPackageRepository deliveryPackageRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentService paymentService;

    @Transactional
    public ResponseEntity<?> getAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok().body(orders);
    }

    @Transactional
    public ResponseEntity<?> createOrderOnlyOneProduct(String productId, String userId, Integer deliveryPrice){
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException(productId));

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setOrderItems(new ArrayList<>());

        if (product.getPriceWithDiscount() > 0) {
            order.setTotalPrice(product.getPriceWithDiscount() + deliveryPrice);
        } else {
            order.setTotalPrice(product.getPrice() + deliveryPrice);
        }

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        order.getOrderItems().add(orderItem);

        orderRepository.save(order);
        return ResponseEntity.status(201).body(new ResponseOrderDto(order));
    }

    @Transactional
    public ResponseEntity<?> createOrder(String userId, Integer optionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findCartByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        if (cart.getCartItem() == null || cart.getCartItem().isEmpty()) {
            throw new EmptyCartException();
        }

        String body = deliveryService.deliveryCost(userId);
        ObjectMapper objectMapper = new ObjectMapper();
        List<DeliveryOptions> options;

        try{
            options = objectMapper.readValue(
                    body,
                    new TypeReference<List<DeliveryOptions>>(){}
            );
        }catch (IOException ioException){
            throw new RuntimeException("Falha ao parsear resposta do delivery Service", ioException);
        }

        DeliveryOptions selected = options.stream()
                .filter(o -> o.getId() != null && o.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Opção de frete inválida: id = " + optionId));

        if(selected.isHas_error()){
            throw new IllegalStateException("Opção de frete com erro: id=" + optionId);
        }

        double deliveryPrice = selected.getPrice();
        Integer price = (int) deliveryPrice * 100;

        Order order = new Order();
        order.setUser(user);
        order.setStatus("PENDING");
        order.setTotalPrice(cart.getTotalPrice() + price);
        order.setOrderItems(new ArrayList<>());

        Payment payment = new Payment();
        payment.setOrder(order);

        order.setPayment(payment);

        paymentRepository.save(payment);
        orderRepository.save(order);

        for (CartItem cartItem : cart.getCartItem()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);

        DeliveryPackage deliveryPackage = new DeliveryPackage();
        DeliveryOptions.PackageItem firstPkg = (selected.getPackages() != null && !selected.getPackages().isEmpty())
                ? selected.getPackages().get(0)
                : null;

        if (firstPkg != null && firstPkg.getDimensions() != null) {
            deliveryPackage.setHeight(safeToDouble(firstPkg.getDimensions().getHeight()));
            deliveryPackage.setWidth(safeToDouble(firstPkg.getDimensions().getWidth()));
            deliveryPackage.setLength(safeToDouble(firstPkg.getDimensions().getLength()));
        } else {
            deliveryPackage.setHeight(null);
            deliveryPackage.setWidth(null);
            deliveryPackage.setLength(null);
        }

        if (firstPkg != null && firstPkg.getWeight() != null) {
            deliveryPackage.setWeight(firstPkg.getWeight().doubleValue());
        } else {
            deliveryPackage.setWeight(null);
        }

        deliveryPackage.setDeliveryPrice(price);
        deliveryPackage.setCreatedAt(Timestamp.from(Instant.now()));
        deliveryPackage.setOrder(order);
        deliveryPackage.setUser(user);
        deliveryPackage.setDeliveryTicketKey("N/A");
        deliveryPackage.setStatus("PENDING");
        deliveryPackage.setService(optionId.toString());

        deliveryPackageRepository.save(deliveryPackage);

        cartItemRepository.deleteAll(cart.getCartItem());
        cart.getCartItem().clear();
        cart.setTotalPrice(0);
        cartRepository.saveAndFlush(cart);

        return ResponseEntity.status(201).body(new ResponseOrderDto(order));
    }

    public ResponseEntity<?> getAllOrdersById(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        System.out.println("O que tem nas minhas orders: " + orders.toString());
        return ResponseEntity.ok().body(new ResponseAllOrdersDto(orders));
    }

    public ResponseEntity<?> getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return ResponseEntity.ok().body(new ResponseOrderDto(order));
    }

    @Transactional
    public ResponseEntity<String> updateOrderStatus(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        Payment payment = order.getPayment();
        String status = paymentService.getPaymentStatus(payment.getPaymentId());

        payment.setStatus(status);
        paymentRepository.save(payment);

        order.setStatus(status);
        orderRepository.save(order);
        return ResponseEntity.ok().body("Status has been changed");
    }

    private Double safeToDouble(String value) {
        if (value == null) return null;
        try {
            return Double.valueOf(value.replace(",", ".").trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 