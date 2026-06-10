# Microservicio: Disponibilidad

## Descripcion

Microservicio encargado de gestionar la disponibilidad de vehiculos del Sistema de Alquiler de Vehiculos. Permite registrar periodos de disponibilidad y validar si un vehiculo esta disponible para un rango de fechas especifico.

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
src/main/java/com/example/disponibilidad/
├── DisponibilidadApplication.java
├── webclient/
│   └── VehiculoClient.java       # Comunicacion con microservicio Vehiculo
├── controller/
│   └── DisponibilidadController.java # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── DisponibilidadRequestDTO.java
│   ├── DisponibilidadResponseDTO.java
│   └── VehiculoResponseDTO.java  # DTO para respuesta de Vehiculo
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Disponibilidad.java       # Entidad JPA
├── repository/
│   └── DisponibilidadRepository.java # JpaRepository con consultas custom
└── service/
    └── DisponibilidadService.java # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/disponibilidad` | Listar todas las disponibilidades | `200 OK` |
| `GET` | `/api/disponibilidad/{id}` | Buscar disponibilidad por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/disponibilidad/validar` | Validar disponibilidad por fechas | `200 OK` (boolean) |
| `POST` | `/api/disponibilidad` | Crear nueva disponibilidad | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/disponibilidad/{id}` | Actualizar disponibilidad | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/disponibilidad/{id}` | Eliminar disponibilidad | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "vehiculoId": 1,
  "fechaInicio": "2025-07-01",
  "fechaFin": "2025-07-10",
  "disponible": true
}
```

### Ejemplo de validacion (GET)

```
GET /api/disponibilidad/validar?vehiculoId=1&inicio=2025-07-01&fin=2025-07-05
```
Response: `true`

---

## Modelo de Datos

### Tabla: `Disponibilidad`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT |
| `vehiculo_id` | `BIGINT` | FK logica → microservicio Vehiculo |
| `fecha_inicio` | `DATE` | NOT NULL |
| `fecha_fin` | `DATE` | NOT NULL |
| `disponible` | `BOOLEAN` | NOT NULL |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE disponibilidad_db;`
3. Ejecutar:
```bash
cd disponibilidad
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8085`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `vehiculo` | 8084 | WebClient | Validar existencia del vehiculo |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas |
| `404 NOT FOUND` | Disponibilidad no encontrada |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8085
```
