use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_Services()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'services'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE services (id bigint not null COMMENT 'PK',
                           idBusiness bigint not null COMMENT 'FK business. Negocio al que pertenece el servicio',
                           name varchar(80) not null COMMENT 'Nombre servicio',
                           type int not null COMMENT 'Tipo funcionamiento servicio',
                           position int not null COMMENT 'Orden para mostrar', 
                           bookingStep int COMMENT 'Saltos en la hora de inicio de la reserva en minutos (NULL= general negocio)',
                           bookingDays int COMMENT 'Días poaibles de reserva anticipada (NULL general negocio)',
                           prevBookingDays int COMMENT 'Días pasados hoy posible reservar (NULL = general negocio)',
                           prevBookingMins int COMMENT 'Minutos pasados desde ahora posible reservar si prevBookingDays = 0 (NULL = general negocio)',
                           startDate datetime(6) not null COMMENT 'Fecha inicio servicio. Reservas no admitidas antes de esta fecha',
                           endDate datetime(6) COMMENT 'Fecha fin servicio. Reservas no admitidas después de esta fecha (si null sin límite)',
                           duration int NOT NULL DEFAULT 0 COMMENT 'Duración estándar del servicio (minutos)',
                           primary key (id)) engine=InnoDB;
	ALTER TABLE services ADD CONSTRAINT fk_services_business FOREIGN KEY (idBusiness) REFERENCES business (id);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_Services();
DROP PROCEDURE CreateTable_Services;
COMMIT;
