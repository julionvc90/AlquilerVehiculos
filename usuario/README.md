# Microservicio: Usuario

## Descripcion

Microservicio encargado de la gestion de usuarios del Sistema de Alquiler de Vehiculos. Expone una API REST con operaciones CRUD completas, endpoint de verificacion de existencia para otros microservicios, validaciones de negocio, manejo centralizado de errores y trazabilidad mediante logs.

---

## Stack Tecnologico

| Componente | Version / Detalle |
|---|---|
| Lenguaje | Java 21 |
| Framework | Spring Boot 4.0.6 |
| Persistencia | Spring Data JPA + Hibernate |
| Base de Datos | MySQL |
| Driver | mysql-connector-j |
| Validaciones | Jakarta Bean Validation (JSR 380) |
| Comunicacion | Spring WebMVC (REST) |
| Utilidades | Lombok, SLF4J |
| Build | Maven |

---

## Arquitectura (Patron CSR)

```
src/main/java/com/example/usuario/
├── UsuarioApplication.java
├── controller/
│   └── UsuarioController.java    # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── UsuarioRequestDTO.java    # DTO de entrada con validaciones Jakarta
│   └── UsuarioResponseDTO.java   # DTO de salida (password NO incluida)
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java        # Estructura consistente de errores
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Usuario.java              # Entidad JPA
├── repository/
│   └── UsuarioRepository.java    # JpaRepository con consultas custom
└── service/
    └── UsuarioService.java       # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/usuarios` | Listar todos los usuarios | `200 OK` |
| `GET` | `/api/usuarios/{id}` | Buscar usuario por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/usuarios/{id}/existe` | Verificar si un usuario existe | `200 OK` (boolean) |
| `GET` | `/api/usuarios/username/{username}` | Buscar usuario por username | `200 OK` / `404 Not Found` |
| `POST` | `/api/usuarios` | Crear nuevo usuario | `201 Created` / `400 Bad Request` / `409 Conflict` |
| `PUT` | `/api/usuarios/{id}` | Actualizar usuario existente | `200 OK` / `400 Bad Request` / `404 Not Found` / `409 Conflict` |
| `DELETE` | `/api/usuarios/{id}` | Eliminar usuario | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "username": "jperez",
  "password": "secreto123",
  "email": "juan.perez@email.com",
  "rol": "CLIENTE"
}
```

### Ejemplo de Response (GET por ID)

```json
{
  "id": 300001,
  "username": "jperez",
  "email": "juan.perez@email.com",
  "rol": "CLIENTE",
  "activo": true
}
```

> **Nota:** El campo `password` nunca se incluye en las respuestas.

---

## Modelo de Datos

### Tabla: `usuario`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT (inicia en 300000) |
| `username` | `VARCHAR(50)` | UNIQUE, NOT NULL |
| `password` | `VARCHAR(255)` | NOT NULL |
| `email` | `VARCHAR(100)` | NOT NULL |
| `rol` | `VARCHAR(20)` | NOT NULL |
| `activo` | `BOOLEAN` | NOT NULL, default `true` |

### Roles

| Rol | Descripcion |
|---|---|
| `ADMIN` | Administrador del sistema |
| `CLIENTE` | Cliente que alquila vehiculos |
| `VENDEDOR` | Vendedor/empleado |

### Relaciones

- **`cliente.usuario_id`** y **`vendedor.usuario_id`** referencian el ID de este microservicio.

> **Nota:** Configurar el AUTO_INCREMENT en MySQL con: `ALTER TABLE usuario AUTO_INCREMENT = 300000;`

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar las credenciales de MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE usuario_db;`
3. Ejecutar:
```bash
cd usuario
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8082`

---

## Consumido por otros Microservicios

| Microservicio | Endpoint consumido | Via |
|---|---|---|
| `cliente` | `GET /api/usuarios/{id}/existe` | WebClient (puerto 8082) |
| `vendedor` | `GET /api/usuarios/{id}/existe` | WebClient (puerto 8082) |

---

## Manejo de Errores

| Codigo HTTP | Causa | Ejemplo |
|---|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas | Campo `username` vacio |
| `404 NOT FOUND` | Recurso no encontrado | Usuario con ID inexistente |
| `409 CONFLICT` | Violacion de regla de negocio | Username o email duplicado |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor | Falla de conexion a BD |

---

## Validaciones de Negocio

| Regla | Implementada en |
|---|---|
| Username unico | `UsuarioService.crear()` y `actualizar()` |
| Email unico | `UsuarioService.crear()` y `actualizar()` |
| Username, password, email, rol obligatorios | `UsuarioRequestDTO` via `@NotBlank` |
| Email con formato valido | `UsuarioRequestDTO` via `@Email` |
| Password NUNCA se expone en respuestas | `UsuarioResponseDTO` (sin campo password) |

---

## Configuracion de Puerto

```
server.port=8082
```
