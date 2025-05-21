package br.com.microservice.pedido.gateway.dto.output;

import br.com.microservice.pedido.gateway.dto.MetodoPagamento;
import br.com.microservice.pedido.gateway.dto.MoedaPagamento;
import br.com.microservice.pedido.gateway.dto.StatusPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;

public record OutputPagamentoDTO(
        String id,
        String pedidoId,
        BigDecimal valorTotal,
        MoedaPagamento moedaPagamento,
        MetodoPagamento metodoPagamento,
        String gatewayPagamento,
        HashMap<String, String> detalhes,
        StatusPagamento status,
        LocalDateTime dtCriacao,
        LocalDateTime dtAtualizacao,
        String codGateway
) {
}
