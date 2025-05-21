package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.ProdutoDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputProdutoDTO;

import java.util.List;

public interface ProdutoGateway {
    List<ProdutoDTO> list(List<String> ids);
}
