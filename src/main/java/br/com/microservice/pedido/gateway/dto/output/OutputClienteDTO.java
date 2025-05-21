package br.com.microservice.pedido.gateway.dto.output;

import br.com.microservice.pedido.domain.value_objects.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record OutputClienteDTO(
        String id,
        String nome,
        String cpf,
        String email,
        LocalDate dataNascimento,
        Set<Endereco> enderecos,
        Set<Telefone> telefones
) {
}
