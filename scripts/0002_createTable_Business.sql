use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_Business()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'Business'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE Business (id bigint not null COMMENT 'PK',
                           idString varchar(32) not null COMMENT 'Identificador visible único del negocio',
                           email varchar(256) COMMENT 'Correo electrónico (puede estar repetido y no ser válido)',
                           phone varchar(16) COMMENT 'Teléfono (puede estar repatido y no ser válido)',
                           name varchar(80) not null COMMENT 'Nombre negocio',
                           registrationDate datetime(6) COMMENT 'Fecha alta',
                           deactivationDate datetime(6) COMMENT 'Fecha baja (si no null el negocio está desactivado)',
                           bookingDays int COMMENT 'Días posibles de reservas anticipadas (null sin limite)',
                           primary key (id)) engine=InnoDB;
	ALTER TABLE Business ADD CONSTRAINT UK_Business_idString unique (idString);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_Business();
DROP PROCEDURE CreateTable_Business;
COMMIT;
