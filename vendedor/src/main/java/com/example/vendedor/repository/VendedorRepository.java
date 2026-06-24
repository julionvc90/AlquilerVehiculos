package com.example.vendedor.repository;

import com.example.vendedor.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

    Optional<Vendedor> findByRut(String rut);

    Optional<Vendedor> findByEmail(String email);

    boolean existsByRut(String rut);
}
