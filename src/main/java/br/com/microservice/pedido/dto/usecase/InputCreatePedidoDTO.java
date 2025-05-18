package br.com.microservice.pedido.dto.usecase;

import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.domain.value_objects.ProdutoPedido;
import br.com.microservice.pedido.domain.value_objects.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record InputCreatePedidoDTO(
        String idCliente,
        LocalDateTime dataCriacao,
        Set<String> produtos,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
) {
}