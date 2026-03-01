-- ============================================================
--  SISTEMA DE ALQUILER DE VEHICULOS — RentaCar
--  Script SQL: CREATE TABLE + INDEX + INSERT pruebas
--  Autor: Luis Fernando Marte (MOD 01)
-- ============================================================

CREATE DATABASE IF NOT EXISTS rentacar_db
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_spanish_ci;

USE rentacar_db;

-- ============================================================
-- TABLA 1: usuarios
-- ============================================================
CREATE TABLE usuarios (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(100)  NOT NULL,
    email         VARCHAR(100)  NOT NULL UNIQUE,
    password_hash VARCHAR(255)  NOT NULL,
    rol           ENUM('ADMIN','GERENTE','RECEPCIONISTA','SOLO_LECTURA') NOT NULL DEFAULT 'RECEPCIONISTA',
    activo        TINYINT(1)    NOT NULL DEFAULT 1,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLA 2: vehiculos
-- ============================================================
CREATE TABLE vehiculos (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    marca           VARCHAR(50)  NOT NULL,
    modelo          VARCHAR(50)  NOT NULL,
    placa           VARCHAR(20)  NOT NULL UNIQUE,
    color           VARCHAR(30)  NOT NULL,
    anio            YEAR         NOT NULL,
    capacidad       INT          NOT NULL DEFAULT 5,
    km_inicial      INT          NOT NULL DEFAULT 0,
    km_actual       INT          NOT NULL DEFAULT 0,
    categoria       VARCHAR(40)  NOT NULL,
    combustible     ENUM('GASOLINA','DIESEL','ELECTRICO','HIBRIDO') NOT NULL DEFAULT 'GASOLINA',
    transmision     ENUM('MANUAL','AUTOMATICA') NOT NULL DEFAULT 'AUTOMATICA',
    precio_base_dia DECIMAL(10,2) NOT NULL,
    estado          ENUM('DISPONIBLE','ALQUILADO','MANTENIMIENTO','AVERIADO') NOT NULL DEFAULT 'DISPONIBLE',
    notas           TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLA 3: clientes
-- ============================================================
CREATE TABLE clientes (
    id                  INT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    cedula              VARCHAR(20)  NOT NULL UNIQUE,
    pasaporte           VARCHAR(30),
    licencia_conducir   VARCHAR(30)  NOT NULL,
    telefono            VARCHAR(20)  NOT NULL,
    email               VARCHAR(100),
    direccion           VARCHAR(200),
    en_lista_negra      TINYINT(1)   NOT NULL DEFAULT 0,
    motivo_lista_negra  TEXT,
    notas               TEXT,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- TABLA 4: reservas
-- ============================================================
CREATE TABLE reservas (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id      INT          NOT NULL,
    vehiculo_id     INT          NOT NULL,
    fecha_inicio    DATE         NOT NULL,
    fecha_fin       DATE         NOT NULL,
    hora_recogida   TIME         NOT NULL DEFAULT '08:00:00',
    hora_devolucion TIME         NOT NULL DEFAULT '08:00:00',
    lugar_recogida  VARCHAR(100) NOT NULL,
    extras          VARCHAR(300),          -- ej: "GPS,SEGURO_BASICO,SILLA_BEBE"
    precio_total    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    estado          ENUM('CONFIRMADA','ACTIVA','CANCELADA','FINALIZADA') NOT NULL DEFAULT 'CONFIRMADA',
    notas           TEXT,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id)  REFERENCES clientes(id)  ON DELETE RESTRICT,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id) ON DELETE RESTRICT
);

-- ============================================================
-- TABLA 5: contratos
-- ============================================================
CREATE TABLE contratos (
    id                          INT AUTO_INCREMENT PRIMARY KEY,
    reserva_id                  INT          NOT NULL UNIQUE,
    fecha_generacion            DATE         NOT NULL,
    km_inicial                  INT          NOT NULL,
    nivel_combustible_inicial   ENUM('LLENO','3/4','MEDIO','1/4','VACIO') NOT NULL DEFAULT 'LLENO',
    notas_danos_preexistentes   TEXT,
    estado                      ENUM('ACTIVO','FINALIZADO','RETRASADO') NOT NULL DEFAULT 'ACTIVO',
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE RESTRICT
);

-- ============================================================
-- TABLA 6: pagos
-- ============================================================
CREATE TABLE pagos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    reserva_id   INT           NOT NULL,
    monto        DECIMAL(10,2) NOT NULL,
    metodo_pago  ENUM('EFECTIVO','TARJETA','TRANSFERENCIA') NOT NULL,
    fecha        DATE          NOT NULL,
    notas        TEXT,
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (reserva_id) REFERENCES reservas(id) ON DELETE RESTRICT
);

-- ============================================================
-- TABLA 7: danos
-- ============================================================
CREATE TABLE danos (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id  INT           NOT NULL,
    reserva_id   INT,
    cliente_id   INT,
    descripcion  TEXT          NOT NULL,
    costo        DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    fecha        DATE          NOT NULL,
    estado       ENUM('PENDIENTE','COBRADO','PERDONADO') NOT NULL DEFAULT 'PENDIENTE',
    created_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id)  ON DELETE RESTRICT,
    FOREIGN KEY (reserva_id)  REFERENCES reservas(id)   ON DELETE SET NULL,
    FOREIGN KEY (cliente_id)  REFERENCES clientes(id)   ON DELETE SET NULL
);

-- ============================================================
-- TABLA 8: mantenimientos
-- ============================================================
CREATE TABLE mantenimientos (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    vehiculo_id    INT           NOT NULL,
    tipo           VARCHAR(80)   NOT NULL,
    fecha          DATE          NOT NULL,
    km             INT           NOT NULL,
    proximo_km     INT,
    proxima_fecha  DATE,
    costo          DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    notas          TEXT,
    created_at     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculos(id) ON DELETE CASCADE
);

-- ============================================================
-- INDICES
-- ============================================================
CREATE INDEX idx_vehiculos_placa    ON vehiculos(placa);
CREATE INDEX idx_vehiculos_estado   ON vehiculos(estado);
CREATE INDEX idx_clientes_cedula    ON clientes(cedula);
CREATE INDEX idx_reservas_estado    ON reservas(estado);
CREATE INDEX idx_reservas_fecha_ini ON reservas(fecha_inicio);
CREATE INDEX idx_reservas_cliente   ON reservas(cliente_id);
CREATE INDEX idx_reservas_vehiculo  ON reservas(vehiculo_id);
CREATE INDEX idx_danos_vehiculo     ON danos(vehiculo_id);
CREATE INDEX idx_mant_vehiculo      ON mantenimientos(vehiculo_id);

-- ============================================================
-- INSERT DE PRUEBA
-- ============================================================

-- Usuarios
INSERT INTO usuarios (nombre, email, password_hash, rol) VALUES
('Admin Principal',   'admin@rentacar.com',        SHA2('admin123',256),   'ADMIN'),
('Carlos Gerente',    'gerente@rentacar.com',       SHA2('gerente123',256), 'GERENTE'),
('Maria Recepcion',   'recepcion1@rentacar.com',    SHA2('recep123',256),   'RECEPCIONISTA'),
('Pedro Recepcion',   'recepcion2@rentacar.com',    SHA2('recep456',256),   'RECEPCIONISTA'),
('Ana Lectura',       'lectura@rentacar.com',       SHA2('lect123',256),    'SOLO_LECTURA');

-- Vehiculos
INSERT INTO vehiculos (marca, modelo, placa, color, anio, capacidad, km_inicial, km_actual, categoria, combustible, transmision, precio_base_dia, estado) VALUES
('Toyota',    'Corolla',   'A123BC', 'Blanco',  2022, 5, 15000, 17200, 'Sedan',    'GASOLINA', 'AUTOMATICA', 1800.00, 'DISPONIBLE'),
('Honda',     'CR-V',      'B456DE', 'Negro',   2023, 5, 5000,  6800,  'SUV',      'GASOLINA', 'AUTOMATICA', 2500.00, 'DISPONIBLE'),
('Hyundai',   'Tucson',    'C789FG', 'Gris',    2021, 5, 30000, 32100, 'SUV',      'GASOLINA', 'AUTOMATICA', 2200.00, 'ALQUILADO'),
('Kia',       'Sportage',  'D012HI', 'Azul',    2022, 5, 20000, 21500, 'SUV',      'DIESEL',   'AUTOMATICA', 2300.00, 'MANTENIMIENTO'),
('Chevrolet', 'Spark',     'E345JK', 'Rojo',    2020, 4, 45000, 47000, 'Compacto', 'GASOLINA', 'MANUAL',      1200.00, 'DISPONIBLE');

-- Clientes
INSERT INTO clientes (nombre, cedula, pasaporte, licencia_conducir, telefono, email, direccion) VALUES
('Juan Perez',       '001-1234567-8', NULL,        'LIC-001234', '809-555-1001', 'juan@email.com',    'Calle 1, Santo Domingo'),
('Maria Gonzalez',   '002-2345678-9', NULL,        'LIC-002345', '809-555-1002', 'maria@email.com',   'Av. Independencia 45'),
('Carlos Rodriguez', '003-3456789-0', NULL,        'LIC-003456', '809-555-1003', 'carlos@email.com',  'Los Jardines, SDN'),
('Ana Martinez',     '004-4567890-1', 'PA123456',  'LIC-004567', '809-555-1004', 'ana@email.com',     'Calle 5, Santiago'),
('Roberto Sanchez',  '005-5678901-2', NULL,        'LIC-005678', '809-555-1005', 'roberto@email.com', 'Bella Vista, SD');

-- Reservas
INSERT INTO reservas (cliente_id, vehiculo_id, fecha_inicio, fecha_fin, hora_recogida, hora_devolucion, lugar_recogida, extras, precio_total, estado) VALUES
(1, 1, '2025-03-01', '2025-03-05', '09:00:00', '09:00:00', 'Oficina Central', 'GPS,SEGURO_BASICO',      8100.00,  'FINALIZADA'),
(2, 2, '2025-03-10', '2025-03-15', '10:00:00', '10:00:00', 'Aeropuerto SDQ',  'SEGURO_COMPLETO',        13700.00, 'FINALIZADA'),
(3, 3, '2025-04-01', '2025-04-07', '08:00:00', '08:00:00', 'Oficina Central', 'GPS,CONDUCTOR_ADICIONAL',16900.00, 'ACTIVA'),
(4, 5, '2025-04-15', '2025-04-18', '11:00:00', '11:00:00', 'Hotel Jaragua',   NULL,                     3600.00,  'CONFIRMADA'),
(5, 1, '2025-05-01', '2025-05-03', '09:00:00', '09:00:00', 'Oficina Central', 'SILLA_BEBE',             3800.00,  'CONFIRMADA');

-- Contratos
INSERT INTO contratos (reserva_id, fecha_generacion, km_inicial, nivel_combustible_inicial, notas_danos_preexistentes, estado) VALUES
(1, '2025-03-01', 15000, 'LLENO',  'Sin danos preexistentes',              'FINALIZADO'),
(2, '2025-03-10', 5000,  'LLENO',  'Pequeno rayon en puerta trasera dcha', 'FINALIZADO'),
(3, '2025-04-01', 30000, '3/4',    'Sin danos preexistentes',              'ACTIVO'),
(4, '2025-04-15', 47000, 'LLENO',  'Sin danos preexistentes',              'ACTIVO'),
(5, '2025-05-01', 17200, 'LLENO',  'Sin danos preexistentes',              'ACTIVO');

-- Pagos
INSERT INTO pagos (reserva_id, monto, metodo_pago, fecha, notas) VALUES
(1, 8100.00,  'TARJETA',       '2025-03-01', 'Pago completo al check-out'),
(2, 7000.00,  'TRANSFERENCIA', '2025-03-10', 'Adelanto 50%'),
(2, 6700.00,  'TARJETA',       '2025-03-15', 'Saldo restante al check-in'),
(3, 8450.00,  'EFECTIVO',      '2025-04-01', 'Adelanto 50%'),
(4, 3600.00,  'TARJETA',       '2025-04-15', 'Pago total por adelantado');

-- Danos
INSERT INTO danos (vehiculo_id, reserva_id, cliente_id, descripcion, costo, fecha, estado) VALUES
(2, 2, 2, 'Rayon superficial puerta trasera derecha',  1500.00, '2025-03-15', 'COBRADO'),
(3, 3, 3, 'Golpe menor en paragolpe delantero',         3500.00, '2025-04-07', 'PENDIENTE'),
(1, 1, 1, 'Mancha en tapizado asiento conductor',        500.00, '2025-03-05', 'PERDONADO'),
(5, NULL, NULL, 'Vidrio trasero fisurado (preexistente)', 0.00,  '2025-01-10', 'PERDONADO'),
(4, NULL, NULL, 'Fuga leve de aceite',                   2000.00,'2025-02-20', 'PENDIENTE');

-- Mantenimientos
INSERT INTO mantenimientos (vehiculo_id, tipo, fecha, km, proximo_km, proxima_fecha, costo, notas) VALUES
(1, 'Cambio de aceite',     '2025-01-10', 15000, 20000, '2025-07-10', 800.00,  'Aceite 5W-30 sintético'),
(2, 'Revision general',     '2025-02-01', 5000,  10000, '2025-08-01', 1200.00, 'Sin novedad'),
(3, 'Cambio de frenos',     '2025-01-20', 28000, 38000, '2026-01-20', 3500.00, 'Pastillas delanteras'),
(4, 'Cambio de correa',     '2025-03-05', 20000, 30000, '2026-03-05', 2800.00, 'Correa de distribución'),
(5, 'Cambio de aceite',     '2025-02-15', 44000, 49000, '2025-08-15', 700.00,  'Aceite convencional');