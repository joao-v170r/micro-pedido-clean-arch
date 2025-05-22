package br.com.microservice.pedido.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void deveCriarEnderecoQuandoDadosValidos() {
        assertDoesNotThrow(() -> new Endereco(
                "12345-678",
                "Rua Exemplo, 123",
                10,
                20
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123456", "1234-567", "abcde-fgh", "12345-abc"})
    void deveLancarExcecaoQuandoCepInvalido(String cep) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Endereco(
                        cep,
                        "Rua Exemplo, 123",
                        10,
                        20
                )
        );
        assertEquals("cep está invalido", exception.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoEnderecoCompletoInvalido(String endereco) {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Endereco(
                        "12345-678",
                        endereco,
                        10,
                        20
                )
        );
        assertEquals("endereço completo e obrigatorio", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoLatitudeNula() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Endereco(
                        "12345-678",
                        "Rua Exemplo, 123",
                        null,
                        20
                )
        );
        assertEquals("latitude é obrigatorio", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoLongitudeNula() {
        NullPointerException exception = assertThrows(
                NullPointerException.class,
                () -> new Endereco(
                        "12345-678",
                        "Rua Exemplo, 123",
                        10,
                        null
                )
        );
        assertEquals("longitude é obrigatorio", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345-678", "12345678"})
    void deveValidarCEPsValidos(String cep) {
        Endereco endereco = new Endereco("12345-678", "Rua Teste", 1, 1);
        assertTrue(endereco.validCEP(cep));
    }
}