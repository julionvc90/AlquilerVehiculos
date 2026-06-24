# Microservicio: Pago

## Descripcion

Microservicio encargado de la gestion de pagos del Sistema de Alquiler de Vehiculos. Permite registrar pagos asociados a una reserva, validando que la reserva exista en el sistema.

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
src/main/java/com/example/pago/
├── PagoApplication.java
├── webclient/
│   └── ReservaClient.java        # Comunicacion con microservicio Reserva
├── controller/
│   └── PagoController.java       # Endpoints REST
├── dto/
│   ├── PagoRequestDTO.java       # DTO de entrada con validaciones Jakarta
│   ├── PagoResponseDTO.java      # DTO de salida
│   └── ReservaResponseDTO.java   # DTO para respuesta de Reserva
├── exception/
│   ├── PagoNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Pago.java                 # Entidad JPA
├── repository/
│   └── PagoRepository.java       # JpaRepository con consultas custom
└── service/
    └── PagoService.java          # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/pago` | Listar todos los pagos | `200 OK` |
| `GET` | `/api/pago/{id}` | Buscar pago por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/pago/reserva/{reservaId}` | Buscar pagos por reserva | `200 OK` |
| `POST` | `/api/pago` | Crear nuevo pago (valida reserva) | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/pago/{id}` | Actualizar pago existente | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/pago/{id}` | Eliminar pago | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "idReserva": 1,
  "idVehiculo": 1,
  "fechaPago": "2025-06-21",
  "montoPago": 100000,
  "metodoPago": "Transferencia",
  "estadoPago": "Pagada",
  "transaccionPago": "TXN001"
}
```

---

## Modelo de Datos

### Tabla: `pago`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id_pago` | `BIGINT` | PK, AUTO_INCREMENT |
| `id_reserva` | `BIGINT` | NOT NULL |
| `id_vehiculo` | `BIGINT` | NOT NULL |
| `fecha_pago` | `DATE` | NOT NULL |
| `monto_pago` | `DECIMAL` | NOT NULL |
| `metodo_pago` | `VARCHAR(14)` | Efectivo, Tarjeta, Transferencia, WebPay |
| `estado_pago` | `VARCHAR(20)` | Ingresada, Pagada, Anulada, Cancelada, Pendiente, EnCobranza |
| `transaccion_pago` | `VARCHAR(14)` | NOT NULL |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE pago_db;`
3. Ejecutar:
```bash
cd pago
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8088`

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
| `404 NOT FOUND` | Pago no encontrado |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8088
```
