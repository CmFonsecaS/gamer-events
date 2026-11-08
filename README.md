# Gamer Events — ISY2202 Semana 3

Sitio web de eventos gamer con Spring Boot, Spring Security, Thymeleaf, JWT y MySQL (Docker).
Proyecto académico con ejecución local (MySQL en Docker).

## Arquitectura

```
Navegador ──HTTP──> Frontend (Spring Boot + Thymeleaf + Spring Security)  :8090
                         │  login y llamadas con JWT (Authorization: Bearer)
                         ▼
                    Backend (Spring Boot API REST + Spring Security + JWT) :8091
                         │  JPA (consultas parametrizadas)
                         ▼
                    MySQL 8.4 en Docker (contenedor gamer-events-mysql)     :3307
```

| Carpeta | Contenido |
|---|---|
| `frontend/` | Páginas Thymeleaf, CSS, controladores MVC, login contra el backend |
| `backend/` | API REST, login con BCrypt, emisión y validación de JWT |
| `database/` | `01_schema.sql` (creación), `02_datos.sql` (inserción), `03_usuario_app.sh` (usuario de solo lectura) |
| `docker-compose.yml` | Base de datos MySQL |

## Rutas

| Ruta | Acceso | Función |
|---|---|---|
| `http://localhost:8090/eventos` (también `/` y `/home`) | Pública | Inicio: eventos recientes, populares y banners |
| `http://localhost:8090/eventos/buscar` | Pública | Búsqueda por nombre, videojuego, categoría, país/ciudad, fecha y modalidad |
| `http://localhost:8090/eventos/{id}` | Privada (rol USER o ADMIN) | Detalle del evento |
| `POST http://localhost:8091/login` | Pública | API de login, devuelve JWT. Acepta JSON (`username`, `password`) o formulario (`user`, `encryptedPass`) |
| `GET http://localhost:8091/api/publico/**` | Pública | API de inicio, filtros y búsqueda |
| `GET http://localhost:8091/api/eventos/{id}` | Privada (JWT) | API de detalle |

## Usuarios de prueba

| Usuario | Contraseña |
|---|---|
| `jugador1` | `Gamer#2026` |
| `jugador2` | `Duoc#Seguro26` |

En la base solo se guarda el hash BCrypt. Tras 5 intentos fallidos el usuario queda bloqueado 5 minutos.

## Cómo ejecutar (Windows, PowerShell)

Requisitos: Java 21, Docker Desktop.

`mvnw.cmd` usa la variable `JAVA_HOME`. Si apunta a otra versión de Java, indicar Java 21 en la
terminal antes de ejecutar (ajustar la ruta a la instalación local):

```powershell
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21.0.10.7-hotspot"
```

1. Copiar `.env.example` como `.env` y completar las claves (no se sube a GitHub).
2. Base de datos:
   ```powershell
   cd C:\Proyectos\gamer-events
   docker compose up -d
   ```
3. Backend (en una terminal):
   ```powershell
   cd C:\Proyectos\gamer-events\backend
   .\mvnw.cmd spring-boot:run
   ```
4. Frontend (en otra terminal):
   ```powershell
   cd C:\Proyectos\gamer-events\frontend
   .\mvnw.cmd spring-boot:run
   ```
5. Abrir `http://localhost:8090/eventos`.

## Pruebas automáticas

```powershell
cd backend;  .\mvnw.cmd test   # 14 pruebas (requiere MySQL levantado)
cd frontend; .\mvnw.cmd test   # 20 pruebas (backend simulado)
```
