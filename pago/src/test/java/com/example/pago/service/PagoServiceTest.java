package com.example.pago.service;

import com.example.pago.dto.PagoRequestDTO;
import com.example.pago.dto.ReservaResponseDTO;
import com.example.pago.exception.PagoNotFoundException;
import com.example.pago.model.Pago;
import com.example.pago.repository.PagoRepository;
import com.example.pago.webclient.ReservaClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock private PagoRepository repository;
    @Mock private ReservaClient reservaClient;
    @InjectMocks private PagoService service;

    private Pago pago;
    private PagoRequestDTO dto;

    @BeforeEach
    void setUp() {
        pago = new Pago();
        pago.setIdPago(1L);
        pago.setIdReserva(1L);
        pago.setIdVehiculo(1L);
        pago.setMontoPago(new BigDecimal("100000"));
        pago.setMetodoPago("Transferencia");
        pago.setEstadoPago("Pagada");

        dto = new PagoRequestDTO();
        dto.setIdReserva(1L);
        dto.setIdVehiculo(1L);
        dto.setMontoPago(new BigDecimal("100000"));
        dto.setMetodoPago("Transferencia");
        dto.setEstadoPago("Pagada");
        dto.setFechaPago(LocalDate.now());
        dto.setTransaccionPago("TXN001");
    }

    @Test @DisplayName("crearPago - exitoso")
    void crearPago_exitoso() {
        when(reservaClient.obtenerReserva(1L)).thenReturn(new ReservaResponseDTO());
        when(repository.save(any())).thenReturn(pago);
        assertNotNull(service.crearPago(dto));
    }

    @Test @DisplayName("crearPago - reserva no existe")
    void crearPago_reservaNoExiste() {
        when(reservaClient.obtenerReserva(1L)).thenThrow(new RuntimeException("No existe"));
        assertThrows(RuntimeException.class, () -> service.crearPago(dto));
    }

    @Test @DisplayName("obtenerPorId")
    void obtenerPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        assertEquals("Pagada", service.obtenerPorId(1L).getEstadoPago());
    }

    @Test @DisplayName("obtenerPorId - no encontrado")
    void obtenerPorId_noEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PagoNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test @DisplayName("listar")
    void listar() {
        when(repository.findAll()).thenReturn(List.of(pago));
        assertEquals(1, service.listarTodas().size());
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(repository.findById(1L)).thenReturn(Optional.of(pago));
        service.eliminarPago(1L);
        verify(repository).delete(pago);
    }
}
