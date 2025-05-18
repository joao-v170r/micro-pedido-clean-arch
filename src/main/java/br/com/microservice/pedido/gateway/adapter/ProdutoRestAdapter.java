package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.ProdutoGateway;
import br.com.microservice.pedido.gateway.dto.OutputProdutoDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;


@Component
@RequiredArgsConstructor
public class ProdutoRestAdapter implements ProdutoGateway {

    @Override
    public OutputProdutoDTO findById(String id) {
        return null;/*clienteWebClient.get()
                .uri("/produtos/{id}", id)
                .retrieve()
                .bodyToMono(OutputProdutoDTO.class)
                .timeout(Duration.ofSeconds(5))
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception.ProdutoNotFound("não foi possivel encontrar o produto")
                );*/
    }
}
