package com.example.vehiculo.service;

import com.example.vehiculo.client.VendedorClient;
import com.example.vehiculo.model.Vehiculo;
import com.example.vehiculo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository repository;
    @Autowired
    private VendedorClient vendedorClient;

    public List<Vehiculo> listar() {
        return repository.findAll();
    }

    public Vehiculo buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));
    }

    public Vehiculo guardar(Vehiculo vehiculo){

        vendedorClient.obtenerVendedor(
                vehiculo.getVendedorId());

        return repository.save(vehiculo);
    }

    public Vehiculo actualizar(Long id, Vehiculo vehiculoActualizado) {

        Vehiculo vehiculo = buscarPorId(id);

        vehiculo.setPatente(vehiculoActualizado.getPatente());
        vehiculo.setMarca(vehiculoActualizado.getMarca());
        vehiculo.setModelo(vehiculoActualizado.getModelo());
        vehiculo.setAnio(vehiculoActualizado.getAnio());
        vehiculo.setTarifaDiaria(vehiculoActualizado.getTarifaDiaria());

        return repository.save(vehiculo);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}