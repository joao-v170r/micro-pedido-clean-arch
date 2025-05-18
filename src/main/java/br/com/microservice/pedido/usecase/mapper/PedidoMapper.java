package br.com.microservice.pedido.usecase.mapper;

import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.dto.PedidoDTO;

public class PedidoMapper {
    public static PedidoDTO toDTO(Pedido domain) {
        return new PedidoDTO(
            domain.getId(),
            domain.getReciboPagamento(),
            domain.getCliente(),
            domain.getDataCriacao(),
            domain.getProdutos(),
            domain.getStatus(),
            domain.getEnderecoEntrega(),
            domain.getFrete(),
            domain.getMetodoPagamento()
        );
    }

    public static Pedido toDomain(PedidoDTO dto) {
        return Pedido.reconstruir(
            dto.metodoPagamento(),
            dto.frete(),
            dto.enderecoEntrega(),
            dto.status(),
            dto.produtos(),
            dto.dataCriacao(),
            dto.cliente(),
            dto.reciboPagamento(),
            dto.id()
        );
    }
}
