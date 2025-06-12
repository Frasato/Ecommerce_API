package com.papelariafrasato.api.services;

import com.papelariafrasato.api.config.RabbitMQConfig;
import com.papelariafrasato.api.dtos.RequestPaymentCardDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentQueueService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendPaymentToQueue(RequestPaymentCardDto paymentCardDto){
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.PAYMENT_EXCHANGE,
                RabbitMQConfig.PAYMENT_ROUTING_KEY,
                paymentCardDto);
    }

}
