package com.example.usuario.client;

import org.springframework.stereotype.Component;

@Component
public class UsuarioClient {
    // Este microservicio es consumido por otros (cliente, vendedor).
    // El endpoint GET /api/usuarios/{id}/existe permite verificar existencia.
    // No se requiere comunicacion saliente en este momento.
}
