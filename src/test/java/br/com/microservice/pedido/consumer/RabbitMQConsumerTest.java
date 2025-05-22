package br.com.microservice.pedido.consumer;

import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.usecase.CreatePedidoUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RabbitMQConsumerTest {

    @Mock
    private CreatePedidoUseCase useCase;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RabbitMQConsumer rabbitMQConsumer;

    @Test
    void consumeMessage_shouldHandleJsonProcessingException() throws JsonProcessingException {
        // Arrange
        String invalidMessage = "invalid json";
        when(objectMapper.readValue(eq(invalidMessage), any(Class.class))).thenThrow(JsonProcessingException.class);

        // Act
        rabbitMQConsumer.consumeMessage(invalidMessage);

        // Assert
        verify(objectMapper).readValue(eq(invalidMessage), any(Class.class));
        verify(useCase, never()).create(any(InputCreatePedidoDTO.class));
    }

    @Test
    void consumeMessage_shouldHandleOtherExceptionsDuringUseCaseExecution() throws JsonProcessingException {
        // Arrange
        String message = "{\"clienteId\":\"123\",\"dataCriacao\":\"2024-05-21T10:00:00\",\"produtos\":{\"prod1\":1},\"enderecoEntrega\":{\"cep\":\"12345-678\",\"enderecoCompleto\":\"Rua A, 123\",\"latitude\":10,\"longitude\":20},\"frete\":10.0,\"metodoPagamento\":\"PIX\"}";
        InputCreatePedidoDTO inputDto = new InputCreatePedidoDTO(
                "123", null, null, null, null, null); // Simplified for test
        when(objectMapper.readValue(message, InputCreatePedidoDTO.class)).thenReturn(inputDto);
        doThrow(new RuntimeException("Use case error")).when(useCase).create(any(InputCreatePedidoDTO.class));

        // Act
        rabbitMQConsumer.consumeMessage(message);

        // Assert
        verify(objectMapper).readValue(message, InputCreatePedidoDTO.class);
        verify(useCase).create(inputDto);
    }

    @Test
    void consumeMessage_shouldHaveRabbitListenerAnnotation() throws NoSuchMethodException {
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
}