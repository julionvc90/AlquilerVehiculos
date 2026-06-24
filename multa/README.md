# Microservicio: Multa

## Descripcion

Microservicio encargado de la gestion de multas del Sistema de Alquiler de Vehiculos. Permite registrar multas asociadas a una reserva (por ejemplo, por devolucion tardia o daños), validando que la reserva exista en el sistema.

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
src/main/java/com/example/multa/
├── MultaApplication.java
├── webclient/
│   └── ReservaClient.java        # Comunicacion con microservicio Reserva
├── controller/
│   └── MultaController.java      # Endpoints REST
├── dto/
│   ├── MultaRequestDTO.java      # DTO de entrada con validaciones Jakarta
│   ├── MultaResponseDTO.java     # DTO de salida
│   └── ReservaResponseDTO.java   # DTO para respuesta de Reserva
├── exception/
│   ├── MultaNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Multa.java                # Entidad JPA
├── repository/
│   └── MultaRepository.java      # JpaRepository con consultas custom
└── service/
    └── MultaService.java         # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/multa` | Listar todas las multas | `200 OK` |
| `GET` | `/api/multa/{id}` | Buscar multa por ID | `200 OK` / `404 Not Found` |
| `POST` | `/api/multa` | Crear nueva multa (valida reserva) | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/multa/{id}` | Actualizar multa existente | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/multa/{id}` | Eliminar multa | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "idReserva": 1,
  "idVehiculo": 1,
  "motivoMulta": "Devolucion tardia 2 dias",
  "montoMulta": 50000,
  "estadoMulta": "Pendiente"
}
```

---

## Modelo de Datos

### Tabla: `multas`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id_multa` | `BIGINT` | PK, AUTO_INCREMENT |
| `id_reserva` | `BIGINT` | NOT NULL |
| `id_vehiculo` | `BIGINT` | NOT NULL |
| `motivo_multa` | `VARCHAR(250)` | NOT NULL |
| `fecha_multa` | `DATE` | NOT NULL |
| `monto_multa` | `DECIMAL` | NOT NULL |
| `estado_multa` | `VARCHAR(20)` | Pendiente, Pagada, Anulada |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE multa_db;`
3. Ejecutar:
```bash
cd multa
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8090`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `reserva` | 8089 | WebClient | Validar existencia de la reserva |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas |
| `404 NOT FOUND` | Multa no encontrada |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8090
```
