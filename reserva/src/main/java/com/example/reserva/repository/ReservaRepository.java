package com.example.reserva.repository;

import com.example.reserva.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByIdReserva(Long idReserva);

    List<Reserva> findByIdVehiculo(Long idVehiculo);

    List<Reserva> findByEstadoReserva(String estadoReserva);
}