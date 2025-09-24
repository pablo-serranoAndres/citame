use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_appointments()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'appointments'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE appointments (id bigint not null COMMENT 'PK',
                               idService bigint not null COMMENT 'FK services. Servicio que se reserva',
                               idUser bigint not null COMMENT 'FK user_login. Usuario/cliente que reserva',
                               bookingDate date not null COMMENT 'Fecha de la reserva',
                               startTime time not null COMMENT 'Hora inicio servicio',
                               endTime time not null COMMENT 'Hora término servicio',
                               created datetime(0) not null COMMENT 'Fecha y hora registro reserva',
                               modificated datetime(0) COMMENT 'Fecha y hora última modificación',
                               finished datetime(0) COMMENT 'Fecha y hora cierre registro (realizado,anulado, no presentado...)',
                               confirmed tinyint(1) not null default 0 COMMENT 'Reserva confirmada por negocio',
                               realized tinyint(1) not null default 0 COMMENT 'Servicio realizado',
                               notPresented tinyint(1) not null default 0 COMMENT 'Cliente no se presentó al servicio',
                               primary key (id)) engine=InnoDB;
	ALTER TABLE appointments ADD CONSTRAINT fk_appointments_services FOREIGN KEY (idService) REFERENCES services (id);
    ALTER TABLE appointments ADD CONSTRAINT uk_appointments_users FOREIGN KEY (idUser) REFERENCES user_login (id);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_appointments();
DROP PROCEDURE CreateTable_appointments;
COMMIT;
