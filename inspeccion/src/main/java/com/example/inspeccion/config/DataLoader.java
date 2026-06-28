package com.example.inspeccion.config;

import com.example.inspeccion.model.Inspeccion;
import com.example.inspeccion.repository.InspeccionRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final InspeccionRepository repository;
    private static final String[] TIPOS = {"Entrega", "Devolucion"};
    private static final String[] RESULTADOS = {"Aprobado", "Rechazado", "Aprobado c/obs"};

    public DataLoader(InspeccionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Inspeccion ins = new Inspeccion();
            ins.setAlquilerId((long) (1 + r.nextInt(50)));
            ins.setVehiculoId((long) (1 + r.nextInt(50)));
            ins.setFechaInspeccion(LocalDateTime.now().minusDays(r.nextInt(60)));
            ins.setTipoInspeccion(TIPOS[r.nextInt(TIPOS.length)]);
            ins.setResultado(RESULTADOS[r.nextInt(RESULTADOS.length)]);
            ins.setObservaciones(faker.lorem().sentence());
            ins.setInspector(faker.name().fullName());
            ins.setActivo(true);
            repository.save(ins);
        }
    }
}
