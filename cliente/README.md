# Microservicio: Cliente

## Descripcion

Microservicio encargado de la gestion de clientes del Sistema de Alquiler de Vehiculos. Expone una API REST con operaciones CRUD completas, validaciones de negocio, manejo centralizado de errores y trazabilidad mediante logs.

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
| Comunicacion | Spring WebMVC (REST) + WebClient (WebFlux) |
| Utilidades | Lombok, SLF4J |
| Build | Maven |

---

## Arquitectura (Patron CSR)

```
src/main/java/com/example/cliente/
├── ClienteApplication.java
├── webclient/
│   └── UsuarioClient.java        # Comunicacion con microservicio Usuario
├── controller/
│   └── ClienteController.java    # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── ClienteRequestDTO.java    # DTO de entrada con validaciones Jakarta
│   └── ClienteResponseDTO.java   # DTO de salida
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java        # Estructura consistente de errores
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Cliente.java              # Entidad JPA
├── repository/
│   └── ClienteRepository.java    # JpaRepository con consultas custom
└── service/
    └── ClienteService.java       # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/clientes` | Listar todos los clientes | `200 OK` |
| `GET` | `/api/clientes/{id}` | Buscar cliente por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/clientes/rut/{rut}` | Buscar cliente por RUT | `200 OK` / `404 Not Found` |
| `POST` | `/api/clientes` | Crear nuevo cliente | `201 Created` / `400 Bad Request` / `409 Conflict` |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente existente | `200 OK` / `400 Bad Request` / `404 Not Found` / `409 Conflict` |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "rut": "12.345.678-9",
  "nombre": "Juan",
  "apellido": "Perez",
  "email": "juan.perez@email.com",
  "telefono": "+56912345678",
  "direccion": "Av. Siempre Viva 742",
  "usuarioId": 300001
}
```

---

## Modelo de Datos

### Tabla: `cliente`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT (inicia en 200000) |
| `rut` | `VARCHAR(12)` | UNIQUE, NOT NULL |
| `nombre` | `VARCHAR(50)` | NOT NULL |
| `apellido` | `VARCHAR(50)` | NOT NULL |
| `email` | `VARCHAR(100)` | NOT NULL |
| `telefono` | `VARCHAR(15)` | Opcional |
| `direccion` | `VARCHAR(200)` | Opcional |
| `usuario_id` | `BIGINT` | FK logica → microservicio Usuario |
| `activo` | `BOOLEAN` | NOT NULL, default `true` |

### Relaciones

- **`usuario_id`**: Referencia logica al microservicio `usuario`. La verificacion de existencia se realiza via `UsuarioClient` (WebClient) al puerto `9091`.

> **Nota**: Configurar el AUTO_INCREMENT en MySQL con: `ALTER TABLE cliente AUTO_INCREMENT = 200000;`

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar las credenciales de MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE cliente_db;`
3. Ejecutar:
```bash
cd cliente
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:9092`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `usuario` | 9091 | WebClient | Verificar existencia de usuario por ID |

---

## Manejo de Errores

| Codigo HTTP | Causa | Ejemplo |
|---|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas | Campo `email` con formato invalido |
| `404 NOT FOUND` | Recurso no encontrado | Cliente con ID inexistente |
| `409 CONFLICT` | Violacion de regla de negocio | RUT duplicado al crear/actualizar |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor | Falla de conexion a BD |

---

## Validaciones de Negocio

| Regla | Implementada en |
|---|---|
| RUT unico (no pueden existir 2 clientes con el mismo RUT) | `ClienteService.crear()` y `ClienteService.actualizar()` |
| RUT, nombre, apellido, email obligatorios | `ClienteRequestDTO` via `@NotBlank` |
| Email con formato valido | `ClienteRequestDTO` via `@Email` |
| Longitud maxima de campos | `ClienteRequestDTO` via `@Size` |

---

## Configuracion de Puerto

```
server.port=9092
```
