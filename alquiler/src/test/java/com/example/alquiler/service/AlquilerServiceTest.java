package com.example.alquiler.service;

import com.example.alquiler.dto.*;
import com.example.alquiler.exception.ResourceNotFoundException;
import com.example.alquiler.model.Alquiler;
import com.example.alquiler.model.EstadoAlquiler;
import com.example.alquiler.repository.AlquilerRepository;
import com.example.alquiler.webclient.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlquilerServiceTest {

    @Mock private AlquilerRepository alquilerRepository;
    @Mock private ClienteClient clienteClient;
    @Mock private VehiculoClient vehiculoClient;
    @Mock private DisponibilidadClient disponibilidadClient;
    @Mock private ReservaClient reservaClient;
    @Mock private PagoClient pagoClient;
    @InjectMocks private AlquilerService service;

    private Alquiler alquiler;
    private AlquilerRequestDTO dto;

    @BeforeEach
    void setUp() {
        alquiler = new Alquiler();
        alquiler.setId(1L);
        alquiler.setClienteId(1L);
        alquiler.setVehiculoId(1L);
        alquiler.setReservaId(1L);
        alquiler.setEstado(EstadoAlquiler.Reservado);
        alquiler.setMontoTotal(100000.0);
        alquiler.setFechaInicio(LocalDate.of(2025, 7, 1));
        alquiler.setFechaFin(LocalDate.of(2025, 7, 5));

        dto = new AlquilerRequestDTO();
        dto.setClienteId(1L);
        dto.setVehiculoId(1L);
        dto.setReservaId(1L);
        dto.setFechaInicio(LocalDate.of(2025, 7, 1));
        dto.setFechaFin(LocalDate.of(2025, 7, 5));

        // mocks comunes
        ReservaResponseDTO reserva = new ReservaResponseDTO();
        reserva.setEstadoReserva("Confirmada");

        PagoResponseDTO pago = new PagoResponseDTO();
        pago.setEstadoPago("Pagada");

        VehiculoResponseDTO vehiculo = new VehiculoResponseDTO();
        vehiculo.setTarifaDiaria(25000.0);
    }

    @Test @DisplayName("guardar - exitoso")
    void guardar_exitoso() {
        ReservaResponseDTO res = new ReservaResponseDTO();
        res.setEstadoReserva("Confirmada");
        PagoResponseDTO pag = new PagoResponseDTO();
        pag.setEstadoPago("Pagada");
        VehiculoResponseDTO veh = new VehiculoResponseDTO();
        veh.setTarifaDiaria(25000.0);

        when(reservaClient.obtenerReserva(1L)).thenReturn(res);
        when(pagoClient.buscarPagosPorReserva(1L)).thenReturn(List.of(pag));
        when(clienteClient.obtenerCliente(1L)).thenReturn(new ClienteResponseDTO());
        when(vehiculoClient.obtenerVehiculo(1L)).thenReturn(veh);
        when(disponibilidadClient.validarDisponibilidad(anyLong(), any(), any())).thenReturn(true);
        when(alquilerRepository.save(any())).thenReturn(alquiler);

        assertNotNull(service.guardar(dto));
    }

    @Test @DisplayName("guardar - reserva no confirmada")
    void guardar_reservaNoConfirmada() {
        ReservaResponseDTO res = new ReservaResponseDTO();
        res.setEstadoReserva("Pendiente");
        when(reservaClient.obtenerReserva(1L)).thenReturn(res);

        assertThrows(IllegalArgumentException.class, () -> service.guardar(dto));
    }

    @Test @DisplayName("iniciarAlquiler - exitoso")
    void iniciarAlquiler_exitoso() {
        alquiler.setEstado(EstadoAlquiler.Reservado);
        when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquiler));
        when(alquilerRepository.save(any())).thenReturn(alquiler);

        AlquilerResponseDTO r = service.iniciarAlquiler(1L);
        assertEquals(EstadoAlquiler.ACTIVO, r.getEstado());
    }

    @Test @DisplayName("iniciarAlquiler - estado invalido")
    void iniciarAlquiler_estadoInvalido() {
        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquiler));

        assertThrows(IllegalArgumentException.class, () -> service.iniciarAlquiler(1L));
    }

    @Test @DisplayName("finalizarAlquiler - exitoso")
    void finalizarAlquiler_exitoso() {
        alquiler.setEstado(EstadoAlquiler.ACTIVO);
        when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquiler));
        when(alquilerRepository.save(any())).thenReturn(alquiler);

        AlquilerResponseDTO r = service.finalizarAlquiler(1L);
        assertEquals(EstadoAlquiler.FINALIZADO, r.getEstado());
    }

    @Test @DisplayName("listar")
    void listar() {
        when(alquilerRepository.findAll()).thenReturn(List.of(alquiler));
        assertEquals(1, service.listar().size());
    }

    @Test @DisplayName("buscarPorId")
    void buscarPorId() {
        when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquiler));
        assertEquals(EstadoAlquiler.Reservado, service.buscarPorId(1L).getEstado());
    }

    @Test @DisplayName("eliminar")
    void eliminar() {
        when(alquilerRepository.findById(1L)).thenReturn(Optional.of(alquiler));
        service.eliminar(1L);
        verify(alquilerRepository).delete(alquiler);
    }
}
