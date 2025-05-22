package br.com.microservice.pedido.domain.value_objects;

import java.util.Objects;

public record Endereco(
        String cep,
        String enderecoCompleto,
        Integer latitude,
        Integer longitude
) {
    public Endereco {
        if (cep == null || cep.trim().isEmpty() || !validCEP(cep)) {
            throw new IllegalArgumentException("cep está invalido");
        }
        if (enderecoCompleto == null || enderecoCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("endereço completo e obrigatorio");
        }
        Objects.requireNonNull(latitude, "latitude é obrigatorio");
        Objects.requireNonNull(longitude, "longitude é obrigatorio");
    }

    public Boolean validCEP(String cep) {
        return cep.matches("^[0-9]{5}-?[0-9]{3}$");
    }
}