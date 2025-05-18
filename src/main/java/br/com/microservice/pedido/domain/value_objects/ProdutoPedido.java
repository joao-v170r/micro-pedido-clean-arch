package br.com.microservice.pedido.domain.value_objects;

import java.math.BigDecimal;

public record ProdutoPedido(
        String idProduto,
        BigDecimal preco,
        int quantidade
) {
    public ProdutoPedido {
        if(idProduto == null || idProduto.isBlank()) {
            throw new IllegalArgumentException("id do produto invalido");
        }

        if(preco == null || preco.doubleValue() < 0) {
            throw new IllegalArgumentException("preco do produto invalido");
        }

        if(quantidade < 1) {
            throw new IllegalArgumentException("quantidade do produto não pode ser menor que 1");
        }
    }
}
