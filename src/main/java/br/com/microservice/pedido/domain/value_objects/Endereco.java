package br.com.microservice.pedido.domain.value_objects;

import java.util.Objects;

public record Endereco(
        String cep,
        String enderecoCompleto,
        Integer latitude,
        Integer longitude
){
    public Endereco {
        if(cep.isEmpty() || !validCEP(cep)) {
            throw new IllegalArgumentException("cep está invalido");
        }
        if(enderecoCompleto.isEmpty()){
            throw new IllegalArgumentException("endereço completo e obrigatorio");
        }
        Objects.requireNonNull(latitude, "latitude é obrigatorio");
        Objects.requireNonNull(latitude, "longitude é obrigatorio");
    }

    public Boolean validCEP(String cep) {
        return cep.matches("^[0-9]{5}-?[0-9]{3}$");
    }

}