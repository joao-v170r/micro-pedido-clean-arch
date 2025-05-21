package br.com.microservice.pedido.gateway.dto.input;

import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;

import java.util.List;

public record InputReduzirEstoqueDTO(
        List<DebitoProdutoEstoqueDTO> produtos
) {
}
