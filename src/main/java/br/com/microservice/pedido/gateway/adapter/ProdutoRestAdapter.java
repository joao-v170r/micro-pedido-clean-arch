package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.ProdutoGateway;
import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputProdutoDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ProdutoRestAdapter implements ProdutoGateway {

    private final WebClient produtoWebClient;

    @Override
    public List<ProdutoDTO> list(List<String> ids) {
        return produtoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/produto/listar")
                        .queryParam("sku", ids) // ou "id" dependendo do que o endpoint espera
                        .build())
                .retrieve()
                .bodyToFlux(ProdutoDTO.class)
                .collectList()
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception
                                .ProdutoNotFound("não foi possivel processar o pagamento")
                );
    }
}
