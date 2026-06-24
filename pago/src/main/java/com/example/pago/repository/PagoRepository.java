package com.example.pago.repository;

import com.example.pago.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> findByIdReserva(Long idReserva);

    List<Pago> findByIdVehiculo(Long idVehiculo);

    List<Pago> findByEstadoPago(String estadoPago);
}
