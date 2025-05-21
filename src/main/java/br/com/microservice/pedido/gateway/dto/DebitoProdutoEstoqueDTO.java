package br.com.microservice.pedido.gateway.dto;

public record DebitoProdutoEstoqueDTO(
        String sku,
        Integer quantidade
) {
}
