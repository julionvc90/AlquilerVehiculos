package com.example.cliente.config;

import com.example.cliente.model.Cliente;
import com.example.cliente.repository.ClienteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository repository;

    public DataLoader(ClienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Cliente c = new Cliente();
            c.setRut((10_000_000 + r.nextInt(90_000_000)) + "-" + r.nextInt(10));
            c.setNombre(faker.name().firstName());
            c.setApellido(faker.name().lastName());
            c.setEmail(faker.internet().emailAddress());
            c.setTelefono("+569" + (10000000 + r.nextInt(90000000)));
            c.setDireccion(faker.address().fullAddress());
            c.setUsuarioId((long) (1 + r.nextInt(25)));
            c.setActivo(true);
            repository.save(c);
        }
    }
}
