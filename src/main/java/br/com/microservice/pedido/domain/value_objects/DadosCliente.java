package br.com.microservice.pedido.domain.value_objects;

public record DadosCliente(
        String idCliente,
        String nomeCliente,
        String cpf,
        String email
) {
    public DadosCliente {
        if(idCliente == null || idCliente.isBlank()) {
            throw new IllegalArgumentException("cliente invalido");
        }
        if(nomeCliente == null || nomeCliente.isBlank()) {
            throw new IllegalArgumentException("nome do cliente invalido");
        }
        if(cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("cpf esta invalido");
        }
        if(email == null || email.isBlank()) {
            throw new IllegalArgumentException("email esta invalido");
        }
    }
}
