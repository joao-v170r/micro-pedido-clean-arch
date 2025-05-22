package br.com.microservice.pedido.domain;

import br.com.microservice.pedido.domain.value_objects.DadosCliente;
import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private final DadosCliente cliente = new DadosCliente("1", "Nome Teste", "12345678900", "email@test.com");
    private final Endereco endereco = new Endereco("12345-678", "Rua Teste, 123", 1, 2);
    private final Set<ProdutoPedido> produtos = new HashSet<>(Collections.singletonList(
            new ProdutoPedido("prod1", BigDecimal.valueOf(10.0), 2)
    ));
    private final BigDecimal frete = BigDecimal.valueOf(5.0);
    private final MetodoPagamento metodoPagamento = MetodoPagamento.PIX;

    @Test
    void criar_deveRetornarPedidoComStatusCriadoEIdNuloEReciboNulo() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);

        // Act
        Pedido pedido = Pedido.criar(cliente, dataCriacao, produtos, endereco, frete, metodoPagamento);

        // Assert
        assertNotNull(pedido);
        assertNull(pedido.getId());
        assertNull(pedido.getReciboPagamento());
        assertEquals(StatusPedido.CRIADO, pedido.getStatus());
        assertEquals(cliente, pedido.getCliente());
        assertEquals(dataCriacao, pedido.getDataCriacao());
        assertEquals(produtos, pedido.getProdutos());
        assertEquals(endereco, pedido.getEnderecoEntrega());
        assertEquals(frete, pedido.getFrete());
        assertEquals(metodoPagamento, pedido.getMetodoPagamento());
    }

    @Test
    void reconstruir_deveRetornarPedidoComTodosOsCamposPreenchidos() {
        // Arrange
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);
        String id = "pedido-id-123";
        String reciboPagamento = "recibo-123";
        StatusPedido status = StatusPedido.PROCESSANDO;

        // Act
        Pedido pedido = Pedido.reconstruir(
                metodoPagamento, frete, endereco, status, produtos, dataCriacao, cliente, reciboPagamento, id
        );

        // Assert
        assertNotNull(pedido);
        assertEquals(id, pedido.getId());
        assertEquals(reciboPagamento, pedido.getReciboPagamento());
        assertEquals(status, pedido.getStatus());
        assertEquals(cliente, pedido.getCliente());
        assertEquals(dataCriacao, pedido.getDataCriacao());
        assertEquals(produtos, pedido.getProdutos());
        assertEquals(endereco, pedido.getEnderecoEntrega());
        assertEquals(frete, pedido.getFrete());
        assertEquals(metodoPagamento, pedido.getMetodoPagamento());
    }

    @Test
    void validaFrete_deveLancarExcecaoQuandoFreteNegativo() {
        // Arrange
        BigDecimal freteNegativo = BigDecimal.valueOf(-10.0);
        LocalDateTime dataCriacao = LocalDateTime.now().minusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Pedido.criar(cliente, dataCriacao, produtos, endereco, freteNegativo, metodoPagamento)
        );
        assertEquals("Frete não pode ser negativo", exception.getMessage());
    }

    @Test
    void validaDataCriacao_deveLancarExcecaoQuandoDataFutura() {
        // Arrange
        LocalDateTime dataFutura = LocalDateTime.now().plusDays(1);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                Pedido.criar(cliente, dataFutura, produtos, endereco, frete, metodoPagamento)
        );
        assertEquals("Data de criação do pedido não pode ser uma data futura", exception.getMessage());
    }

    @Test
    void getValorTotal_deveCalcularCorretamente() {
        // Arrange
        Set<ProdutoPedido> produtosParaCalculo = new HashSet<>();
        produtosParaCalculo.add(new ProdutoPedido("prodA", BigDecimal.valueOf(10.0), 2)); // 20.0
        produtosParaCalculo.add(new ProdutoPedido("prodB", BigDecimal.valueOf(5.0), 3));  // 15.0
        BigDecimal freteCalculo = BigDecimal.valueOf(7.50); // 7.50
        // Total esperado: 20.0 + 15.0 + 7.50 = 42.50

        Pedido pedido = Pedido.criar(
                cliente, LocalDateTime.now().minusDays(1), produtosParaCalculo, endereco, freteCalculo, metodoPagamento
        );

        // Act
        BigDecimal valorTotal = pedido.getValorTotal();

        // Assert
        assertEquals(BigDecimal.valueOf(42.50), valorTotal);
    }

    @Test
    void setReciboPagamento_deveAtualizarRecibo() {
        // Arrange
        Pedido pedido = Pedido.criar(cliente, LocalDateTime.now().minusDays(1), produtos, endereco, frete, metodoPagamento);
        String novoRecibo = "novo-recibo-456";

        // Act
        pedido.setReciboPagamento(novoRecibo);

        // Assert
        assertEquals(novoRecibo, pedido.getReciboPagamento());
    }

    @Test
    void setStatus_deveAtualizarStatus() {
        // Arrange
        Pedido pedido = Pedido.criar(cliente, LocalDateTime.now().minusDays(1), produtos, endereco, frete, metodoPagamento);
        StatusPedido novoStatus = StatusPedido.PROCESSANDO;

        // Act
        pedido.setStatus(novoStatus);

        // Assert
        assertEquals(novoStatus, pedido.getStatus());
    }

    @Test
    void setEnderecoEntrega_deveAtualizarEndereco() {
        // Arrange
        Pedido pedido = Pedido.criar(cliente, LocalDateTime.now().minusDays(1), produtos, endereco, frete, metodoPagamento);
        Endereco novoEndereco = new Endereco("87654-321", "Nova Rua, 456", 3, 4);

        // Act
        pedido.setEnderecoEntrega(novoEndereco);

        // Assert
        assertEquals(novoEndereco, pedido.getEnderecoEntrega());
    }
}