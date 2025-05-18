package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.OutputProdutoDTO;

public interface ProdutoGateway {
    OutputProdutoDTO findById(String id);
}
