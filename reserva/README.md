# Microservicio: Reserva

## Descripcion

Microservicio encargado de la gestion de reservas del Sistema de Alquiler de Vehiculos. Permite crear reservas de vehiculos por parte de clientes, validando la existencia del cliente, del vehiculo y la disponibilidad en las fechas solicitadas.

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
src/main/java/com/example/reserva/
├── ReservaApplication.java
├── webclient/
│   ├── ClienteClient.java        # Comunicacion con microservicio Cliente
│   ├── VehiculoClient.java       # Comunicacion con microservicio Vehiculo
│   └── DisponibilidadClient.java # Comunicacion con microservicio Disponibilidad
├── controller/
│   └── ReservaController.java    # Endpoints REST
├── dto/
│   ├── ReservaRequestDTO.java    # DTO de entrada con validaciones Jakarta
│   ├── ReservaResponseDTO.java   # DTO de salida
│   ├── ClienteResponseDTO.java   # DTO para respuesta de Cliente
│   └── VehiculoResponseDTO.java  # DTO para respuesta de Vehiculo
├── exception/
│   ├── ReservaNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Reserva.java              # Entidad JPA
├── repository/
│   └── ReservaRepository.java    # JpaRepository
└── service/
    └── ReservaService.java       # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/reserva` | Listar todas las reservas | `200 OK` |
| `GET` | `/api/reserva/{id}` | Buscar reserva por ID | `200 OK` / `404 Not Found` |
| `POST` | `/api/reserva` | Crear nueva reserva (valida cliente, vehiculo y disponibilidad) | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/reserva/{id}` | Actualizar reserva existente | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/reserva/{id}` | Eliminar reserva | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "idCliente": 200001,
  "idVehiculo": 1,
  "fechaReserva": "2025-06-20",
  "fechaInicio": "2025-07-01",
  "fechaTermino": "2025-07-05",
  "totalDias": 4,
  "valorDia": 25000,
  "totalReserva": 100000,
  "estadoReserva": "Confirmada",
  "observacionesReserva": "Sin novedades"
}
```

---

## Modelo de Datos

### Tabla: `reservas`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id_reserva` | `BIGINT` | PK, AUTO_INCREMENT |
| `id_cliente` | `BIGINT` | NOT NULL |
| `id_vehiculo` | `BIGINT` | NOT NULL |
| `fecha_reserva` | `DATE` | NOT NULL |
| `fecha_inicio` | `DATE` | NOT NULL |
| `fecha_termino` | `DATE` | NOT NULL |
| `total_dias` | `INTEGER` | NOT NULL |
| `valor_dia` | `DECIMAL` | NOT NULL |
| `total_reserva` | `DECIMAL` | NOT NULL |
| `estado_reserva` | `VARCHAR(255)` | Pendiente, Confirmada, Cancelada, Anulada |
| `observaciones_reserva` | `VARCHAR(255)` | NOT NULL |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.yml`
2. Crear la base de datos: `CREATE DATABASE Reserva_db;`
3. Ejecutar:
```bash
cd reserva
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:9099`
5. Swagger UI: `http://localhost:9099/doc/swagger-ui/index.html`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `cliente` | 9092 | WebClient | Validar existencia del cliente |
| `vehiculo` | 9094 | WebClient | Obtener datos del vehiculo |
| `disponibilidad` | 9095 | WebClient | Validar disponibilidad del vehiculo |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas o regla de negocio |
| `404 NOT FOUND` | Reserva no encontrada |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=9099
```
