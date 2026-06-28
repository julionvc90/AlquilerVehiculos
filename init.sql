-- =========================================================
-- Datos de prueba - AlquilerVehiculos
-- Se cargan automaticamente al iniciar Docker por primera vez
-- =========================================================

CREATE DATABASE IF NOT EXISTS usuario_db;
CREATE DATABASE IF NOT EXISTS cliente_db;
CREATE DATABASE IF NOT EXISTS vendedor_db;
CREATE DATABASE IF NOT EXISTS vehiculo_db;
CREATE DATABASE IF NOT EXISTS disponibilidad_db;
CREATE DATABASE IF NOT EXISTS Reserva_db;
CREATE DATABASE IF NOT EXISTS pago_db;
CREATE DATABASE IF NOT EXISTS alquiler_db;
CREATE DATABASE IF NOT EXISTS inspeccion_db;
CREATE DATABASE IF NOT EXISTS multa_db;

-- ===================== USUARIOS =====================
USE usuario_db;
INSERT INTO usuario (username, password, email, rol, activo) VALUES
('jperez',   '123456', 'jperez@mail.com',   'CLIENTE',  true),
('mgonzalez','123456', 'mgonzalez@mail.com', 'CLIENTE',  true),
('crodriguez','123456','crodriguez@mail.com','CLIENTE',  true),
('afernandez','123456','afernandez@mail.com','CLIENTE',  true),
('lsanchez', '123456', 'lsanchez@mail.com',  'CLIENTE',  true),
('plopez',   '123456', 'plopez@mail.com',    'VENDEDOR', true),
('rgomez',   '123456', 'rgomez@mail.com',    'VENDEDOR', true),
('mdiaz',    '123456', 'mdiaz@mail.com',     'VENDEDOR', true),
('ftorres',  '123456', 'ftorres@mail.com',   'VENDEDOR', true),
('cvasquez', '123456', 'cvasquez@mail.com',  'VENDEDOR', true);

-- ===================== CLIENTES =====================
USE cliente_db;
INSERT INTO cliente (rut, nombre, apellido, email, telefono, direccion, usuario_id, activo) VALUES
('11111111-1','Juan',     'Perez',     'jperez@mail.com',    '+56911111111','Av. Providencia 100', 1, true),
('22222222-2','Maria',    'Gonzalez',  'mgonzalez@mail.com', '+56922222222','Calle Merced 200',    2, true),
('33333333-3','Carlos',   'Rodriguez', 'crodriguez@mail.com','+56933333333','Av. Las Condes 300',  3, true),
('44444444-4','Ana',      'Fernandez', 'afernandez@mail.com','+56944444444','Calle Huerfanos 400', 4, true),
('55555555-5','Luis',     'Sanchez',   'lsanchez@mail.com',  '+56955555555','Av. Tobalaba 500',    5, true);

-- ===================== VENDEDORES =====================
USE vendedor_db;
INSERT INTO vendedor (rut, nombre, apellido, email, telefono, usuario_id, activo) VALUES
('66666666-6','Pedro',    'Lopez',   'plopez@mail.com',  '+56966666666',6,  true),
('77777777-7','Rosa',     'Gomez',   'rgomez@mail.com',  '+56977777777',7,  true),
('88888888-8','Miguel',   'Diaz',    'mdiaz@mail.com',   '+56988888888',8,  true),
('99999999-9','Francisca','Torres',  'ftorres@mail.com', '+56999999999',9,  true),
('10101010-0','Carolina', 'Vasquez', 'cvasquez@mail.com','+56900000000',10, true);

-- ===================== VEHICULOS =====================
USE vehiculo_db;
INSERT INTO vehiculo (patente, marca, modelo, anio, categoria, capacidad_pasajeros, color, vendedor_id, tarifa_diaria, ubicacion, activo) VALUES
('AB1234','Toyota',  'Corolla',2024,'Sedan',    '5','Rojo',  1, 25000,'Santiago', true),
('CD5678','Hyundai', 'Accent', 2023,'Sedan',    '5','Azul',  2, 20000,'Providencia', true),
('EF9012','Kia',     'Rio',    2024,'Hatchback','5','Negro', 3, 22000,'Las Condes', true),
('GH3456','Nissan',  'Versa',  2022,'Sedan',    '5','Blanco',4, 18000,'Ñuñoa', true),
('IJ7890','Mazda',   'Mazda3', 2024,'Sedan',    '5','Gris',  5, 28000,'Santiago', true);

-- ===================== DISPONIBILIDAD =====================
USE disponibilidad_db;
INSERT INTO disponibilidad (vehiculo_id, fecha_inicio, fecha_fin, disponible) VALUES
(1, '2025-07-01', '2025-12-31', true),
(2, '2025-07-01', '2025-12-31', true),
(3, '2025-07-01', '2025-12-31', true),
(4, '2025-07-01', '2025-12-31', true),
(5, '2025-07-01', '2025-12-31', true);

-- ===================== RESERVAS =====================
USE Reserva_db;
INSERT INTO reservas (id_cliente, id_vehiculo, fecha_reserva, fecha_inicio, fecha_termino, total_dias, valor_dia, total_reserva, estado_reserva, observaciones_reserva) VALUES
(1, 1, '2025-06-20', '2025-07-01', '2025-07-05', 4, 25000, 100000, 'Confirmada', 'Reserva de prueba'),
(2, 2, '2025-06-21', '2025-07-10', '2025-07-13', 3, 20000, 60000,  'Confirmada', 'Reserva de prueba'),
(3, 3, '2025-06-22', '2025-07-15', '2025-07-18', 3, 22000, 66000,  'Confirmada', 'Reserva de prueba');

-- ===================== PAGOS =====================
USE pago_db;
INSERT INTO pago (id_reserva, id_vehiculo, fecha_pago, monto_pago, metodo_pago, estado_pago, transaccion_pago) VALUES
(1, 1, '2025-06-21', 100000, 'Transferencia', 'Pagada',    'TXN00001'),
(2, 2, '2025-06-22', 60000,  'Tarjeta',       'Pagada',    'TXN00002'),
(3, 3, '2025-06-23', 66000,  'WebPay',         'Ingresada', 'TXN00003');

-- ===================== ALQUILERES =====================
USE alquiler_db;
INSERT INTO alquiler (cliente_id, vehiculo_id, reserva_id, fecha_inicio, fecha_fin, dias, tarifa_diaria, monto_total, estado) VALUES
(1, 1, 1, '2025-07-01', '2025-07-05', 4, 25000, 100000, 'Reservado'),
(2, 2, 2, '2025-07-10', '2025-07-13', 3, 20000, 60000,  'ACTIVO'),
(3, 3, 3, '2025-07-15', '2025-07-18', 3, 22000, 66000,  'Reservado');

-- ===================== INSPECCIONES =====================
USE inspeccion_db;
INSERT INTO inspeccion (alquiler_id, vehiculo_id, fecha_inspeccion, tipo_inspeccion, resultado, observaciones, inspector, activo) VALUES
(1, 1, '2025-07-01 10:00:00', 'Entrega',   'Aprobado',  'Vehiculo en buen estado', 'Carlos Rojas', true),
(2, 2, '2025-07-10 10:00:00', 'Entrega',   'Aprobado',  'Sin observaciones',      'Carlos Rojas', true),
(3, 3, '2025-07-15 10:00:00', 'Entrega',   'Aprobado',  'Tanque lleno',           'Carlos Rojas', true);

-- ===================== MULTAS =====================
USE multa_db;
INSERT INTO multas (id_reserva, id_vehiculo, motivo_multa, fecha_multa, monto_multa, estado_multa) VALUES
(1, 1, 'Devolucion tardia 1 dia', '2025-07-06', 30000, 'Pendiente'),
(2, 2, 'Limpieza extra',          '2025-07-14', 15000, 'Pagada');
