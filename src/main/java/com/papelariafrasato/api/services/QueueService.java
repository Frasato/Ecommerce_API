package com.papelariafrasato.api.services;

import com.papelariafrasato.api.dtos.RequestPaymentCardDto;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

import java.io.IOException;

import static com.papelariafrasato.api.config.RabbitMQConfig.PAYMENT_DLQ;
import static com.papelariafrasato.api.config.RabbitMQConfig.PAYMENT_QUEUE;

@Service
public class QueueService {

    @Autowired
    private PaymentService paymentService;

    @RabbitListener(queues = PAYMENT_QUEUE)
    public void receivePayment(RequestPaymentCardDto paymentCardDto, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        try {
            paymentService.processPayment(paymentCardDto);
            channel.basicNack(deliveryTag, false, false);
        }catch(Exception e){
            System.out.println(e.getMessage());
            try {
                channel.basicNack(deliveryTag, false, false);
            }catch(IOException exception){
                System.out.println(exception.getMessage());
            }
        }
    }

    @RabbitListener(queues = PAYMENT_DLQ)
    public void receiveFailedPayment(RequestPaymentCardDto paymentCardDto){
        System.out.println("Failed to process payment: " + paymentCardDto);
    }

}
