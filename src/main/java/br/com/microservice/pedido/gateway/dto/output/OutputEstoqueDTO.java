package br.com.microservice.pedido.gateway.dto.output;

import br.com.microservice.pedido.gateway.dto.ProdutoEstoqueDTO;

import java.util.List;

public record OutputEstoqueDTO(
       List<ProdutoEstoqueDTO> produtos
) {
}
