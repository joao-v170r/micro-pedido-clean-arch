package br.com.microservice.pedido.gateway.dto;

import java.math.BigDecimal;

public record ProdutoDTO(
        String id,
        String nome,
        String sku,
        BigDecimal preco
) {
}
