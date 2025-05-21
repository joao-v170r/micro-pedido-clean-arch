package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;

public interface PagamentoGateway {
    OutputPagamentoDTO solicitaPagamento(InputSolicitaPagamentoDTO input);
    OutputPagamentoDTO processaPagamento(String id);
}
