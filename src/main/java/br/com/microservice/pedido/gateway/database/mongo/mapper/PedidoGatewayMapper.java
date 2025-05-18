package br.com.microservice.pedido.gateway.database.mongo.mapper;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.gateway.database.mongo.entity.PedidoEntity;

import java.util.HashSet;

public class PedidoGatewayMapper {

    public static Pedido toDomain(PedidoEntity entity) {
        return Pedido.reconstruir(
            entity.getMetodoPagamento(),
            entity.getFrete(),
            entity.getEndereco(),
            entity.getStatusPedido(),
            new HashSet<>(entity.getProdutos()),
            entity.getDataCriacao(),
            entity.getCliente(),
            entity.getReciboPagamento(),
            entity.getId()
        );
    }

    public static PedidoEntity toEntity(Pedido pedido) {
        return new PedidoEntity(
            pedido.getId(),
            pedido.getReciboPagamento(),
            pedido.getCliente(),
            pedido.getDataCriacao(),
            pedido.getProdutos().stream().toList(),
            pedido.getStatus(),
            pedido.getEnderecoEntrega(),
            pedido.getFrete(),
            pedido.getMetodoPagamento()
        );
    }
}
