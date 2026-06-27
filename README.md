# 🚗 Sistema de Alquiler de Vehiculos

**Proyecto Semestral — Arquitectura de Microservicios**
Desarrollo FullStack I · Ingenieria en Informatica

Integrantes:

* Benjamin Arellano
* Julio Navarro
* Claudio Carril

---

## 📋 Descripcion General

Sistema distribuido para la gestion completa de alquiler de vehiculos, implementado con una arquitectura de **microservicios independientes**.

El proyecto utiliza:

* **Eureka Server** para el registro y descubrimiento de microservicios.
* **Gateway** como punto de entrada unico al sistema.
* **DataFaker** para generar datos falsos automaticamente en ambiente de desarrollo.
* **Swagger / OpenAPI** para documentar y probar los endpoints REST.
* **MySQL** como base de datos, con una base independiente por microservicio.

Cada microservicio posee su propia base de datos MySQL y expone una API REST. Los servicios se comunican entre si mediante **WebClient** y pueden ser consumidos desde una sola entrada utilizando el **Gateway**.

---

## 🔄 Flujo de Negocio

```txt
Usuario ──┬→ Cliente ──┐
          └→ Vendedor ─→ Vehiculo ─→ Disponibilidad
                                         │
Cliente ─┐                                │
Vendedor ┤                                │
         ├→ Reserva ─→ Pago ─→ Alquiler ─┤
Vehiculo ┘                       │        │
Disponib ─┘                   Inspeccion  Multa
```

---

## 🌐 Arquitectura General

```txt
                 ┌────────────────────┐
                 │   Eureka Server     │
                 │ localhost:8761      │
                 └─────────┬──────────┘
                           │
        ┌──────────────────┼──────────────────┐
        │                  │                  │
┌───────▼───────┐  ┌───────▼───────┐  ┌───────▼───────┐
│   Gateway     │  │ Microservicios│  │ Bases de datos│
│ localhost:8101│  │ registrados   │  │ MySQL         │
└───────┬───────┘  └───────────────┘  └───────────────┘
        │
        ▼
Entrada unica para consumir las APIs REST
```

---

## 🧩 Microservicios

| #  | Microservicio      | Puerto | BD                  | Descripcion                                     |
| -- | ------------------ | ------ | ------------------- | ----------------------------------------------- |
| 1  | **eureka**         | `8761` | No aplica           | Servidor de descubrimiento de servicios         |
| 2  | **gateway**        | `8101` | No aplica           | Punto de entrada unico hacia los microservicios |
| 3  | **usuario**        | `9091` | `usuario_db`        | Gestion de usuarios y roles                     |
| 4  | **cliente**        | `9092` | `cliente_db`        | Gestion de clientes                             |
| 5  | **vendedor**       | `9093` | `vendedor_db`       | Gestion de vendedores                           |
| 6  | **vehiculo**       | `9094` | `vehiculo_db`       | Catalogo de vehiculos                           |
| 7  | **disponibilidad** | `9095` | `disponibilidad_db` | Control de disponibilidad por fechas            |
| 8  | **inspeccion**     | `9096` | `inspeccion_db`     | Inspecciones de entrega y devolucion            |
| 9  | **alquiler**       | `9097` | `alquiler_db`       | Gestion del alquiler                            |
| 10 | **pago**           | `9098` | `pago_db`           | Registro de pagos                               |
| 11 | **reserva**        | `9099` | `reserva_db`        | Gestion de reservas                             |
| 12 | **multa**          | `9100` | `multa_db`          | Gestion de multas                               |

---

## 🛠 Stack Tecnologico

| Componente                        | Version / Uso                   |
| --------------------------------- | ------------------------------- |
| Lenguaje                          | **Java 21**                     |
| Framework                         | **Spring Boot 4.0.6**           |
| Arquitectura                      | **Microservicios**              |
| Build                             | **Maven** con wrapper `mvnw`    |
| Base de Datos                     | **MySQL**                       |
| Persistencia                      | Spring Data JPA + Hibernate     |
| Validaciones                      | Jakarta Bean Validation         |
| Comunicacion REST                 | Spring WebMVC                   |
| Comunicacion entre microservicios | WebClient                       |
| Descubrimiento de servicios       | **Spring Cloud Netflix Eureka** |
| Entrada unica                     | **Spring Cloud Gateway**        |
| Datos de prueba                   | **DataFaker**                   |
| Documentacion API                 | SpringDoc OpenAPI / Swagger     |
| Configuracion                     | YAML `application.yml`          |
| Testing                           | JUnit 5 + Mockito               |
| Utilidades                        | Lombok                          |

---

## 🏗 Arquitectura por Microservicio

Cada microservicio mantiene una estructura basada en el patron:

```txt
Controller → Service → Repository
```

Estructura general:

```txt
src/main/java/com/example/[microservicio]/
├── [Microservicio]Application.java
├── config/           # Configuraciones, WebClient, DataLoader
├── controller/       # Endpoints REST
├── dto/              # DTOs de entrada y salida
├── exception/        # Manejo de errores
├── model/            # Entidades JPA
├── repository/       # JpaRepository
├── service/          # Logica de negocio
└── webclient/        # Comunicacion con otros microservicios
```

---

# 🌐 Eureka Server

## ¿Que es Eureka?

Eureka es el servidor de descubrimiento de servicios del proyecto.

Su funcion es registrar automaticamente los microservicios que se encuentran activos, permitiendo que otros servicios puedan encontrarlos por su nombre y no por un puerto fijo.

Por ejemplo, el microservicio `vehiculo` se registra en Eureka con el nombre:

```txt
VEHICULO
```

Y el Gateway puede redirigir peticiones hacia el usando:

```txt
lb://vehiculo
```

---

## Configuracion de Eureka

El microservicio `eureka` se ejecuta en:

```txt
http://localhost:8761
```

Configuracion principal:

```yml
server:
  port: 8761

spring:
  application:
    name: eureka

eureka:
  instance:
    hostname: localhost
  client:
    register-with-eureka: false
    fetch-registry: false
```

### Explicacion

| Propiedad                         | Funcion                                 |
| --------------------------------- | --------------------------------------- |
| `server.port: 8761`               | Puerto donde se ejecuta Eureka          |
| `spring.application.name: eureka` | Nombre del servicio                     |
| `register-with-eureka: false`     | Evita que Eureka se registre a si mismo |
| `fetch-registry: false`           | Evita que Eureka busque otros registros |

---

## Configuracion Eureka Client

Cada microservicio contiene una configuracion similar:

```yml
eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: http://localhost:8761/eureka/
  instance:
    prefer-ip-address: true
    instance-id: "${spring.application.name}:${server.port}"
```

### Explicacion

| Propiedad                    | Funcion                                       |
| ---------------------------- | --------------------------------------------- |
| `register-with-eureka: true` | Registra el microservicio en Eureka           |
| `fetch-registry: true`       | Permite consultar otros servicios registrados |
| `defaultZone`                | URL del servidor Eureka                       |
| `prefer-ip-address: true`    | Registra el servicio usando direccion IP      |
| `instance-id`                | Muestra nombre y puerto del servicio          |

Cuando Eureka funciona correctamente, en el navegador se deben ver los servicios en estado **UP**.

Ejemplo:

```txt
GATEWAY          UP (1)
USUARIO          UP (1)
CLIENTE          UP (1)
VENDEDOR         UP (1)
VEHICULO         UP (1)
DISPONIBILIDAD   UP (1)
ALQUILER         UP (1)
PAGO             UP (1)
RESERVA          UP (1)
INSPECCION       UP (1)
MULTA            UP (1)
```

---

# 🚪 Gateway

## ¿Que es Gateway?

Gateway es el punto de entrada unico para consumir los microservicios.

En lugar de llamar a cada microservicio por separado usando su puerto interno, el cliente solo utiliza el puerto del Gateway:

```txt
http://localhost:8101
```

Ejemplo:

```txt
Sin Gateway:
http://localhost:9094/api/vehiculos

Con Gateway:
http://localhost:8101/api/vehiculos
```

---

## Configuracion del Gateway

Ejemplo de ruta hacia el microservicio `vehiculo`:

```yml
spring:
  application:
    name: gateway

  cloud:
    gateway:
      server:
        webmvc:
          routes:
            - id: vehiculo
              uri: lb://vehiculo
              predicates:
                - Path=/api/vehiculos/**
```

### Explicacion

| Propiedad                          | Funcion                                             |
| ---------------------------------- | --------------------------------------------------- |
| `spring.application.name: gateway` | Nombre del Gateway                                  |
| `uri: lb://vehiculo`               | Busca el servicio `vehiculo` registrado en Eureka   |
| `Path=/api/vehiculos/**`           | Redirige las peticiones de vehiculos                |
| `lb://`                            | Usa balanceo de carga y descubrimiento de servicios |

---

## Rutas principales por Gateway

Todas las peticiones pueden realizarse desde el puerto `8101`.

| Microservicio  | Ruta por Gateway                           |
| -------------- | ------------------------------------------ |
| usuario        | `http://localhost:8101/api/usuarios`       |
| cliente        | `http://localhost:8101/api/clientes`       |
| vendedor       | `http://localhost:8101/api/vendedores`     |
| vehiculo       | `http://localhost:8101/api/vehiculos`      |
| disponibilidad | `http://localhost:8101/api/disponibilidad` |
| reserva        | `http://localhost:8101/api/reserva`        |
| pago           | `http://localhost:8101/api/pago`           |
| alquiler       | `http://localhost:8101/api/alquiler`       |
| inspeccion     | `http://localhost:8101/api/inspecciones`   |
| multa          | `http://localhost:8101/api/multa`          |

---

# 🧪 DataFaker

## ¿Que es DataFaker?

DataFaker es una libreria utilizada para generar datos falsos automaticamente.

En este proyecto se usa para crear datos de prueba al iniciar los microservicios, evitando tener que registrar manualmente usuarios, clientes, vendedores, vehiculos, reservas, pagos, alquileres, inspecciones y multas.

---

## Activacion del perfil dev

Los cargadores de datos estan definidos en clases como:

```java
@Component
@Profile("dev")
public class DataLoader implements CommandLineRunner {
    @Override
    public void run(String... args) {
        // Generacion de datos falsos
    }
}
```

Eso significa que DataFaker solo se ejecuta cuando el perfil `dev` esta activo.

En los `application.yml` se activa con:

```yml
spring:
  profiles:
    active: dev
```

---

## Configuracion de base de datos en dev

En el perfil `dev`, se usa:

```yml
---
spring:
  config:
    activate:
      on-profile: dev
  jpa:
    hibernate:
      ddl-auto: create
```

### Explicacion

| Propiedad                                | Funcion                                                 |
| ---------------------------------------- | ------------------------------------------------------- |
| `spring.profiles.active: dev`            | Activa el perfil de desarrollo                          |
| `spring.config.activate.on-profile: dev` | Ejecuta esa configuracion solo cuando `dev` esta activo |
| `ddl-auto: create`                       | Crea nuevamente las tablas al iniciar                   |

> Importante: `ddl-auto: create` elimina y vuelve a crear las tablas al iniciar el microservicio. Es util para pruebas, pero no se recomienda para produccion.

---

## Dependencia de DataFaker

Los microservicios que generan datos de prueba incluyen:

```xml
<dependency>
    <groupId>net.datafaker</groupId>
    <artifactId>datafaker</artifactId>
    <version>2.4.3</version>
</dependency>
```

---

# 📚 Swagger UI

El proyecto cuenta con documentacion Swagger / OpenAPI para probar los endpoints REST de los microservicios.

Actualmente se puede acceder a todos los Swagger desde un unico enlace centralizado en el **Gateway**:

```txt
http://localhost:8101/doc/swagger-ui.html
```

Desde este link aparece un selector/desplegable con acceso rapido a la documentacion de cada microservicio:

| Microservicio  | Acceso desde Swagger Centralizado                            |
| -------------- | ------------------------------------------------------------ |
| usuario        | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| cliente        | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| vendedor       | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| vehiculo       | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| disponibilidad | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| reserva        | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| pago           | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| alquiler       | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| inspeccion     | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |
| multa          | Disponible desde `http://localhost:8101/doc/swagger-ui.html` |

Esto permite probar los endpoints de todos los microservicios desde una sola interfaz, sin tener que abrir un link diferente para cada servicio.

---

## Swagger individual por microservicio

Ademas del Swagger centralizado en Gateway, cada microservicio tambien puede exponer su propia documentacion individual:

| Microservicio  | Swagger UI individual                           |
| -------------- | ----------------------------------------------- |
| usuario        | http://localhost:9091/doc/swagger-ui/index.html |
| cliente        | http://localhost:9092/doc/swagger-ui/index.html |
| vendedor       | http://localhost:9093/doc/swagger-ui/index.html |
| vehiculo       | http://localhost:9094/doc/swagger-ui/index.html |
| disponibilidad | http://localhost:9095/doc/swagger-ui/index.html |
| inspeccion     | http://localhost:9096/doc/swagger-ui/index.html |
| alquiler       | http://localhost:9097/doc/swagger-ui/index.html |
| pago           | http://localhost:9098/doc/swagger-ui/index.html |
| reserva        | http://localhost:9099/doc/swagger-ui/index.html |
| multa          | http://localhost:9100/doc/swagger-ui/index.html |

Para la demostracion del proyecto se recomienda utilizar principalmente el Swagger centralizado:

```txt
http://localhost:8101/doc/swagger-ui.html
```

De esta forma se demuestra que el **Gateway** funciona como entrada unica para acceder rapidamente a la documentacion y pruebas de todos los microservicios.

---

# 🚀 Como Ejecutar

## Requisitos previos

* Java 21
* Maven o Maven Wrapper `mvnw`
* MySQL ejecutandose en `localhost:3306`
* Usuario MySQL: `root`
* Contrasena MySQL: vacia
* IntelliJ IDEA o terminal

---

## Paso 1: Iniciar MySQL

Asegurarse de que MySQL este corriendo.

Ejemplo recomendado:

```txt
Laragon / XAMPP / MySQL Server
```

Las bases de datos se crean automaticamente con:

```txt
createDatabaseIfNotExist=true
```

---

## Paso 2: Iniciar Eureka

Eureka debe iniciarse primero:

```bash
cd eureka
./mvnw spring-boot:run
```

En Windows:

```bash
cd eureka
..\mvnw.cmd spring-boot:run
```

Verificar en:

```txt
http://localhost:8761
```

---

## Paso 3: Iniciar los microservicios

Luego iniciar los microservicios:

```bash
cd usuario && ../mvnw spring-boot:run
cd cliente && ../mvnw spring-boot:run
cd vendedor && ../mvnw spring-boot:run
cd vehiculo && ../mvnw spring-boot:run
cd disponibilidad && ../mvnw spring-boot:run
cd reserva && ../mvnw spring-boot:run
cd pago && ../mvnw spring-boot:run
cd alquiler && ../mvnw spring-boot:run
cd inspeccion && ../mvnw spring-boot:run
cd multa && ../mvnw spring-boot:run
```

En Windows:

```bash
cd usuario && ..\mvnw.cmd spring-boot:run
cd cliente && ..\mvnw.cmd spring-boot:run
cd vendedor && ..\mvnw.cmd spring-boot:run
cd vehiculo && ..\mvnw.cmd spring-boot:run
cd disponibilidad && ..\mvnw.cmd spring-boot:run
cd reserva && ..\mvnw.cmd spring-boot:run
cd pago && ..\mvnw.cmd spring-boot:run
cd alquiler && ..\mvnw.cmd spring-boot:run
cd inspeccion && ..\mvnw.cmd spring-boot:run
cd multa && ..\mvnw.cmd spring-boot:run
```

---

## Paso 4: Iniciar Gateway

Gateway se inicia al final:

```bash
cd gateway
./mvnw spring-boot:run
```

En Windows:

```bash
cd gateway
..\mvnw.cmd spring-boot:run
```

Verificar que aparezca en Eureka como:

```txt
GATEWAY    UP (1)
```

---

# 🔎 Como comprobar el funcionamiento

## Comprobar Eureka

Abrir:

```txt
http://localhost:8761
```

Debe mostrar los microservicios registrados como **UP**.

---

## Comprobar Gateway

Primero probar un microservicio directamente:

```txt
GET http://localhost:9094/api/vehiculos
```

Luego probar el mismo endpoint usando Gateway:

```txt
GET http://localhost:8101/api/vehiculos
```

Si ambas respuestas coinciden, Gateway esta redirigiendo correctamente.

---

## Comprobar DataFaker

Probar endpoints por Gateway:

```txt
GET http://localhost:8101/api/usuarios
GET http://localhost:8101/api/clientes
GET http://localhost:8101/api/vendedores
GET http://localhost:8101/api/vehiculos
```

Si DataFaker funciona correctamente, los endpoints deben devolver datos falsos.

Ejemplo:

```json
[
  {
    "id": 1,
    "marca": "Toyota",
    "modelo": "Corolla",
    "color": "Rojo",
    "ubicacion": "Santiago"
  }
]
```

Si aparece:

```json
[]
```

significa que la peticion llego correctamente, pero no existen datos cargados o el perfil `dev` no esta activo.

---

## Comprobar Swagger Centralizado

Abrir:

```txt
http://localhost:8101/doc/swagger-ui.html
```

Desde este enlace se puede seleccionar la documentacion de cada microservicio sin abrir links separados.

Tambien se pueden probar rapidamente las rutas principales desde el mismo Swagger centralizado.

---

# 🔄 Flujo Completo de Prueba por Gateway

Ejemplo usando Gateway como entrada unica:

```bash
# 1. Listar usuarios generados por DataFaker
curl http://localhost:8101/api/usuarios

# 2. Listar clientes generados por DataFaker
curl http://localhost:8101/api/clientes

# 3. Listar vendedores generados por DataFaker
curl http://localhost:8101/api/vendedores

# 4. Listar vehiculos generados por DataFaker
curl http://localhost:8101/api/vehiculos

# 5. Listar disponibilidad
curl http://localhost:8101/api/disponibilidad

# 6. Listar reservas
curl http://localhost:8101/api/reserva

# 7. Listar pagos
curl http://localhost:8101/api/pago

# 8. Listar alquileres
curl http://localhost:8101/api/alquiler
```

---

# 🧪 Pruebas Unitarias

Para ejecutar pruebas unitarias:

```bash
cd <microservicio>
./mvnw test
```

En Windows:

```bash
cd <microservicio>
..\mvnw.cmd test
```

Las pruebas usan:

```txt
JUnit 5 + Mockito
```

---

# 📁 Estructura del Repositorio

```txt
AlquilerVehiculos/
├── README.md
├── pom.xml
├── eureka/
├── gateway/
├── usuario/
├── cliente/
├── vendedor/
├── vehiculo/
├── disponibilidad/
├── reserva/
├── pago/
├── alquiler/
├── inspeccion/
└── multa/
```

# 📄 Licencia

Proyecto academico — Duoc UC · Carrera de Ingenieria en Informatica
