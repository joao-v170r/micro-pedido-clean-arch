package br.com.microservice.pedido.gateway.dto;

import java.util.HashMap;

public record OutputPagamentoDTO(
        String id,
        String status,
        HashMap<String, String> detalhes,
        String moeda,
        String metodo
) {
}
