# Microservicio: Alquiler

## Descripcion

Microservicio encargado de la gestion de alquileres del Sistema de Alquiler de Vehiculos. Orquesta el flujo completo validando reserva, pago, cliente, vehiculo y disponibilidad. Expone API REST con CRUD completo y endpoints de inicio/finalizacion con control de estados.

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
src/main/java/com/example/alquiler/
├── AlquilerApplication.java
├── webclient/
│   ├── ClienteClient.java        # Comunicacion con microservicio Cliente
│   ├── VehiculoClient.java       # Comunicacion con microservicio Vehiculo
│   ├── DisponibilidadClient.java # Comunicacion con microservicio Disponibilidad
│   ├── ReservaClient.java        # Comunicacion con microservicio Reserva
│   └── PagoClient.java           # Comunicacion con microservicio Pago
├── controller/
│   └── AlquilerController.java   # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── AlquilerRequestDTO.java   # DTO de entrada con validaciones Jakarta
│   ├── AlquilerResponseDTO.java  # DTO de salida
│   ├── ClienteResponseDTO.java   # DTO para respuesta de Cliente
│   ├── VehiculoResponseDTO.java  # DTO para respuesta de Vehiculo
│   ├── ReservaResponseDTO.java   # DTO para respuesta de Reserva
│   └── PagoResponseDTO.java      # DTO para respuesta de Pago
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   ├── Alquiler.java             # Entidad JPA
│   └── EstadoAlquiler.java       # Enum: Reservado, ACTIVO, FINALIZADO, CANCELADO
├── repository/
│   └── AlquilerRepository.java   # JpaRepository
└── service/
    └── AlquilerService.java      # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/alquiler` | Listar todos los alquileres | `200 OK` |
| `GET` | `/api/alquiler/{id}` | Buscar alquiler por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/alquiler/{id}/existe` | Verificar si existe un alquiler | `200 OK` (boolean) |
| `POST` | `/api/alquiler` | Crear nuevo alquiler (valida reserva + pago) | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/alquiler/actualizar/{id}` | Actualizar alquiler existente | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `PUT` | `/api/alquiler/iniciar/{id}` | Iniciar alquiler (Reservado → ACTIVO) | `200 OK` / `400 Bad Request` |
| `PUT` | `/api/alquiler/finalizar/{id}` | Finalizar alquiler (ACTIVO → FINALIZADO) | `200 OK` / `400 Bad Request` |
| `DELETE` | `/api/alquiler/{id}` | Eliminar alquiler | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "clienteId": 200001,
  "vehiculoId": 1,
  "reservaId": 1,
  "fechaInicio": "2025-07-01",
  "fechaFin": "2025-07-05"
}
```

---

## Modelo de Datos

### Tabla: `Alquiler`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT |
| `cliente_id` | `BIGINT` | NOT NULL |
| `vehiculo_id` | `BIGINT` | NOT NULL |
| `reserva_id` | `BIGINT` | FK logica → microservicio Reserva |
| `fecha_inicio` | `DATE` | NOT NULL |
| `fecha_fin` | `DATE` | NOT NULL |
| `dias` | `INTEGER` | NOT NULL |
| `tarifa_diaria` | `DOUBLE` | NOT NULL |
| `monto_total` | `DOUBLE` | NOT NULL |
| `estado` | `ENUM` | Reservado, ACTIVO, FINALIZADO, CANCELADO |

### Estados del Alquiler

| Estado | Descripcion |
|---|---|
| `Reservado` | Creado, pendiente de inicio |
| `ACTIVO` | Vehiculo entregado al cliente |
| `FINALIZADO` | Vehiculo devuelto |
| `CANCELADO` | Alquiler cancelado |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE alquiler_db;`
3. Ejecutar:
```bash
cd alquiler
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8086`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `cliente` | 8081 | WebClient | Validar existencia del cliente |
| `vehiculo` | 8084 | WebClient | Obtener datos del vehiculo |
| `disponibilidad` | 8085 | WebClient | Validar disponibilidad del vehiculo |
| `reserva` | 8089 | WebClient | Validar que la reserva este confirmada |
| `pago` | 8088 | WebClient | Validar que exista un pago para la reserva |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas o regla de negocio |
| `404 NOT FOUND` | Alquiler no encontrado |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8086
```
