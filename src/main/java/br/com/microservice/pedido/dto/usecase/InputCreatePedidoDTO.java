package br.com.microservice.pedido.dto.usecase;

import br.com.microservice.pedido.domain.value_objects.Endereco;
import br.com.microservice.pedido.domain.value_objects.MetodoPagamento;
import br.com.microservice.pedido.dto.ProdutoPedidoDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public record InputCreatePedidoDTO(
        String clienteId,
        LocalDateTime dataCriacao,
        HashMap<String, Integer> produtos,
        Endereco enderecoEntrega,
        BigDecimal frete,
        MetodoPagamento metodoPagamento
) {
}