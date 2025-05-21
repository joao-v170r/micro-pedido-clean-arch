package br.com.microservice.pedido.controller;


import br.com.microservice.pedido.domain.Pedido;
import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.usecase.ReadPedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("pedido")
@Tag(name = "Pedido", description = "Endpoints reponsavel pela criação e gerenciamento de pedidos")
public class ReadPedidoController {

    private final ReadPedidoUseCase useCase;

    public ReadPedidoController(ReadPedidoUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    @Operation(
            summary = "lista todos os pedidos registrados"
    )
    public ResponseEntity<List<PedidoDTO>> findAll(@PageableDefault(size = 10, page = 0, sort = "id") Pageable pageable) {
        if(pageable == null) {
            return ResponseEntity.ok(useCase.findAll());
        }
        return ResponseEntity.ok(useCase.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "busca um pedido pelo id"
    )
    private ResponseEntity<PedidoDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(useCase.findById(id));
    }
}
