package com.papelariafrasato.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String PAYMENT_QUEUE = "payment-queue";
    public static final String PAYMENT_DLQ = "payment-dlq";
    public static final String PAYMENT_EXCHANGE = "payment-exchange";
    public static final String PAYMENT_DLQ_EXCHANGE = "payment-dlq-exchange";
    public static final String PAYMENT_ROUTING_KEY = "payment.process";
    public static final String PAYMENT_DLQ_ROUTING_KEY = "payment.failed";

    @Bean
    public Queue paymentQueue(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", PAYMENT_DLQ_EXCHANGE);
        args.put("x-dead-letter-routing-key", PAYMENT_DLQ_ROUTING_KEY);
        args.put("x-message-ttl", 300000);
        return new Queue(PAYMENT_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue paymentDLQ(){
        return new Queue(PAYMENT_DLQ, true);
    }

    @Bean
    public DirectExchange paymentExchange(){
        return new DirectExchange(PAYMENT_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentDLQExchange(){
        return new DirectExchange(PAYMENT_DLQ_EXCHANGE);
    }

    @Bean
    public Binding paymentBinding(){
        return BindingBuilder
                .bind(paymentQueue())
                .to(paymentExchange())
                .with(PAYMENT_ROUTING_KEY);
    }

    @Bean
    public Binding paymentDLQBinding(){
        return BindingBuilder
                .bind(paymentDLQ())
                .to(paymentDLQExchange())
                .with(PAYMENT_DLQ_ROUTING_KEY);
    }
}
