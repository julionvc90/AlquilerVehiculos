# Microservicio: Vehiculo

## Descripcion

Microservicio encargado de la gestion de vehiculos del Sistema de Alquiler de Vehiculos. Expone una API REST con operaciones CRUD, verificacion de existencia para otros microservicios, validaciones de negocio, manejo centralizado de errores y trazabilidad mediante logs.

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
src/main/java/com/example/vehiculo/
├── VehiculoApplication.java
├── webclient/
│   └── VendedorClient.java       # Comunicacion con microservicio Vendedor
├── controller/
│   └── VehiculoController.java   # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── VehiculoRequestDTO.java   # DTO de entrada con validaciones Jakarta
│   ├── VehiculoResponseDTO.java  # DTO de salida
│   └── VendedorResponseDTO.java  # DTO para respuesta de Vendedor
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Vehiculo.java             # Entidad JPA
├── repository/
│   └── VehiculoRepository.java   # JpaRepository
└── service/
    └── VehiculoService.java      # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/vehiculos` | Listar todos los vehiculos | `200 OK` |
| `GET` | `/api/vehiculos/{id}` | Buscar vehiculo por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/vehiculos/{id}/existe` | Verificar si un vehiculo existe | `200 OK` (boolean) |
| `POST` | `/api/vehiculos` | Crear nuevo vehiculo | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/vehiculos/{id}` | Actualizar vehiculo existente | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/vehiculos/{id}` | Eliminar vehiculo | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "vendedorId": 100001,
  "patente": "AB1234",
  "marca": "Toyota",
  "modelo": "Corolla",
  "anio": 2024,
  "categoria": "Sedan",
  "capacidadPasajeros": "5",
  "color": "Rojo",
  "tarifaDiaria": 25000,
  "ubicacion": "Santiago"
}
```

---

## Modelo de Datos

### Tabla: `Vehiculo`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT |
| `patente` | `VARCHAR(6)` | UNIQUE, NOT NULL |
| `marca` | `VARCHAR(10)` | NOT NULL |
| `modelo` | `VARCHAR(20)` | NOT NULL |
| `anio` | `INTEGER` | |
| `categoria` | `VARCHAR(255)` | |
| `capacidad_pasajeros` | `VARCHAR(2)` | NOT NULL |
| `color` | `VARCHAR(255)` | |
| `vendedor_id` | `BIGINT` | FK logica → microservicio Vendedor |
| `tarifa_diaria` | `DOUBLE` | |
| `ubicacion` | `VARCHAR(255)` | |
| `activo` | `BOOLEAN` | NOT NULL, default `true` |

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar credenciales MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE vehiculo_db;`
3. Ejecutar:
```bash
cd vehiculo
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8084`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `vendedor` | 8083 | WebClient | Validar existencia del vendedor |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas |
| `404 NOT FOUND` | Vehiculo no encontrado |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8084
```
