package br.com.microservice.pedido.gateway.dto.output;

import br.com.microservice.pedido.gateway.dto.ProdutoDTO;

import java.util.List;

public record OutputProdutoDTO(
        List<ProdutoDTO> produtos
) {
}
