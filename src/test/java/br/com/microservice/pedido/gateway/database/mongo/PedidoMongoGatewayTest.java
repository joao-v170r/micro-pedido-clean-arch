package br.com.microservice.pedido.gateway.database.mongo;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import br.com.microservice.pedido.gateway.database.mongo.mapper.PedidoGatewayMapper;
import br.com.microservice.pedido.gateway.database.mongo.repository.PedidoRepository;
import br.com.microservice.pedido.gateway.exception.mongo.GatewayExceptionMongo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoMongoGatewayTest {

    @Mock
    PedidoRepository repository;

    @InjectMocks
    PedidoMongoGateway gateway;

    private Pedido samplePedido;
    private PedidoEntity samplePedidoEntity;

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
        samplePedidoEntity = PedidoGatewayMapper.toEntity(samplePedido);
    }

    @Test
    void findById_shouldReturnPedidoWhenExists() {
        // Arrange
        when(repository.findById("pedido123")).thenReturn(Optional.of(samplePedidoEntity));

        // Act
        Optional<Pedido> result = gateway.findById("pedido123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("pedido123", result.get().getId());
        verify(repository).findById("pedido123");
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        // Arrange
        when(repository.findById("nonExistentId")).thenReturn(Optional.empty());

        // Act
        Optional<Pedido> result = gateway.findById("nonExistentId");

        // Assert
        assertFalse(result.isPresent());
        verify(repository).findById("nonExistentId");
    }

    @Test
    void findById_shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.findById(null));
        assertEquals("id invalido para consulta do gateway", exception.getMessage());
        verify(repository, never()).findById(anyString());
    }

    @Test
    void findById_shouldThrowIllegalArgumentExceptionWhenIdIsBlank() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.findById(" "));
        assertEquals("id invalido para consulta do gateway", exception.getMessage());
        verify(repository, never()).findById(anyString());
    }

    @Test
    void findById_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        when(repository.findById(anyString())).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.findById("pedido123"));
        assertEquals("error ao buscar um pedido por id", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).findById("pedido123");
    }

    @Test
    void findAllWithStatus_shouldReturnPedidosWithGivenStatus() {
        // Arrange
        List<PedidoEntity> entities = List.of(samplePedidoEntity);
        when(repository.findByStatus(StatusPedido.CRIADO)).thenReturn(entities);

        // Act
        List<Pedido> result = gateway.findAllWithStatus(StatusPedido.CRIADO);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(StatusPedido.CRIADO, result.get(0).getStatus());
        verify(repository).findByStatus(StatusPedido.CRIADO);
    }

    @Test
    void findAllWithStatus_shouldThrowIllegalArgumentExceptionWhenStatusIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.findAllWithStatus(null));
        assertEquals("status invalido para consulta do gateway", exception.getMessage());
        verify(repository, never()).findByStatus(any(StatusPedido.class));
    }

    @Test
    void findAllWithStatus_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        when(repository.findByStatus(any(StatusPedido.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.findAllWithStatus(StatusPedido.CRIADO));
        assertEquals("error ao buscar um pedido por id", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).findByStatus(StatusPedido.CRIADO);
    }

    @Test
    void existId_shouldReturnTrueWhenIdExists() {
        // Arrange
        when(repository.existsById("pedido123")).thenReturn(true);

        // Act
        Boolean result = gateway.existId("pedido123");

        // Assert
        assertTrue(result);
        verify(repository).existsById("pedido123");
    }

    @Test
    void existId_shouldReturnFalseWhenIdDoesNotExist() {
        // Arrange
        when(repository.existsById("nonExistentId")).thenReturn(false);

        // Act
        Boolean result = gateway.existId("nonExistentId");

        // Assert
        assertFalse(result);
        verify(repository).existsById("nonExistentId");
    }

    @Test
    void existId_shouldThrowIllegalArgumentExceptionWhenIdIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.existId(null));
        assertEquals("id invalido para consulta do gateway", exception.getMessage());
        verify(repository, never()).existsById(anyString());
    }

    @Test
    void existId_shouldThrowIllegalArgumentExceptionWhenIdIsBlank() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.existId(" "));
        assertEquals("id invalido para consulta do gateway", exception.getMessage());
        verify(repository, never()).existsById(anyString());
    }

    @Test
    void findAll_shouldReturnAllPedidos() {
        // Arrange
        List<PedidoEntity> entities = List.of(samplePedidoEntity);
        when(repository.findAll()).thenReturn(entities);

        // Act
        List<Pedido> result = gateway.findAll();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll();
    }

    @Test
    void findAll_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        when(repository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.findAll());
        assertEquals("erro ao buscar todos os pedidos", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).findAll();
    }

    @Test
    void save_shouldSavePedidoSuccessfully() {
        // Arrange
        when(repository.save(any(PedidoEntity.class))).thenReturn(samplePedidoEntity);

        // Act
        Pedido result = gateway.save(samplePedido);

        // Assert
        assertNotNull(result);
        assertEquals(samplePedido.getId(), result.getId());
        verify(repository).save(any(PedidoEntity.class));
    }

    @Test
    void save_shouldThrowIllegalArgumentExceptionWhenPedidoIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.save(null));
        assertEquals("objeto pedido esta nulo", exception.getMessage());
        verify(repository, never()).save(any(PedidoEntity.class));
    }

    @Test
    void save_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        when(repository.save(any(PedidoEntity.class))).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.save(samplePedido));
        assertEquals("error ao salvar pedido", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).save(any(PedidoEntity.class));
    }

    @Test
    void deleteById_shouldDeletePedidoSuccessfully() {
        // Arrange
        when(repository.existsById("pedido123")).thenReturn(true);
        doNothing().when(repository).deleteById("pedido123");

        // Act
        gateway.deleteById("pedido123");

        // Assert
        verify(repository).existsById("pedido123");
        verify(repository).deleteById("pedido123");
    }

    @Test
    void deleteById_shouldThrowIllegalArgumentExceptionWhenIdDoesNotExist() {
        // Arrange
        when(repository.existsById("nonExistentId")).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.deleteById("nonExistentId"));
        assertEquals("id do pedido não foi encontrado", exception.getMessage());
        verify(repository).existsById("nonExistentId");
        verify(repository, never()).deleteById(anyString());
    }

    @Test
    void deleteById_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        when(repository.existsById("pedido123")).thenReturn(true);
        doThrow(new RuntimeException("Database error")).when(repository).deleteById("pedido123");

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.deleteById("pedido123"));
        assertEquals("houver um error ao deletar o pedido", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).existsById("pedido123");
        verify(repository).deleteById("pedido123");
    }

    @Test
    void findAll_withPageable_shouldReturnPaginatedPedidos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(samplePedidoEntity)));

        // Act
        List<Pedido> result = gateway.findAll(pageable);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(repository).findAll(pageable);
    }

    @Test
    void findAll_withPageable_shouldThrowIllegalArgumentExceptionWhenPageableIsNull() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> gateway.findAll(null));
        assertEquals("o objeto pagable esta vazio", exception.getMessage());
        verify(repository, never()).findAll(any(Pageable.class));
    }

    @Test
    void findAll_withPageable_shouldThrowGatewayExceptionMongoWhenRepositoryThrowsException() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        GatewayExceptionMongo exception = assertThrows(GatewayExceptionMongo.class, () -> gateway.findAll(pageable));
        assertEquals("houver um error ao buscar o pedido com paginação", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        verify(repository).findAll(pageable);
    }
}