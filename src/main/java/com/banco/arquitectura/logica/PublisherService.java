package com.banco.arquitectura.logica;
import com.banco.arquitectura.config.RabbitMQConfig;
import com.banco.arquitectura.controller.dto.NotificarDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
@Service
@Slf4j
public class PublisherService {
    private final RabbitTemplate rabbitTemplate;

    public PublisherService(RabbitTemplate rabbitTemplate) {
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