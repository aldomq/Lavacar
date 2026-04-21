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

INSERT INTO usuario (username, password, nombre, email, rol)
SELECT * FROM (
    SELECT 'admin', '1234', 'Administrador', 'admin@lavacar.com', 'ADMIN'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE username = 'admin'
);

INSERT INTO categoria (nombre, descripcion)
SELECT * FROM (
    SELECT 'Shampoo', 'Productos para lavado exterior'
    UNION ALL
    SELECT 'Ceras', 'Productos para brillo y proteccion'
    UNION ALL
    SELECT 'Accesorios', 'Microfibras, esponjas y aromatizantes'
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM categoria
);

INSERT INTO producto (nombre, descripcion, precio, existencias, imagen_url, id_categoria)
SELECT * FROM (
    SELECT 'Shampoo con cera', 'Limpieza concentrada para carroceria', 6500, 12,
           'https://images.pexels.com/photos/9470891/pexels-photo-9470891.jpeg?auto=compress&cs=tinysrgb&w=900', 1
    UNION ALL
    SELECT 'Cera liquida premium', 'Acabado brillante y proteccion de pintura', 8900, 8,
           'https://images.pexels.com/photos/12920558/pexels-photo-12920558.jpeg?auto=compress&cs=tinysrgb&w=900', 2
    UNION ALL
    SELECT 'Pano de microfibra', 'Microfibra suave para secado sin rayas', 2500, 25,
           'https://images.pexels.com/photos/6872572/pexels-photo-6872572.jpeg?auto=compress&cs=tinysrgb&w=900', 3
    UNION ALL
    SELECT 'Aromatizante interior', 'Fragancia fresca para cabina', 3200, 18,
           'https://images.pexels.com/photos/14999946/pexels-photo-14999946.jpeg?auto=compress&cs=tinysrgb&w=900', 3
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM producto
);

UPDATE producto
SET imagen_url = 'https://images.pexels.com/photos/9470891/pexels-photo-9470891.jpeg?auto=compress&cs=tinysrgb&w=900'
WHERE nombre = 'Shampoo con cera';

UPDATE producto
SET imagen_url = 'https://images.pexels.com/photos/12920558/pexels-photo-12920558.jpeg?auto=compress&cs=tinysrgb&w=900'
WHERE nombre = 'Cera liquida premium';

UPDATE producto
SET imagen_url = 'https://images.pexels.com/photos/6872572/pexels-photo-6872572.jpeg?auto=compress&cs=tinysrgb&w=900'
WHERE nombre IN ('Pano de microfibra', 'Paño de microfibra', 'PaÃ±o de microfibra');

UPDATE producto
SET imagen_url = 'https://images.pexels.com/photos/14999946/pexels-photo-14999946.jpeg?auto=compress&cs=tinysrgb&w=900'
WHERE nombre = 'Aromatizante interior';

INSERT INTO tipo_lavado (nombre, descripcion, precio, duracion_minutos, activo)
SELECT * FROM (
    SELECT 'Lavado basico', 'Lavado exterior con secado rapido', 5000, 25, TRUE
    UNION ALL
    SELECT 'Lavado premium', 'Exterior, aspirado y cera liquida', 9000, 45, TRUE
    UNION ALL
    SELECT 'Lavado ejecutivo', 'Detalle completo con limpieza interior', 15000, 70, TRUE
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM tipo_lavado
);

INSERT INTO cola_lavado (cliente_nombre, cliente_contacto, placa, vehiculo, estado, observaciones, id_tipo_lavado)
SELECT * FROM (
    SELECT 'Carlos Mena', '8888-1111', 'ABC123', 'Toyota Hilux', 'PENDIENTE', 'Revisar lodo en guardabarros', 2
    UNION ALL
    SELECT 'Daniela Rojas', '8787-2323', 'KLM908', 'Hyundai Accent', 'EN_PROCESO', 'Agregar aromatizante vainilla', 1
) AS tmp
WHERE NOT EXISTS (
    SELECT 1 FROM cola_lavado
);
