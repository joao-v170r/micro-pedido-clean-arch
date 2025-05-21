package br.com.microservice.pedido.gateway.dto.input;

import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;

import java.math.BigDecimal;

public record InputSolicitaPagamentoDTO(
        String clienteId,
        String pedidoId,
        MetodoPagamento metodoPagamento,
        BigDecimal valorTotal
) {
}
