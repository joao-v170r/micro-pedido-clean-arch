package br.com.microservice.pedido.controller;

import br.com.microservice.pedido.dto.PedidoDTO;
import br.com.microservice.pedido.dto.rest_controller.RequestCreatePedidoController;
import br.com.microservice.pedido.dto.usecase.InputCreatePedidoDTO;
import br.com.microservice.pedido.usecase.CreatePedidoUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("create-pedido")
@Tag(name = "Pedido", description = "Endpoints reponsavel pela criação e gerenciamento de pedidos")
public class CreatePedidoController {
    final CreatePedidoUseCase useCase;

    public CreatePedidoController(CreatePedidoUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    @Operation(
            summary = "Cria um pedido"
    )
    public ResponseEntity<PedidoDTO> create(@Valid @RequestBody RequestCreatePedidoController request) {
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(
                useCase.create(
                    new InputCreatePedidoDTO(
                            request.idCliente(),
                            request.dataCriacao(),
                            request.produtos(),
                            request.enderecoEntrega(),
                            request.frete(),
                            request.metodoPagamento()
                    )
                )
        );
    }
}
