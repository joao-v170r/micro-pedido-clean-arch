package br.com.microservice.pedido.dto;

import br.com.microservice.pedido.domain.value_objects.*;
import br.com.microservice.pedido.gateway.dto.ProdutoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record PedidoDTO(
        String id,
        String reciboPagamento,
        DadosCliente cliente,
        LocalDateTime dataCriacao,
        Set<ProdutoPedido>produtos,
        StatusPedido status,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
) {
}
