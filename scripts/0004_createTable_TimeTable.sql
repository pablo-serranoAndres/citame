use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_timetable()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'timetable'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE timetable (id bigint not null COMMENT 'PK',
                           idBusiness bigint not null COMMENT 'FK business. Negocio al que pertenece el horario',
                           name varchar(80) COMMENT 'Nombre horario (opcional)',
                           validity varchar(32) COMMENT 'Expresión validez horario (null siempre) separados por comas: M-T-W-R-F-S-U (días de la semana)',
                           opening time not null COMMENT 'Hora de apertura',
                           closing time not null COMMENT 'Hora de cierre',
                           startDate date COMMENT 'Fecha comienzo validez horario (NULL sin limite)',
                           endDate date COMMENT 'Fecha final validez horario (NULL sin limite)',
                           primary key (id)) engine=InnoDB;
	ALTER TABLE timetable ADD CONSTRAINT fk_timetable_business FOREIGN KEY (idBusiness) REFERENCES business (id);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_timetable();
DROP PROCEDURE CreateTable_timetable;
COMMIT;
