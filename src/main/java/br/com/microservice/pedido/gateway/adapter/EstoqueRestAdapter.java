package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.EstoqueGateway;
import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.ProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputPagamentoDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EstoqueRestAdapter implements EstoqueGateway {
    private final WebClient estoqueWebClient;

    @Override
    public List<ProdutoEstoqueDTO> reduzir(InputReduzirEstoqueDTO input) {
        return estoqueWebClient.put()
                .uri("/update-estoque-produto")
                .contentType(MediaType.APPLICATION_JSON)  // Define o content-type
                .bodyValue(input)
                .retrieve()
                .bodyToFlux(ProdutoEstoqueDTO.class)
                .collectList()
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception
                                .ProdutoNotFound("não foi possivel processar o pagamento")
                );
    }
}
