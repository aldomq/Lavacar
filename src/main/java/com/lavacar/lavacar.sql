DROP DATABASE IF EXISTS lavacar_db;
CREATE DATABASE lavacar_db;
USE lavacar_db;

CREATE TABLE usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100),
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE,
    rol VARCHAR(50) DEFAULT 'USER'
);

CREATE TABLE categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE producto (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    existencias INT NOT NULL,
    imagen_url VARCHAR(500),
    id_categoria BIGINT,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (id_categoria)
        REFERENCES categoria(id_categoria)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

CREATE TABLE tipo_lavado (
    id_tipo_lavado BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    duracion_minutos INT,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE cola_lavado (
    id_cola_lavado BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_nombre VARCHAR(100) NOT NULL,
    cliente_contacto VARCHAR(50) NOT NULL,
    placa VARCHAR(20) NOT NULL,
    vehiculo VARCHAR(100) NOT NULL,
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    inicio_proceso DATETIME NULL,
    fin_estimada DATETIME NULL,
    observaciones VARCHAR(255),
    id_tipo_lavado BIGINT,
    CONSTRAINT fk_cola_tipo_lavado
        FOREIGN KEY (id_tipo_lavado)
        REFERENCES tipo_lavado(id_tipo_lavado)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);
