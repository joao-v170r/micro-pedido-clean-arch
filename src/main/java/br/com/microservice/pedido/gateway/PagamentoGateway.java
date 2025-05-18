package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.OutputPagamentoDTO;

public interface PagamentoGateway {
    OutputPagamentoDTO solicitaPagamento(InputSolicitaPagamentoDTO input);
}
