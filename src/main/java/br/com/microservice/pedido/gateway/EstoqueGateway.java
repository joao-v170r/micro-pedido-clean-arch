package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.DebitoProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.ProdutoEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.input.InputReduzirEstoqueDTO;
import br.com.microservice.pedido.gateway.dto.output.OutputEstoqueDTO;

import java.util.List;

public interface EstoqueGateway {
    List<ProdutoEstoqueDTO> reduzir(InputReduzirEstoqueDTO input);
}