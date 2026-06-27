package com.example.multa.config;

import com.example.multa.model.Multa;
import com.example.multa.repository.MultaRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final MultaRepository repository;
    private static final String[] ESTADOS = {"Pendiente", "Pagada", "Anulada"};

    public DataLoader(MultaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Multa m = new Multa();
            m.setIdReserva((long) (1 + r.nextInt(50)));
            m.setIdVehiculo((long) (1 + r.nextInt(50)));
            m.setMotivoMulta(faker.lorem().sentence(4));
            m.setMontoMulta(BigDecimal.valueOf(5000 + r.nextInt(95001)));
            m.setFechaMulta(LocalDate.now().minusDays(r.nextInt(60)));
            m.setEstadoMulta(ESTADOS[r.nextInt(ESTADOS.length)]);
            repository.save(m);
        }
    }
}
