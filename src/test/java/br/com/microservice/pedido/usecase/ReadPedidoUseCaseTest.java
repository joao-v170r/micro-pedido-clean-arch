package br.com.microservice.pedido.usecase;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.gateway.CrudPedidoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadPedidoUseCaseTest {

    @Mock
    private CrudPedidoGateway gateway;

    @InjectMocks
    private ReadPedidoUseCase readPedidoUseCase;

    private Pedido samplePedido;

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
    }

    @Test
    void findAll_shouldReturnAllPedidos() {
        // Arrange
        when(gateway.findAll()).thenReturn(List.of(samplePedido));

        // Act
        List<PedidoDTO> result = readPedidoUseCase.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("pedido123", result.get(0).id());
        verify(gateway, times(1)).findAll();
    }

    @Test
    void findAll_withPageable_shouldReturnPaginatedPedidos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(gateway.findAll(pageable)).thenReturn(List.of(samplePedido));

        // Act
        List<PedidoDTO> result = readPedidoUseCase.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("pedido123", result.get(0).id());
        verify(gateway, times(1)).findAll(pageable);
    }

    @Test
    void findById_shouldReturnPedidoWhenFound() {
        // Arrange
        when(gateway.findById("pedido123")).thenReturn(Optional.of(samplePedido));

        // Act
        PedidoDTO result = readPedidoUseCase.findById("pedido123");

        // Assert
        assertNotNull(result);
        assertEquals("pedido123", result.id());
        verify(gateway, times(1)).findById("pedido123");
    }

    @Test
    void findById_shouldThrowRuntimeExceptionWhenPedidoNotFound() {
        // Arrange
        when(gateway.findById(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> readPedidoUseCase.findById("nonExistentId"));
        assertEquals("Não foi possivel localizar o pedido", exception.getMessage());
        verify(gateway, times(1)).findById("nonExistentId");
    }
}