package br.com.microservice.pedido.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EnderecoTest {

    @Test
    void deveCriarEnderecoQuandoDadosValidos() {
        // Act & Assert
        assertDoesNotThrow(() -> new Endereco(
                "12345-678",
                "Rua Exemplo, 123",
                10,
                20
        ));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "12345678", "1234-5678", "abcde-fgh"})
    void deveLancarExcecaoQuandoCepInvalido(String cep) {
        // Act & Assert
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

    @Test
    void deveLancarExcecaoQuandoEnderecoCompletoVazio() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Endereco(
                        "12345-678",
                        "",
                        10,
                        20
                )
        );
        assertEquals("endereço completo e obrigatorio", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoLatitudeNula() {
        // Act & Assert
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
        // Act & Assert
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

    @Test
    void validCEP_deveRetornarTrueParaCEPValidoComHifen() {
        // Arrange
        Endereco endereco = new Endereco("12345-678", "Rua Teste", 1, 1);
        // Act & Assert
        assertTrue(endereco.validCEP("12345-678"));
    }

    @Test
    void validCEP_deveRetornarTrueParaCEPValidoSemHifen() {
        // Arrange
        Endereco endereco = new Endereco("12345-678", "Rua Teste", 1, 1);
        // Act & Assert
        assertTrue(endereco.validCEP("12345678"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "12345-67", "12345-6789", "ABCDE-FGH", "12345 678"})
    void validCEP_deveRetornarFalseParaCEPInvalido(String cep) {
        // Arrange
        Endereco endereco = new Endereco("12345-678", "Rua Teste", 1, 1);
        // Act & Assert
        assertFalse(endereco.validCEP(cep));
    }
}