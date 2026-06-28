# 🚗 Sistema de Alquiler de Vehiculos

**Proyecto Semestral — Arquitectura de Microservicios**  
Desarrollo FullStack I · Ingenieria en Informatica

Integrantes:
- Benjamin Arellano
- Julio Navarro
- Claudio Carril

---

## 📋 Descripcion General

Sistema distribuido para la gestion completa de alquiler de vehiculos, implementado con una arquitectura de **10 microservicios independientes** que se comunican entre si via **WebClient**, con un **API Gateway** como punto unico de entrada. Cada microservicio posee su propia base de datos MySQL y expone una API REST.

### Flujo de Negocio

```
Usuario ──┬→ Cliente ──┐
           └→ Vendedor ─→ Vehiculo ─→ Disponibilidad
                                          │
Cliente ─┐                                 │
Vendedor ┤                                 │
         ├→ Reserva ─→ Pago ─→ Alquiler ──┤
Vehiculo ┘                        │        │
Disponib ─┘                    Inspeccion  Multa
```

---

## 🧩 Microservicios

| # | Microservicio | Puerto | BD | Descripcion |
|---|---|---|---|---|
| 1 | **usuario** | `9091` | `usuario_db` | Autenticacion y roles (ADMIN, CLIENTE, VENDEDOR) |
| 2 | **cliente** | `9092` | `cliente_db` | Gestion de clientes (personas que alquilan) |
| 3 | **vendedor** | `9093` | `vendedor_db` | Gestion de vendedores (empleados) |
| 4 | **vehiculo** | `9094` | `vehiculo_db` | Catalogo de vehiculos disponibles |
| 5 | **disponibilidad** | `9095` | `disponibilidad_db` | Control de disponibilidad por fechas |
| 6 | **reserva** | `9099` | `Reserva_db` | Reserva de vehiculos por clientes |
| 7 | **pago** | `9098` | `pago_db` | Registro de pagos asociados a reservas |
| 8 | **alquiler** | `9097` | `alquiler_db` | Orquestacion del alquiler (reserva→pago→activo→finalizado) |
| 9 | **inspeccion** | `9096` | `inspeccion_db` | Inspecciones de entrega y devolucion |
| 10 | **multa** | `9100` | `multa_db` | Multas por devolucion tardia o daños |
| — | **gateway** | `9000` | — | API Gateway - punto unico de entrada |

---

## 🛠 Stack Tecnologico

| Componente | Version |
|---|---|
| Lenguaje | **Java 21** |
| Framework | **Spring Boot 4.0.6** |
| Build | **Maven** (con wrapper `mvnw`) |
| Base de Datos | **MySQL** (una por microservicio) |
| Persistencia | Spring Data JPA + Hibernate |
| Validaciones | Jakarta Bean Validation |
| Comunicacion REST | Spring WebMVC |
| Comunicacion entre MS | **WebClient** (WebFlux reactivo) |
| Documentacion API | **SpringDoc OpenAPI / Swagger** |
| Configuracion | **YAML** (`application.yml`) |
| Logging | **SLF4J + Logback** con persistencia a archivo |
| Testing | **JUnit 5 + Mockito** |
| API Gateway | **Spring Cloud Gateway** |
| Utilidades | Lombok

---

## 🏗 Arquitectura

### Patron CSR (Controller-Service-Repository)

Cada microservicio sigue la misma estructura:

```
src/main/java/com/example/[microservicio]/
├── [Microservicio]Application.java
├── webclient/        # Clientes WebClient para comunicacion entre MS
├── controller/       # Endpoints REST (ResponseEntity, @Valid)
├── dto/              # DTOs de entrada/salida con validaciones
├── exception/        # Manejo centralizado de errores (@ControllerAdvice)
├── model/            # Entidades JPA
├── repository/       # JpaRepository
└── service/          # Logica de negocio + SLF4J
```

### Comunicacion entre Microservicios

```
usuario (9091) ←── cliente (9092)
usuario (9091) ←── vendedor (9093)
vendedor (9093) ←── vehiculo (9094)
vehiculo (9094) ←── disponibilidad (9095)
vehiculo (9094) ←── inspeccion (9096)
alquiler (9097) ←── inspeccion (9096)

cliente (9092) ←── reserva (9099)
vehiculo (9094) ←── reserva (9099)
disponibilidad (9095) ←── reserva (9099)

reserva (9099) ←── pago (9098)
reserva (9099) ←── multa (9100)

cliente (9092) ←── alquiler (9097)
vehiculo (9094) ←── alquiler (9097)
disponibilidad (9095) ←── alquiler (9097)
reserva (9099) ←── alquiler (9097)
pago (9098) ←── alquiler (9097)
```

---

## 🚀 Como Ejecutar

### Requisitos previos

- Java 21
- Maven (incluido via `mvnw`)
- MySQL (puerto 3306) — recomendado via XAMPP

### Paso 1: Configurar MySQL

Asegurate que MySQL este corriendo en `localhost:3306` con:
- **Usuario:** `root`
- **Contrasena:** *(vacia)*

### Paso 2: Iniciar el API Gateway (opcional)

El Gateway centraliza todas las rutas en `http://localhost:9000`:

```bash
cd gateway && ./mvnw spring-boot:run   # Puerto 9000
```

Si usas el Gateway, accede a los MS via `http://localhost:9000/api/<ms>`.  
Si no, accede directamente a cada MS en su puerto (9091–9100).

### Paso 3: Iniciar los microservicios

Deben iniciarse en orden, respetando las dependencias:

```bash
# Terminal 1 - Servicios base
cd usuario    && ./mvnw spring-boot:run   # Puerto 9091
cd cliente    && ./mvnw spring-boot:run   # Puerto 9092
cd vendedor   && ./mvnw spring-boot:run   # Puerto 9093

# Terminal 2 - Servicios intermedios
cd gateway        && ./mvnw spring-boot:run   # Puerto 9000
cd vehiculo       && ./mvnw spring-boot:run   # Puerto 9094
cd disponibilidad && ./mvnw spring-boot:run   # Puerto 9095
cd reserva        && ./mvnw spring-boot:run   # Puerto 9099

# Terminal 3 - Servicios finales
cd pago       && ./mvnw spring-boot:run   # Puerto 9098
cd alquiler   && ./mvnw spring-boot:run   # Puerto 9097
cd inspeccion && ./mvnw spring-boot:run   # Puerto 9096
cd multa      && ./mvnw spring-boot:run   # Puerto 9100
```

Cada microservicio crea automaticamente su base de datos al iniciar (`createDatabaseIfNotExist=true`).

### Paso 3: Swagger UI

Cada microservicio expone Swagger:

| Microservicio | Swagger UI |
|---|---|
| usuario | http://localhost:9091/doc/swagger-ui/index.html |
| cliente | http://localhost:9092/doc/swagger-ui/index.html |
| vendedor | http://localhost:9093/doc/swagger-ui/index.html |
| vehiculo | http://localhost:9094/doc/swagger-ui/index.html |
| disponibilidad | http://localhost:9095/doc/swagger-ui/index.html |
| inspeccion | http://localhost:9096/doc/swagger-ui/index.html |
| alquiler | http://localhost:9097/doc/swagger-ui/index.html |
| pago | http://localhost:9098/doc/swagger-ui/index.html |
| reserva | http://localhost:9099/doc/swagger-ui/index.html |
| multa | http://localhost:9100/doc/swagger-ui/index.html |

---

## 🧪 Pruebas Unitarias

```bash
cd <microservicio>
./mvnw test
```

Pruebas con JUnit 5 + Mockito: CRUD, reglas de negocio y validaciones FK.

---

## 🔄 Flujo Completo de Prueba

```bash
# 1. Crear usuario
curl -X POST http://localhost:9091/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"jperez","password":"123","email":"jperez@mail.com","rol":"cliente"}'

# 2. Crear cliente
curl -X POST http://localhost:9092/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"rut":"11111111-1","nombre":"Juan","apellido":"Perez","email":"jperez@mail.com","usuarioId":1}'

# 3. Crear vendedor
curl -X POST http://localhost:9093/api/vendedores \
  -H "Content-Type: application/json" \
  -d '{"rut":"22222222-2","nombre":"Pedro","apellido":"Lopez","email":"plopez@mail.com","usuarioId":2}'

# 4. Crear vehiculo
curl -X POST http://localhost:9094/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{"vendedorId":1,"patente":"AB1234","marca":"Toyota","modelo":"Corolla","anio":2024,"categoria":"Sedan","capacidadPasajeros":"5","color":"Rojo","tarifaDiaria":25000,"ubicacion":"Santiago"}'

# 5. Registrar disponibilidad
curl -X POST http://localhost:9095/api/disponibilidad \
  -H "Content-Type: application/json" \
  -d '{"vehiculoId":1,"fechaInicio":"2025-07-01","fechaFin":"2025-07-10","disponible":true}'

# 6. Crear reserva
curl -X POST http://localhost:9099/api/reserva \
  -H "Content-Type: application/json" \
  -d '{"idCliente":1,"idVehiculo":1,"fechaReserva":"2025-06-20","fechaInicio":"2025-07-01","fechaTermino":"2025-07-05","totalDias":4,"valorDia":25000,"totalReserva":100000,"estadoReserva":"Confirmada","observacionesReserva":"Sin novedades"}'

# 7. Pagar la reserva
curl -X POST http://localhost:9098/api/pago \
  -H "Content-Type: application/json" \
  -d '{"idReserva":1,"idVehiculo":1,"fechaPago":"2025-06-21","montoPago":100000,"metodoPago":"Transferencia","estadoPago":"Pagada","transaccionPago":"TXN001"}'

# 8. Crear alquiler (valida reserva confirmada + pago registrado)
curl -X POST http://localhost:9097/api/alquiler \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"vehiculoId":1,"reservaId":1,"fechaInicio":"2025-07-01","fechaFin":"2025-07-05"}'

# 9. Iniciar alquiler (entrega del vehiculo)
curl -X PUT http://localhost:9097/api/alquiler/iniciar/1

# 10. Finalizar alquiler (devolucion)
curl -X PUT http://localhost:9097/api/alquiler/finalizar/1

# 11. Inspeccion de devolucion
curl -X POST http://localhost:9096/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{"alquilerId":1,"vehiculoId":1,"fechaInspeccion":"2025-07-05T18:00:00","tipoInspeccion":"Devolucion","resultado":"Aprobado","observaciones":"Sin daños","inspector":"Carlos"}'

# 12. Multa por retraso (opcional)
curl -X POST http://localhost:9100/api/multa \
  -H "Content-Type: application/json" \
  -d '{"idReserva":1,"idVehiculo":1,"motivoMulta":"Devolucion tardia","montoMulta":50000,"estadoMulta":"Pendiente"}'
```

---

## 📁 Estructura del Repositorio

```
AlquilerVehiculos/
├── README.md                    # Este archivo
├── alquiler/                    # Microservicio Alquiler (9097)
├── cliente/                     # Microservicio Cliente (9092)
├── gateway/                     # API Gateway (9000)
├── disponibilidad/              # Microservicio Disponibilidad (9095)
├── inspeccion/                  # Microservicio Inspeccion (9096)
├── multa/                       # Microservicio Multa (9100)
├── pago/                        # Microservicio Pago (9098)
├── reserva/                     # Microservicio Reserva (9099)
├── usuario/                     # Microservicio Usuario (9091)
├── vehiculo/                    # Microservicio Vehiculo (9094)
└── vendedor/                    # Microservicio Vendedor (9093)
```

Cada microservicio contiene su propio `README.md` con documentacion detallada.

---

## 📄 Licencia

Proyecto academico — Duoc UC · Carrera de Ingenieria en Informatica
