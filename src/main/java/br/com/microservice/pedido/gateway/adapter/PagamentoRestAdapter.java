package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.PagamentoGateway;

import br.com.microservice.pedido.gateway.dto.input.InputSolicitaPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;

import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class PagamentoRestAdapter implements PagamentoGateway {

    private final WebClient pagamentoWebClient;

    @Override
    public OutputPagamentoDTO solicitaPagamento(InputSolicitaPagamentoDTO input) {
        return pagamentoWebClient.post()
                .uri("/solicita-pagamento")
                .contentType(MediaType.APPLICATION_JSON)  // Define o content-type
                .bodyValue(input)
                .retrieve()
                .bodyToMono(OutputPagamentoDTO.class)
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception
                                .ProdutoNotFound("não foi possivel processar o pagamento")
                );
    }

    @Override
    public OutputPagamentoDTO processaPagamento(String id) {
        return pagamentoWebClient.post()
                .uri("/processa-pagamento/{id}",id)
                .retrieve()
                .bodyToMono(OutputPagamentoDTO.class)
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception
                                .ProdutoNotFound("não foi possivel processar o pagamento")
                );
    }
}
