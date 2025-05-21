package br.com.microservice.pedido.gateway.adapter;

import br.com.microservice.pedido.gateway.ClienteGateway;
import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;
import br.com.microservice.pedido.gateway.exception.adapter.ProdutoRestAdapterExeception;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
@RequiredArgsConstructor
public class ClienteRestAdapter implements ClienteGateway {

    private final WebClient clienteWebClient;

    @Override
    public OutputClienteDTO findById(String id) {
        return clienteWebClient.get()
                .uri("/cliente/{id}", id)
                .retrieve()
                .bodyToMono(OutputClienteDTO.class)
                .blockOptional()
                .orElseThrow(
                        () -> new ProdutoRestAdapterExeception.ProdutoNotFound("não foi possivel encontrar o produto")
                );
    }
}
