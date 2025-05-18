package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.OutputClienteDTO;

public interface ClienteGateway {
    OutputClienteDTO findById(String id);
}
