package com.kunlun.firmwaresystem.mqtt;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DirectExchangeProducer {
    @Autowired
    private AmqpTemplate rabbitMQTemplate;

    public void send(String msg, String routingKey) {

        rabbitMQTemplate.convertAndSend(DirectExchangeRabbitMQConfig.directExchangeName, routingKey, msg);
    }
}