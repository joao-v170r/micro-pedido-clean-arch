package br.com.microservice.pedido.schedule;

import br.com.microservice.pedido.usecase.ProcessamentoPedidoUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProcessamentoPedidosScheduleTest {

    @Mock
    private ProcessamentoPedidoUseCase useCase;

    @InjectMocks
    private ProcessamentoPedidosSchedule schedule;

    @Test
    void processaPedidos_shouldCallProcessamentoPedidosCriado() {
        // Act
        schedule.processaPedidos();

        // Assert
        verify(useCase, times(1)).processamentoPedidosCriado();
    }

    @Test
    void processaPedidos_shouldHaveScheduledAnnotation() throws NoSuchMethodException {
        // Arrange
        Method method = ProcessamentoPedidosSchedule.class.getMethod("processaPedidos");

        // Act
        Scheduled scheduledAnnotation = method.getAnnotation(Scheduled.class);

        // Assert
        assertNotNull(scheduledAnnotation);
        assertEquals(5000, scheduledAnnotation.fixedRate());
    }
}