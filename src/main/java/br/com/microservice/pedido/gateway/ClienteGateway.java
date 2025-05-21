package br.com.microservice.pedido.gateway;

import br.com.microservice.pedido.gateway.dto.output.OutputClienteDTO;

public interface ClienteGateway {
    OutputClienteDTO findById(String id);
}
