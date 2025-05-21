package br.com.microservice.pedido.gateway.database.mongo.mapper;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import br.com.microservice.pedido.domain.value_objects.*;

import static org.junit.jupiter.api.Assertions.*;

class PedidoGatewayMapperTest {

    @Test
    void deveMapearDeDomainParaEntity() {
        // Arrange
        DadosCliente cliente = new DadosCliente("1", "João", "12345678900", "joao@email.com");
        Endereco endereco = new Endereco("27521-000", "123", 100, 100);
        ProdutoPedido produto = new ProdutoPedido("1", BigDecimal.TEN, 1);
        Set<ProdutoPedido> produtos = new HashSet<>();
        produtos.add(produto);
        LocalDateTime dataCriacao = LocalDateTime.now();

        Pedido pedido = Pedido.reconstruir(
                MetodoPagamento.PIX,
                BigDecimal.valueOf(15.90),
                endereco,
                StatusPedido.CRIADO,
                produtos,
                dataCriacao,
                cliente,
                "recibo123",
                "pedido123"
        );

        // Act
        PedidoEntity entity = PedidoGatewayMapper.toEntity(pedido);

        // Assert
        assertEquals(pedido.getId(), entity.getId());
        assertEquals(pedido.getReciboPagamento(), entity.getReciboPagamento());
        assertEquals(pedido.getCliente(), entity.getCliente());
        assertEquals(pedido.getDataCriacao(), entity.getDataCriacao());
        assertEquals(pedido.getProdutos().size(), entity.getProdutos().size());
        assertEquals(pedido.getStatus(), entity.getStatusPedido());
        assertEquals(pedido.getEnderecoEntrega(), entity.getEndereco());
        assertEquals(pedido.getFrete(), entity.getFrete());
        assertEquals(pedido.getMetodoPagamento(), entity.getMetodoPagamento());
    }

    @Test
    void deveMapearDeEntityParaDomain() {
        // Arrange
        DadosCliente cliente = new DadosCliente("1", "João", "12345678900", "joao@email.com");
        Endereco endereco = new Endereco("27521-000", "123", 100, 100);
        ProdutoPedido produto = new ProdutoPedido("1", BigDecimal.TEN, 1);
        LocalDateTime dataCriacao = LocalDateTime.now();

        PedidoEntity entity = new PedidoEntity(
                "pedido123",
                "recibo123",
                cliente,
                dataCriacao,
                java.util.List.of(produto),
                StatusPedido.CRIADO,
                endereco,
                BigDecimal.valueOf(15.90),
                MetodoPagamento.PIX
        );

        // Act
        Pedido domain = PedidoGatewayMapper.toDomain(entity);

        // Assert
        assertEquals(entity.getId(), domain.getId());
        assertEquals(entity.getReciboPagamento(), domain.getReciboPagamento());
        assertEquals(entity.getCliente(), domain.getCliente());
        assertEquals(entity.getDataCriacao(), domain.getDataCriacao());
        assertEquals(entity.getProdutos().size(), domain.getProdutos().size());
        assertEquals(entity.getStatusPedido(), domain.getStatus());
        assertEquals(entity.getEndereco(), domain.getEnderecoEntrega());
        assertEquals(entity.getFrete(), domain.getFrete());
        assertEquals(entity.getMetodoPagamento(), domain.getMetodoPagamento());
    }
}