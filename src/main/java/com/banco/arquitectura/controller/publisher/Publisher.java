package com.banco.arquitectura.controller.publisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.banco.arquitectura.controller.dto.NotificarDTO;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
@Service
@Slf4j
public class Publisher {
    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage(NotificarDTO message) {
        rabbitTemplate.convertAndSend(
            RabbitMQConfig.EXCHANGE_NAME,
            RabbitMQConfig.ROUTING_KEY,
            message
        );
        log.info("Message sent: " + message);
    }
}