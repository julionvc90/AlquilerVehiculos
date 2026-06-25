# Microservicio: Vendedor

## Descripcion

Microservicio encargado de la gestion de vendedores del Sistema de Alquiler de Vehiculos. Expone una API REST con operaciones CRUD completas, validaciones de negocio, manejo centralizado de errores y trazabilidad mediante logs.

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
src/main/java/com/example/vendedor/
├── VendedorApplication.java
├── webclient/
│   └── UsuarioClient.java        # Comunicacion con microservicio Usuario
├── controller/
│   └── VendedorController.java   # Endpoints REST (ResponseEntity, @Valid)
├── dto/
│   ├── VendedorRequestDTO.java   # DTO de entrada con validaciones Jakarta
│   └── VendedorResponseDTO.java  # DTO de salida
├── exception/
│   ├── ResourceNotFoundException.java
│   ├── ErrorResponse.java        # Estructura consistente de errores
│   └── GlobalExceptionHandler.java  # @ControllerAdvice centralizado
├── model/
│   └── Vendedor.java             # Entidad JPA
├── repository/
│   └── VendedorRepository.java   # JpaRepository con consultas custom
└── service/
    └── VendedorService.java      # Logica de negocio + logs SLF4J
```

---

## Endpoints REST

| Metodo | Ruta | Descripcion | Response |
|---|---|---|---|
| `GET` | `/api/vendedores` | Listar todos los vendedores | `200 OK` |
| `GET` | `/api/vendedores/{id}` | Buscar vendedor por ID | `200 OK` / `404 Not Found` |
| `GET` | `/api/vendedores/rut/{rut}` | Buscar vendedor por RUT | `200 OK` / `404 Not Found` |
| `POST` | `/api/vendedores` | Crear nuevo vendedor | `201 Created` / `400 Bad Request` / `409 Conflict` |
| `PUT` | `/api/vendedores/{id}` | Actualizar vendedor existente | `200 OK` / `400 Bad Request` / `404 Not Found` / `409 Conflict` |
| `DELETE` | `/api/vendedores/{id}` | Eliminar vendedor | `204 No Content` / `404 Not Found` |

### Ejemplo de Request Body (POST)

```json
{
  "rut": "10.123.456-7",
  "nombre": "Maria",
  "apellido": "Gomez",
  "email": "maria.gomez@empresa.com",
  "telefono": "+56987654321",
  "usuarioId": 300002
}
```

---

## Modelo de Datos

### Tabla: `vendedor`

| Campo | Tipo | Restricciones |
|---|---|---|
| `id` | `BIGINT` | PK, AUTO_INCREMENT (inicia en 100000) |
| `rut` | `VARCHAR(12)` | UNIQUE, NOT NULL |
| `nombre` | `VARCHAR(50)` | NOT NULL |
| `apellido` | `VARCHAR(50)` | NOT NULL |
| `email` | `VARCHAR(100)` | NOT NULL |
| `telefono` | `VARCHAR(15)` | Opcional |
| `usuario_id` | `BIGINT` | FK logica → microservicio Usuario |
| `activo` | `BOOLEAN` | NOT NULL, default `true` |

### Relaciones

- **`usuario_id`**: Referencia logica al microservicio `usuario`. La verificacion de existencia se realiza via `UsuarioClient` (WebClient) al puerto `9091`.

> **Nota:** Configurar el AUTO_INCREMENT en MySQL con: `ALTER TABLE vendedor AUTO_INCREMENT = 100000;`

---

## Ejecucion

### Requisitos previos

- Java 21
- Maven
- MySQL (puerto 3306)

### Pasos

1. Configurar las credenciales de MySQL en `src/main/resources/application.yml`
2. Crear la base de datos: `CREATE DATABASE vendedor_db;`
3. Ejecutar:
```bash
cd vendedor
./mvnw spring-boot:run
```
4. El microservicio se levanta en `http://localhost:9093`
5. Swagger UI: `http://localhost:9093/doc/swagger-ui/index.html`

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
| `404 NOT FOUND` | Recurso no encontrado | Vendedor con ID inexistente |
| `409 CONFLICT` | Violacion de regla de negocio | RUT duplicado al crear/actualizar |
| `500 INTERNAL SERVER ERROR` | Error inesperado del servidor | Falla de conexion a BD |

---

## Validaciones de Negocio

| Regla | Implementada en |
|---|---|
| RUT unico | `VendedorService.crear()` y `actualizar()` |
| RUT, nombre, apellido, email obligatorios | `VendedorRequestDTO` via `@NotBlank` |
| Email con formato valido | `VendedorRequestDTO` via `@Email` |
| Longitud maxima de campos | `VendedorRequestDTO` via `@Size` |

---

## Configuracion de Puerto

```
server.port=9093
```
