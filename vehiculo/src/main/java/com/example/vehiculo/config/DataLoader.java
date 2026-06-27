package com.example.vehiculo.config;

import com.example.vehiculo.model.Vehiculo;
import com.example.vehiculo.repository.VehiculoRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final VehiculoRepository repository;
    private static final String[] MARCAS = {"Toyota", "Chevrolet", "Hyundai", "Kia", "Nissan", "Mazda"};
    private static final String[] MODELOS = {"Corolla", "Yaris", "Sail", "Accent", "Rio", "Versa", "Mazda3"};
    private static final String[] CATEGORIAS = {"Sedan", "SUV", "Hatchback", "Pickup"};
    private static final String[] COLORES = {"Rojo", "Azul", "Negro", "Blanco", "Gris", "Verde"};

    public DataLoader(VehiculoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Vehiculo v = new Vehiculo();
            v.setPatente(String.format("%c%c%04d",
                    (char) ('A' + r.nextInt(26)), (char) ('A' + r.nextInt(26)),
                    r.nextInt(10000)));
            v.setMarca(MARCAS[r.nextInt(MARCAS.length)]);
            v.setModelo(MODELOS[r.nextInt(MODELOS.length)]);
            v.setAnio(2015 + r.nextInt(11));
            v.setCategoria(CATEGORIAS[r.nextInt(CATEGORIAS.length)]);
            v.setCapacidadPasajeros(String.valueOf(2 + r.nextInt(6)));
            v.setColor(COLORES[r.nextInt(COLORES.length)]);
            v.setVendedorId((long) (1 + r.nextInt(50)));
            v.setTarifaDiaria(15000.0 + r.nextInt(35001));
            v.setUbicacion(faker.address().city());
            v.setActivo(true);
            repository.save(v);
        }
    }
}
