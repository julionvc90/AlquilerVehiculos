# Microservicio: Inspeccion

## Descripcion

Microservicio encargado de la gestion de inspecciones del Sistema de Alquiler de Vehiculos. Expone una API REST con operaciones CRUD, filtros por alquiler, vehiculo y tipo de inspeccion, validaciones de negocio, manejo centralizado de errores y trazabilidad mediante logs.

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
src/main/java/com/example/inspeccion/
├── InspeccionApplication.java
├── webclient/
│   ├── AlquilerClient.java       # Comunicacion con microservicio Alquiler
│   └── VehiculoClient.java       # Comunicacion con microservicio Vehiculo
├── controller/
│   └── InspeccionController.java # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── InspeccionRequestDTO.java # DTO de entrada con validaciones Jakarta
│   └── InspeccionResponseDTO.java # DTO de salida
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java        # Estructura consistente de errores
│   └── GlobalExceptionHandler.java # @ControllerAdvice centralizado
├── model/
│   └── Inspeccion.java           # Entidad JPA
├── repository/
│   └── InspeccionRepository.java # JpaRepository con consultas custom
└── service/
    └── InspeccionService.java    # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/inspecciones` | Listar todas las inspecciones | `200 OK` |
| `GET` | `/api/inspecciones/{id}` | Buscar inspeccion por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/inspecciones/alquiler/{alquilerId}` | Buscar por alquiler | `200 OK` |
| `GET` | `/api/inspecciones/vehiculo/{vehiculoId}` | Buscar por vehiculo | `200 OK` |
| `GET` | `/api/inspecciones/tipo/{tipo}` | Filtrar por tipo (PRE_ALQUILER / POST_ALQUILER) | `200 OK` |
| `POST` | `/api/inspecciones` | Crear nueva inspeccion | `201 Created` / `400 Bad Request` |
| `PUT` | `/api/inspecciones/{id}` | Actualizar inspeccion | `200 OK` / `400 Bad Request` / `404 Not Found` |
| `DELETE` | `/api/inspecciones/{id}` | Eliminar inspeccion | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "alquilerId": 800001,
  "vehiculoId": 1,
  "fechaInspeccion": "2026-06-04T10:00:00",
  "tipoInspeccion": "PRE_ALQUILER",
  "resultado": "APROBADO",
  "observaciones": "Vehiculo en buen estado",
  "inspector": "Carlos Rodriguez"
}
```

---

## Modelo de Datos

### Tabla: `inspeccion`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT (inicia en 400000) |
| `alquiler_id` | `BIGINT` | FK logica → microservicio Alquiler |
| `vehiculo_id` | `BIGINT` | FK logica → microservicio Vehiculo |
| `fecha_inspeccion` | `DATETIME` | NOT NULL |
| `tipo_inspeccion` | `VARCHAR(20)` | NOT NULL |
| `resultado` | `VARCHAR(20)` | NOT NULL |
| `observaciones` | `VARCHAR(500)` | Opcional |
| `inspector` | `VARCHAR(100)` | Opcional |
| `activo` | `BOOLEAN` | NOT NULL, default `true` |

### Tipos de Inspeccion

| Tipo | Descripcion |
|---|---|
| `PRE_ALQUILER` | Se realiza antes de entregar el vehiculo |
| `POST_ALQUILER` | Se realiza al devolver el vehiculo |

### Resultados

| Resultado | Descripcion |
|---|---|
| `APROBADO` | Sin danos ni observaciones |
| `RECHAZADO` | Con danos graves |
| `CON_OBSERVACIONES` | Con detalles menores |

> **Nota:** Configurar el AUTO_INCREMENT en MySQL con: `ALTER TABLE inspeccion AUTO_INCREMENT = 400000;`

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar las credenciales de MySQL en `src/main/resources/application.properties`
2. Crear la base de datos: `CREATE DATABASE inspeccion_db;`
3. Ejecutar:
```bash
cd inspeccion
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:8087`

---

## Comunicacion con otros Microservicios

| Microservicio | Puerto | Via | Descripcion |
|---|---|---|---|
| `alquiler` | 8085 | WebClient | Verificar existencia de alquiler |
| `vehiculo` | 8080 | WebClient | Verificar existencia de vehiculo |

---

## Manejo de Errores

| Codigo HTTP | Causa |
|---|---|
| `400 BAD REQUEST` | Validaciones de DTO fallidas |
| `404 NOT FOUND` | Inspeccion no encontrada |
| `409 CONFLICT` | Violacion de regla de negocio |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor |

---

## Configuracion de Puerto

```
server.port=8084
```
