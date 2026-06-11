# 🚗 Sistema de Alquiler de Vehiculos

**Proyecto Semestral — Arquitectura de Microservicios**  
Desarrollo FullStack I · Ingenieria en Informatica

Integrantes:
- Benjamin Arellano
- Julio Navarro
- Claudio Carril

---

## 📋 Descripcion General

Sistema distribuido para la gestion completa de alquiler de vehiculos, implementado con una arquitectura de **10 microservicios independientes** que se comunican entre si via **WebClient**. Cada microservicio posee su propia base de datos MySQL y expone una API REST.

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
| 1 | **usuario** | `8082` | `usuario_db` | Autenticacion y roles (ADMIN, CLIENTE, VENDEDOR) |
| 2 | **cliente** | `8081` | `cliente_db` | Gestion de clientes (personas que alquilan) |
| 3 | **vendedor** | `8083` | `vendedor_db` | Gestion de vendedores (empleados) |
| 4 | **vehiculo** | `8084` | `vehiculo_db` | Catalogo de vehiculos disponibles |
| 5 | **disponibilidad** | `8085` | `disponibilidad_db` | Control de disponibilidad por fechas |
| 6 | **reserva** | `8089` | `Reserva_db` | Reserva de vehiculos por clientes |
| 7 | **pago** | `8088` | `pago_db` | Registro de pagos asociados a reservas |
| 8 | **alquiler** | `8086` | `alquiler_db` | Orquestacion del alquiler (reserva→pago→activo→finalizado) |
| 9 | **inspeccion** | `8087` | `inspeccion_db` | Inspecciones de entrega y devolucion |
| 10 | **multa** | `8090` | `multa_db` | Multas por devolucion tardia o daños |

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
| Utilidades | Lombok, SLF4J |

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
usuario (8082) ←── cliente (8081)
usuario (8082) ←── vendedor (8083)
vendedor (8083) ←── vehiculo (8084)
vehiculo (8084) ←── disponibilidad (8085)
vehiculo (8084) ←── inspeccion (8087)
alquiler (8086) ←── inspeccion (8087)

cliente (8081) ←── reserva (8089)
vehiculo (8084) ←── reserva (8089)
disponibilidad (8085) ←── reserva (8089)

reserva (8089) ←── pago (8088)
reserva (8089) ←── multa (8090)

cliente (8081) ←── alquiler (8086)
vehiculo (8084) ←── alquiler (8086)
disponibilidad (8085) ←── alquiler (8086)
reserva (8089) ←── alquiler (8086)
pago (8088) ←── alquiler (8086)
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
- **Contrasena:** `root`

### Paso 2: Iniciar los microservicios

Deben iniciarse en orden, respetando las dependencias:

```bash
# Terminal 1 - Servicios base
cd usuario    && ./mvnw spring-boot:run   # Puerto 8082
cd cliente    && ./mvnw spring-boot:run   # Puerto 8081
cd vendedor   && ./mvnw spring-boot:run   # Puerto 8083

# Terminal 2 - Servicios intermedios
cd vehiculo       && ./mvnw spring-boot:run   # Puerto 8084
cd disponibilidad && ./mvnw spring-boot:run   # Puerto 8085
cd reserva        && ./mvnw spring-boot:run   # Puerto 8089

# Terminal 3 - Servicios finales
cd pago       && ./mvnw spring-boot:run   # Puerto 8088
cd alquiler   && ./mvnw spring-boot:run   # Puerto 8086
cd inspeccion && ./mvnw spring-boot:run   # Puerto 8087
cd multa      && ./mvnw spring-boot:run   # Puerto 8090
```

Cada microservicio crea automaticamente su base de datos al iniciar (`createDatabaseIfNotExist=true`).

---

## 🔄 Flujo Completo de Prueba

```bash
# 1. Crear usuario
curl -X POST http://localhost:8082/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{"username":"jperez","password":"123","email":"jperez@mail.com","rol":"cliente"}'

# 2. Crear cliente
curl -X POST http://localhost:8081/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"rut":"11111111-1","nombre":"Juan","apellido":"Perez","email":"jperez@mail.com","usuarioId":1}'

# 3. Crear vendedor
curl -X POST http://localhost:8083/api/vendedores \
  -H "Content-Type: application/json" \
  -d '{"rut":"22222222-2","nombre":"Pedro","apellido":"Lopez","email":"plopez@mail.com","usuarioId":2}'

# 4. Crear vehiculo
curl -X POST http://localhost:8084/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{"vendedorId":1,"patente":"AB1234","marca":"Toyota","modelo":"Corolla","anio":2024,"categoria":"Sedan","capacidadPasajeros":"5","color":"Rojo","tarifaDiaria":25000,"ubicacion":"Santiago"}'

# 5. Registrar disponibilidad
curl -X POST http://localhost:8085/api/disponibilidad \
  -H "Content-Type: application/json" \
  -d '{"vehiculoId":1,"fechaInicio":"2025-07-01","fechaFin":"2025-07-10","disponible":true}'

# 6. Crear reserva
curl -X POST http://localhost:8089/api/reserva \
  -H "Content-Type: application/json" \
  -d '{"idCliente":1,"idVehiculo":1,"fechaReserva":"2025-06-20","fechaInicio":"2025-07-01","fechaTermino":"2025-07-05","totalDias":4,"valorDia":25000,"totalReserva":100000,"estadoReserva":"Confirmada","observacionesReserva":"Sin novedades"}'

# 7. Pagar la reserva
curl -X POST http://localhost:8088/api/pago \
  -H "Content-Type: application/json" \
  -d '{"idReserva":1,"idVehiculo":1,"fechaPago":"2025-06-21","montoPago":100000,"metodoPago":"Transferencia","estadoPago":"Pagada","transaccionPago":"TXN001"}'

# 8. Crear alquiler (valida reserva confirmada + pago registrado)
curl -X POST http://localhost:8086/api/alquiler \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"vehiculoId":1,"reservaId":1,"fechaInicio":"2025-07-01","fechaFin":"2025-07-05"}'

# 9. Iniciar alquiler (entrega del vehiculo)
curl -X PUT http://localhost:8086/api/alquiler/iniciar/1

# 10. Finalizar alquiler (devolucion)
curl -X PUT http://localhost:8086/api/alquiler/finalizar/1

# 11. Inspeccion de devolucion
curl -X POST http://localhost:8087/api/inspecciones \
  -H "Content-Type: application/json" \
  -d '{"alquilerId":1,"vehiculoId":1,"fechaInspeccion":"2025-07-05T18:00:00","tipoInspeccion":"Devolucion","resultado":"Aprobado","observaciones":"Sin daños","inspector":"Carlos"}'

# 12. Multa por retraso (opcional)
curl -X POST http://localhost:8090/api/multa \
  -H "Content-Type: application/json" \
  -d '{"idReserva":1,"idVehiculo":1,"motivoMulta":"Devolucion tardia","montoMulta":50000,"estadoMulta":"Pendiente"}'
```

---

## 📁 Estructura del Repositorio

```
AlquilerVehiculos/
├── README.md                    # Este archivo
├── alquiler/                    # Microservicio Alquiler (8086)
├── cliente/                     # Microservicio Cliente (8081)
├── disponibilidad/              # Microservicio Disponibilidad (8085)
├── inspeccion/                  # Microservicio Inspeccion (8087)
├── multa/                       # Microservicio Multa (8090)
├── pago/                        # Microservicio Pago (8088)
├── reserva/                     # Microservicio Reserva (8089)
├── usuario/                     # Microservicio Usuario (8082)
├── vehiculo/                    # Microservicio Vehiculo (8084)
└── vendedor/                    # Microservicio Vendedor (8083)
```

Cada microservicio contiene su propio `README.md` con documentacion detallada.

---

## 📄 Licencia

Proyecto academico — Duoc UC · Carrera de Ingenieria en Informatica
