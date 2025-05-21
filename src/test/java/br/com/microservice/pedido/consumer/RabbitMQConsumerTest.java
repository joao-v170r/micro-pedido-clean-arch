/*
package br.com.microservice.pedido.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class RabbitMQConsumerTest {

    @Test
    void consumeMessage_deveProcessarMensagem() {
        RabbitMQConsumer consumer = spy(new RabbitMQConsumer());
        String message = "Test Message";

        assertDoesNotThrow(() -> consumer.consumeMessage(message));
        verify(consumer).consumeMessage(message);
    }

    @Test
    void consumeMessage_deveTerAnotacaoRabbitListener() throws NoSuchMethodException {
        // Assert
        assertNotNull(
                RabbitMQConsumer.class.getMethod("consumeMessage", String.class)
                        .getAnnotation(RabbitListener.class)
        );
        assertEquals(
                "PedidoQueue",
                RabbitMQConsumer.class.getMethod("consumeMessage", String.class)
                        .getAnnotation(RabbitListener.class).queues()[0]
        );
    }
}*/
