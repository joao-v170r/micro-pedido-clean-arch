/*
package br.com.microservice.pedido.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    @RabbitListener(queues = "PedidoQueue")
    public void consumeMessage(String message) {
        System.out.println("Mensagem recebida: " + message);
        // Lógica para processar a mensagem
    }
}*/