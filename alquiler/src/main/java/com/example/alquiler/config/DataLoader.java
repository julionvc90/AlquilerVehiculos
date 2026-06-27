package com.example.alquiler.config;

import com.example.alquiler.model.Alquiler;
import com.example.alquiler.model.EstadoAlquiler;
import com.example.alquiler.repository.AlquilerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final AlquilerRepository repository;
    private static final EstadoAlquiler[] ESTADOS = EstadoAlquiler.values();

    public DataLoader(AlquilerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Alquiler a = new Alquiler();
            a.setClienteId((long) (1 + r.nextInt(50)));
            a.setVehiculoId((long) (1 + r.nextInt(50)));
            a.setReservaId((long) (1 + r.nextInt(50)));
            LocalDate inicio = LocalDate.now().minusDays(30 + r.nextInt(30));
            a.setFechaInicio(inicio);
            int dias = 1 + r.nextInt(14);
            a.setFechaFin(inicio.plusDays(dias));
            a.setDias(dias);
            double tarifa = 15000 + r.nextInt(35001);
            a.setTarifaDiaria(tarifa);
            a.setMontoTotal(tarifa * dias);
            a.setEstado(ESTADOS[r.nextInt(ESTADOS.length)]);
            repository.save(a);
        }
    }
}
