package br.com.microservice.pedido.domain.value_objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoPedidoTest {

    @Test
    void deveCriarProdutoPedidoQuandoDadosValidos() {
        // Act & Assert
        assertDoesNotThrow(() -> new ProdutoPedido(
                "123",
                BigDecimal.TEN,
                1
        ));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void deveLancarExcecaoQuandoIdProdutoInvalido(String idProduto) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ProdutoPedido(
                        idProduto,
                        BigDecimal.TEN,
                        1
                )
        );
        assertEquals("id do produto invalido", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoNulo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ProdutoPedido(
                        "123",
                        null,
                        1
                )
        );
        assertEquals("preco do produto invalido", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPrecoNegativo() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ProdutoPedido(
                        "123",
                        BigDecimal.valueOf(-1),
                        1
                )
        );
        assertEquals("preco do produto invalido", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -10})
    void deveLancarExcecaoQuandoQuantidadeInvalida(int quantidade) {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new ProdutoPedido(
                        "123",
                        BigDecimal.TEN,
                        quantidade
                )
        );
        assertEquals("quantidade do produto não pode ser menor que 1", exception.getMessage());
    }
}