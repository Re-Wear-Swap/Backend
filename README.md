<h1 align="center">♻️ Re-Wear Swap - Backend</h1>

## 📖 Sobre el Proyecto

**Re-Wear Swap** es una API REST para la plataforma de intercambio de ropa de segunda mano basada en un sistema de puntos. Gestiona usuarios, artículos y reservas, permitiendo a los usuarios publicar prendas, explorar el catálogo y reservar artículos para intercambiar.

El proyecto fue desarrollado como parte del bootcamp **FemCoders P8 Barcelona 2025**, siguiendo un flujo de trabajo profesional con **Spring Boot**, **JPA/Hibernate**, **arquitectura por capas** y **testing con Mockito**.

La API se consume desde un frontend en React ([ver repositorio Frontend](https://github.com/Re-Wear-Swap/Frontend)).

### 🎯 Objetivos del Proyecto

- Construir una API REST completa con Spring Boot
- Implementar persistencia con JPA/Hibernate y PostgreSQL
- Diseñar un sistema de puntos para gestionar intercambios
- Aplicar arquitectura por capas (Controller → Service → Repository)
- Automatizar la expiración de reservas con un Scheduler
- Escribir tests unitarios con Mockito
- Aplicar Gitflow y metodologías Agile

---

## ✨ Funcionalidades Principales

- 👤 **Gestión de usuarios** — registro, login y perfil con foto
- 📦 **CRUD de artículos** — crear, listar, editar y eliminar prendas
- 🔍 **Filtros avanzados** — por categoría, rango de fechas y paginación
- 🔄 **Sistema de reservas** — reservar, cancelar y confirmar intercambios
- ⭐ **Sistema de puntos** — cada usuario empieza con 3 puntos, reservar cuesta 1, confirmar intercambio otorga 1
- ⏰ **Expiración automática** — las reservas expiran a las 24h y se devuelve el punto
- 🌐 **CORS configurado** — preparado para comunicación con el frontend

---

## 🛠️ Tecnologías Utilizadas

| Tecnología      | Uso                               |
| --------------- | --------------------------------- |
| Java 21         | Lenguaje principal                |
| Spring Boot 3.5 | Framework backend                 |
| Spring Data JPA | Acceso a datos con Hibernate      |
| PostgreSQL      | Base de datos relacional          |
| Maven           | Gestión de dependencias y build   |
| Lombok          | Reducción de boilerplate          |
| Mockito         | Tests unitarios                   |
| spring-dotenv   | Variables de entorno desde `.env` |

---

## 📁 Estructura del Proyecto

```
src/main/java/com/rewear/rewear/
│
├── config/                  # Configuración
│   ├── CorsConfig.java        → Configuración CORS global
│   └── ReservationScheduler.java → Expiración automática de reservas (cada 1h)
│
├── controller/              # Controladores REST
│   ├── UserController.java
│   ├── ArticleController.java
│   └── ReservationController.java
│
├── entity/                  # Entidades JPA
│   ├── User.java
│   ├── Article.java
│   ├── Reservation.java
│   └── enums/
│       ├── ArticleStatus.java   → DISPONIBLE, RESERVADO, INTERCAMBIADO
│       ├── Category.java        → CAMISETAS, PANTALONES, CHAQUETAS...
│       └── ItemCondition.java   → NUEVO, USADO_BUEN_ESTADO, USADO_REGULAR
│
├── repository/              # Repositorios Spring Data
│   ├── UserRepository.java
│   ├── ArticleRepository.java
│   └── ReservationRepository.java
│
└── service/                 # Lógica de negocio
    ├── UserService.java & UserServiceImpl.java
    ├── ArticleService.java & ArticleServiceImpl.java
    └── ReservationService.java & ReservationServiceImpl.java
```

> La arquitectura sigue el patrón **Controller → Service → Repository** con interfaces para desacoplar la lógica de negocio.

---

## 🚀 Instalación y Uso

### Requisitos previos

- **Java 21** o superior
- **Maven 3**
- **PostgreSQL** instalado y corriendo en el puerto `5432`

### Pasos de instalación

1. **Clonar el repositorio**

```bash
git clone https://github.com/Re-Wear-Swap/Backend.git
cd Backend
```

2. **Crear la base de datos**

Abre **pgAdmin** o tu terminal de PostgreSQL y ejecuta:

```sql
CREATE DATABASE rewear;
```

3. **Configurar variables de entorno**

```bash
cp .env.example .env
```

Edita el archivo `.env` con tus credenciales:

```env
DB_USERNAME=tu_usuario_postgres
DB_PASSWORD=tu_contraseña_postgres
```

4. **Arrancar la aplicación**

```bash
mvn spring-boot:run
```

Las tablas se crean automáticamente al arrancar gracias a Hibernate (`ddl-auto=update`).

La API estará disponible en `http://localhost:8080`.

---

## 🗄️ Modelo de Datos

### User

| Campo   | Tipo    | Descripción                     |
| ------- | ------- | ------------------------------- |
| id      | Integer | PK, auto-generado               |
| name    | String  | Nombre del usuario (máx. 100)   |
| email   | String  | Email único (máx. 150)          |
| isAdult | Boolean | Mayor de edad                   |
| points  | Integer | Puntos disponibles (default: 3) |
| photo   | String  | URL de foto de perfil           |

### Article

| Campo         | Tipo          | Descripción                                                                          |
| ------------- | ------------- | ------------------------------------------------------------------------------------ |
| id            | Integer       | PK, auto-generado                                                                    |
| title         | String        | Título del artículo (máx. 150)                                                       |
| description   | String        | Descripción (TEXT)                                                                   |
| imageUrl      | String        | URL de la imagen                                                                     |
| category      | Enum          | `CAMISETAS`, `PANTALONES`, `CHAQUETAS`, `VESTIDOS`, `ZAPATOS`, `ACCESORIOS`, `OTROS` |
| articleStatus | Enum          | `DISPONIBLE`, `RESERVADO`, `INTERCAMBIADO`                                           |
| itemCondition | Enum          | `NUEVO`, `USADO_BUEN_ESTADO`, `USADO_REGULAR`                                        |
| createdAt     | LocalDateTime | Fecha de creación (auto)                                                             |
| user_id       | FK → User     | Usuario propietario                                                                  |

### Reservation

| Campo      | Tipo          | Descripción                |
| ---------- | ------------- | -------------------------- |
| id         | Integer       | PK, auto-generado          |
| reservedAt | LocalDateTime | Fecha de reserva (auto)    |
| expiresAt  | LocalDateTime | Expiración (reserva + 24h) |
| article_id | FK → Article  | Artículo reservado (único) |
| user_id    | FK → User     | Usuario que reserva        |

---

## 🔗 Endpoints de la API

### 👤 Users — `/api/users`

| Método | Endpoint           | Descripción                     |
| ------ | ------------------ | ------------------------------- |
| POST   | `/api/users`       | Crear usuario                   |
| POST   | `/api/users/login` | Login (params: `name`, `email`) |
| GET    | `/api/users/{id}`  | Obtener usuario por ID          |

### 📦 Articles — `/api/articles`

| Método | Endpoint             | Descripción                                                              |
| ------ | -------------------- | ------------------------------------------------------------------------ |
| POST   | `/api/articles`      | Crear artículo                                                           |
| GET    | `/api/articles`      | Listar artículos (paginado, filtros: `category`, `startDate`, `endDate`) |
| GET    | `/api/articles/{id}` | Obtener artículo por ID                                                  |
| PUT    | `/api/articles/{id}` | Actualizar artículo                                                      |
| DELETE | `/api/articles/{id}` | Eliminar artículo                                                        |

### 🔄 Reservations — `/api/reservations`

| Método | Endpoint                                | Descripción                                   |
| ------ | --------------------------------------- | --------------------------------------------- |
| POST   | `/api/reservations`                     | Crear reserva (params: `articleId`, `userId`) |
| DELETE | `/api/reservations/{id}`                | Cancelar reserva                              |
| PUT    | `/api/reservations/{articleId}/confirm` | Confirmar intercambio                         |

---

## ⭐ Sistema de Puntos

| Acción                 | Puntos                     |
| ---------------------- | -------------------------- |
| Registro de usuario    | +3 (iniciales)             |
| Reservar un artículo   | -1                         |
| Cancelar una reserva   | +1 (devolución)            |
| Confirmar intercambio  | +1 (al propietario)        |
| Reserva expirada (24h) | +1 (devolución automática) |

---

## ⏰ Scheduler — Expiración de Reservas

Un `@Scheduled` se ejecuta **cada hora** y comprueba las reservas expiradas. Para cada una:

1. Devuelve 1 punto al usuario
2. Cambia el estado del artículo a `DISPONIBLE`
3. Elimina la reserva

---

## 👩‍💻 Equipo

| Desarrolladora              | GitHub                                                   | LinkedIn                                                         |
| --------------------------- | -------------------------------------------------------- | ---------------------------------------------------------------- |
| **Marie-Charlotte Doulcet** | [@Charlottedoulcet](https://github.com/Charlottedoulcet) | [LinkedIn](https://www.linkedin.com/in/marie-charlotte-doulcet/) |
| **Maria Eva Martin**        | [@Eva-87](https://github.com/Eva-87)                     | [LinkedIn](https://www.linkedin.com/in/maria-eva-martin/)        |
| **Anna Costa**              | [@annahico](https://github.com/annahico)                 | [LinkedIn](https://www.linkedin.com/in/annahico/)                |
| **Jen Ceballos**            | [@jenCeballos](https://github.com/JenCeballos)           | [LinkedIn](https://www.linkedin.com/in/jen-ceballos/)            |

> 💜 Proyecto desarrollado durante el **FemCoders P8 Barcelona Bootcamp 2025 - 2026**

---

## 🌱 Posibles Mejoras Futuras

- 🔐 Autenticación con Spring Security y JWT
- 📸 Endpoint de subida de imágenes
- 🧪 Ampliar los tests
- 🐳 Dockerización del proyecto
- 📊 Dashboard de estadísticas de intercambios

---
