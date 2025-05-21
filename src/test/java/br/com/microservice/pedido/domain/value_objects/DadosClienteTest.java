package br.com.microservice.pedido.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

class DadosClienteTest {

    @Test
    void deveCriarDadosClienteQuandoDadosValidos() {
        // Act & Assert
        assertDoesNotThrow(() -> new DadosCliente(
                "123",
                "João Silva",
                "12345678900",
                "joao@email.com"
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoIdClienteInvalido(String idCliente) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DadosCliente(
                        idCliente,
                        "João Silva",
                        "12345678900",
                        "joao@email.com"
                )
        );
        assertEquals("cliente invalido", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoNomeClienteInvalido(String nomeCliente) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DadosCliente(
                        "123",
                        nomeCliente,
                        "12345678900",
                        "joao@email.com"
                )
        );
        assertEquals("nome do cliente invalido", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoCpfInvalido(String cpf) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DadosCliente(
                        "123",
                        "João Silva",
                        cpf,
                        "joao@email.com"
                )
        );
        assertEquals("cpf esta invalido", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoEmailInvalido(String email) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new DadosCliente(
                        "123",
                        "João Silva",
                        "12345678900",
                        email
                )
        );
        assertEquals("email esta invalido", exception.getMessage());
    }
}