package br.com.microservice.pedido.consumer;

import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.usecase.CreatePedidoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Return;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMQConsumer {

    private final CreatePedidoUseCase useCase;
    private final ObjectMapper objectMapper;

    public RabbitMQConsumer(CreatePedidoUseCase useCase, ObjectMapper objectMapper) {
        this.useCase = useCase;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "PedidoQueue")
    public void consumeMessage(String message) {
        log.info("Pedido recebido: {}", message);
        try {
            // Converte a String JSON para RequestCreatePedidoController
            InputCreatePedidoDTO inputDto = objectMapper.readValue(message, InputCreatePedidoDTO.class);

            log.info("Mensagem de pedido convertida para DTO: {}", inputDto.toString());

            // Chama o use case com o DTO de entrada
            useCase.create(inputDto);

            log.info("Pedido criado com sucesso a partir da mensagem da fila.");

        } catch (Exception e) {
            log.error("Ocorreu um erro inesperado ao processar a mensagem da fila: {}", e.getMessage(), e);
            // Capturar outras exceções que podem ocorrer durante o processamento do useCase
        }
    }
}