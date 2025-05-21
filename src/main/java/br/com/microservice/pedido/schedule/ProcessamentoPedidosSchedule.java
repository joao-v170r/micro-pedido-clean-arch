package br.com.microservice.pedido.schedule;

import br.com.microservice.pedido.usecase.ProcessamentoPedidoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProcessamentoPedidosSchedule {

    private final ProcessamentoPedidoUseCase useCase;

    public ProcessamentoPedidosSchedule(ProcessamentoPedidoUseCase useCase) {
        this.useCase = useCase;
    }

    @Scheduled(fixedRate = 5000)
    public void processaPedidos() {
        log.info("processando pedidos criados");
        useCase.processamentoPedidosCriado();
    }
}
