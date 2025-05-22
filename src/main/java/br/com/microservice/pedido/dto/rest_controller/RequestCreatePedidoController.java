package br.com.microservice.pedido.dto.rest_controller;

import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public record RequestCreatePedidoController(
        String clienteId,
        LocalDateTime dataCriacao,
        HashMap<String,Integer> produtos,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
) {
}