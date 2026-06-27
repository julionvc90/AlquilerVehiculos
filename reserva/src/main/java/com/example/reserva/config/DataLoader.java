package com.example.reserva.config;

import com.example.reserva.model.Reserva;
import com.example.reserva.repository.ReservaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final ReservaRepository repository;
    private static final String[] ESTADOS = {"Pendiente", "Confirmada", "Cancelada"};

    public DataLoader(ReservaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Reserva res = new Reserva();
            res.setIdCliente((long) (1 + r.nextInt(50)));
            res.setIdVehiculo((long) (1 + r.nextInt(50)));
            res.setFechaReserva(LocalDate.now().minusDays(r.nextInt(30)));
            LocalDate inicio = LocalDate.now().plusDays(r.nextInt(30));
            res.setFechaInicio(inicio);
            int dias = 1 + r.nextInt(14);
            res.setFechaTermino(inicio.plusDays(dias));
            res.setTotalDias(dias);
            BigDecimal valorDia = BigDecimal.valueOf(15000 + r.nextInt(35001));
            res.setValorDia(valorDia);
            res.setTotalReserva(valorDia.multiply(BigDecimal.valueOf(dias)));
            res.setEstadoReserva(ESTADOS[r.nextInt(ESTADOS.length)]);
            res.setObservacionesReserva("Reserva generada por DataFaker");
            repository.save(res);
        }
    }
}
