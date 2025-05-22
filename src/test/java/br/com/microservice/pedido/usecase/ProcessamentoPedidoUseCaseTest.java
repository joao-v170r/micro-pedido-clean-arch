package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import br.com.microservice.pedido.gateway.PagamentoGateway;
import br.com.microservice.pedido.gateway.dto.StatusPagamento;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessamentoPedidoUseCaseTest {

    @Mock
    private PagamentoGateway gatewayPagamento;

    @Mock
    private CrudPedidoGateway gateway;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private ProcessamentoPedidoUseCase useCase;

    private Pedido samplePedido;
    private OutputPagamentoDTO paymentOutputDTO;

    @BeforeEach
    void setUp() {
        DadosCliente cliente = new DadosCliente("1", "Nome Teste", "12345678900", "email@test.com");
        Endereco endereco = new Endereco("12345-678", "Rua Teste, 123", 1, 2);
        Set<ProdutoPedido> produtos = new HashSet<>(Collections.singletonList(
                new ProdutoPedido("prod1", BigDecimal.valueOf(10.0), 2)
        ));
        BigDecimal frete = BigDecimal.valueOf(5.0);
        MetodoPagamento metodoPagamento = MetodoPagamento.PIX;
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);

        samplePedido = Pedido.reconstruir(
                metodoPagamento, frete, endereco, StatusPedido.CRIADO, produtos, dataCriacao, cliente, "recibo123", "pedido123"
        );

        paymentOutputDTO = new OutputPagamentoDTO(
                "recibo123",
                "pedido123",
                BigDecimal.valueOf(25.0),
                br.com.microservice.pedido.gateway.dto.MoedaPagamento.BRL,
                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
                "Gateway",
                null,
                StatusPagamento.CONCLUIDO,
                LocalDateTime.now(),
                LocalDateTime.now(),
                "codGateway"
        );

        try {
            when(mapper.writeValueAsString(any())).thenReturn("someJson");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void processamentoPedidosCriado_shouldProcessPedidosWithConcluidoStatus() {
        // Arrange
        when(gateway.findAllWithStatus(StatusPedido.CRIADO)).thenReturn(List.of(samplePedido));
        when(gatewayPagamento.processaPagamento(anyString())).thenReturn(paymentOutputDTO);
        when(gateway.save(any(Pedido.class))).thenReturn(samplePedido);

        // Act
        useCase.processamentoPedidosCriado();

        // Assert
        verify(gateway, times(1)).findAllWithStatus(StatusPedido.CRIADO);
        verify(gatewayPagamento, times(1)).processaPagamento("recibo123");
        verify(gateway, times(2)).save(any(Pedido.class)); // Once to set to PROCESSANDO, once to set to PROCESSADO/CANCELADO/CRIADO
        assertEquals(StatusPedido.PROCESSADO, samplePedido.getStatus());
    }

    @Test
    void processamentoPedidosCriado_shouldProcessPedidosWithCanceladoStatus() {
        // Arrange
        OutputPagamentoDTO cancelledPayment = new OutputPagamentoDTO(
                "recibo123",
                "pedido123",
                BigDecimal.valueOf(25.0),
                br.com.microservice.pedido.gateway.dto.MoedaPagamento.BRL,
                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
                "Gateway",
                null,
                StatusPagamento.CANCELADO, // Set status to CANCELADO
                LocalDateTime.now(),
                LocalDateTime.now(),
                "codGateway"
        );
        when(gateway.findAllWithStatus(StatusPedido.CRIADO)).thenReturn(List.of(samplePedido));
        when(gatewayPagamento.processaPagamento(anyString())).thenReturn(cancelledPayment);
        when(gateway.save(any(Pedido.class))).thenReturn(samplePedido);

        // Act
        useCase.processamentoPedidosCriado();

        // Assert
        verify(gateway, times(1)).findAllWithStatus(StatusPedido.CRIADO);
        verify(gatewayPagamento, times(1)).processaPagamento("recibo123");
        verify(gateway, times(2)).save(any(Pedido.class));
        assertEquals(StatusPedido.CANCELADO, samplePedido.getStatus());
    }

    @Test
    void processamentoPedidosCriado_shouldProcessPedidosWithPendenteStatus() {
        // Arrange
        OutputPagamentoDTO pendingPayment = new OutputPagamentoDTO(
                "recibo123",
                "pedido123",
                BigDecimal.valueOf(25.0),
                br.com.microservice.pedido.gateway.dto.MoedaPagamento.BRL,
                br.com.microservice.pedido.gateway.dto.MetodoPagamento.PIX,
                "Gateway",
                null,
                StatusPagamento.PENDENTE, // Set status to PENDENTE
                LocalDateTime.now(),
                LocalDateTime.now(),
                "codGateway"
        );
        when(gateway.findAllWithStatus(StatusPedido.CRIADO)).thenReturn(List.of(samplePedido));
        when(gatewayPagamento.processaPagamento(anyString())).thenReturn(pendingPayment);
        when(gateway.save(any(Pedido.class))).thenReturn(samplePedido);

        // Act
        useCase.processamentoPedidosCriado();

        // Assert
        verify(gateway, times(1)).findAllWithStatus(StatusPedido.CRIADO);
        verify(gatewayPagamento, times(1)).processaPagamento("recibo123");
        verify(gateway, times(2)).save(any(Pedido.class));
        assertEquals(StatusPedido.CRIADO, samplePedido.getStatus()); // Should revert to CRIADO or remain CRIADO
    }
}