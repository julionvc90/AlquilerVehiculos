package com.example.pago.config;

import com.example.pago.model.Pago;
import com.example.pago.repository.PagoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final PagoRepository repository;
    private static final String[] METODOS = {"Efectivo", "Tarjeta", "Transferencia", "WebPay"};
    private static final String[] ESTADOS = {"Ingresada", "Pagada", "Pendiente"};

    public DataLoader(PagoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Pago p = new Pago();
            p.setIdReserva((long) (1 + r.nextInt(50)));
            p.setIdVehiculo((long) (1 + r.nextInt(50)));
            p.setFechaPago(LocalDate.now().minusDays(r.nextInt(60)));
            p.setMontoPago(BigDecimal.valueOf(20000 + r.nextInt(480001)).setScale(2, RoundingMode.HALF_UP));
            p.setMetodoPago(METODOS[r.nextInt(METODOS.length)]);
            p.setEstadoPago(ESTADOS[r.nextInt(ESTADOS.length)]);
            p.setTransaccionPago("TXN" + String.format("%05d", r.nextInt(100000)));
            repository.save(p);
        }
    }
}
