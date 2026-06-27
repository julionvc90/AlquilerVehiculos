package com.example.usuario.config;

import com.example.usuario.model.Usuario;
import com.example.usuario.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository repository;

    public DataLoader(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return;

        Faker faker = new Faker();

        for (int i = 0; i < 50; i++) {
            Usuario u = new Usuario();
            u.setUsername(faker.internet().username());
            u.setPassword(faker.internet().password(6, 12));
            u.setEmail(faker.internet().emailAddress());
            u.setRol(i < 25 ? "CLIENTE" : "VENDEDOR");
            u.setActivo(true);
            repository.save(u);
        }
    }
}
