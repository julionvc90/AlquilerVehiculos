package com.example.disponibilidad.config;

import com.example.disponibilidad.model.Disponibilidad;
import com.example.disponibilidad.repository.DisponibilidadRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final DisponibilidadRepository repository;

    public DataLoader(DisponibilidadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Disponibilidad d = new Disponibilidad();
            d.setVehiculoId((long) (1 + r.nextInt(50)));
            LocalDate inicio = LocalDate.now().plusDays(r.nextInt(30));
            d.setFechaInicio(inicio);
            d.setFechaFin(inicio.plusDays(3 + r.nextInt(14)));
            d.setDisponible(r.nextBoolean());
            repository.save(d);
        }
    }
}
