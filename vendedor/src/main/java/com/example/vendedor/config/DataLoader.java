package com.example.vendedor.config;

import com.example.vendedor.model.Vendedor;
import com.example.vendedor.repository.VendedorRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final VendedorRepository repository;

    public DataLoader(VendedorRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();
        Random r = new Random();

        for (int i = 0; i < 50; i++) {
            Vendedor v = new Vendedor();
            v.setRut((10_000_000 + r.nextInt(90_000_000)) + "-" + r.nextInt(10));
            v.setNombre(faker.name().firstName());
            v.setApellido(faker.name().lastName());
            v.setEmail(faker.internet().emailAddress());
            v.setTelefono("+569" + (10000000 + r.nextInt(90000000)));
            v.setUsuarioId((long) (1 + r.nextInt(25) + 25));
            v.setActivo(true);
            repository.save(v);
        }
    }
}
