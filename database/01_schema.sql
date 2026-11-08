-- =====================================================================
-- Gamer Events - Script de creación de la base de datos (MySQL 8.4)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS gamer_events
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE gamer_events;

-- Usuarios de prueba para la API de login. La contraseña se guarda solo como hash BCrypt.
CREATE TABLE usuario (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  username      VARCHAR(50)  NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  nombre        VARCHAR(100) NOT NULL,
  rol           VARCHAR(20)  NOT NULL DEFAULT 'USER',
  activo        BOOLEAN      NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  CONSTRAINT uk_usuario_username UNIQUE (username),
  CONSTRAINT ck_usuario_rol CHECK (rol IN ('USER', 'ADMIN'))
) ENGINE = InnoDB;

CREATE TABLE categoria (
  id     BIGINT      NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(60) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_categoria_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE videojuego (
  id     BIGINT      NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL,
  genero VARCHAR(60) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uk_videojuego_nombre UNIQUE (nombre)
) ENGINE = InnoDB;

CREATE TABLE organizador (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  nombre    VARCHAR(100) NOT NULL,
  email     VARCHAR(120) NOT NULL,
  sitio_web VARCHAR(200) NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

CREATE TABLE evento (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  nombre            VARCHAR(120)  NOT NULL,
  resumen           VARCHAR(255)  NOT NULL,
  descripcion       TEXT          NOT NULL,
  videojuego_id     BIGINT        NOT NULL,
  categoria_id      BIGINT        NOT NULL,
  organizador_id    BIGINT        NOT NULL,
  modalidad         VARCHAR(12)   NOT NULL,
  fecha_inicio      DATETIME      NOT NULL,
  fecha_fin         DATETIME      NOT NULL,
  pais              VARCHAR(60)   NOT NULL,
  ciudad            VARCHAR(60)   NOT NULL,
  direccion         VARCHAR(200)  NULL,
  enlace_acceso     VARCHAR(255)  NULL,
  requisitos        TEXT          NOT NULL,
  info_inscripcion  TEXT          NOT NULL,
  precio            INT           NOT NULL DEFAULT 0,
  cupos             INT           NOT NULL,
  inscritos         INT           NOT NULL DEFAULT 0,
  imagen_portada    VARCHAR(200)  NOT NULL,
  fecha_publicacion DATETIME      NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_evento_videojuego  FOREIGN KEY (videojuego_id)  REFERENCES videojuego (id),
  CONSTRAINT fk_evento_categoria   FOREIGN KEY (categoria_id)   REFERENCES categoria (id),
  CONSTRAINT fk_evento_organizador FOREIGN KEY (organizador_id) REFERENCES organizador (id),
  CONSTRAINT ck_evento_modalidad CHECK (modalidad IN ('PRESENCIAL', 'ONLINE')),
  CONSTRAINT ck_evento_fechas    CHECK (fecha_fin >= fecha_inicio),
  CONSTRAINT ck_evento_cupos     CHECK (cupos > 0 AND inscritos >= 0 AND inscritos <= cupos),
  INDEX ix_evento_fecha_inicio (fecha_inicio),
  INDEX ix_evento_publicacion (fecha_publicacion),
  INDEX ix_evento_inscritos (inscritos)
) ENGINE = InnoDB;

CREATE TABLE evento_foto (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  evento_id   BIGINT       NOT NULL,
  url         VARCHAR(200) NOT NULL,
  descripcion VARCHAR(150) NOT NULL,
  orden       INT          NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  CONSTRAINT fk_foto_evento FOREIGN KEY (evento_id) REFERENCES evento (id) ON DELETE CASCADE
) ENGINE = InnoDB;

-- Banners de avisos comerciales de la página de inicio.
CREATE TABLE banner (
  id      BIGINT       NOT NULL AUTO_INCREMENT,
  titulo  VARCHAR(100) NOT NULL,
  texto   VARCHAR(200) NOT NULL,
  imagen  VARCHAR(200) NOT NULL,
  activo  BOOLEAN      NOT NULL DEFAULT TRUE,
  orden   INT          NOT NULL DEFAULT 1,
  PRIMARY KEY (id)
) ENGINE = InnoDB;
