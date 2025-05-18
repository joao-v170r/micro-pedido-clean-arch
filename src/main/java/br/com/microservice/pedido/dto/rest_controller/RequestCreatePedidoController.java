package br.com.microservice.pedido.dto.rest_controller;

import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record RequestCreatePedidoController(
        String idCliente,
        LocalDateTime dataCriacao,
        Set<String> produtos,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
) {
}
