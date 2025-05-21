package br.com.microservice.pedido.gateway.dto;

public record ProdutoEstoqueDTO(
        String id,
        String sku,
        Integer quantidade
) {
}
