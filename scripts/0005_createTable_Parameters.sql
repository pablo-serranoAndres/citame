use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_parameters()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'parameters'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE parameters (id bigint not null COMMENT 'PK',
                           idBusiness bigint not null COMMENT 'FK business. Negocio al que pertenece el parámetro',
                           name varchar(32) not null COMMENT 'Clave de parámetro (único por negocio)',
                           caption varchar(128) not null COMMENT 'Texto en edición del parámetro',
                           typeValue integer not null COMMENT 'Tipo de parámetro: 0=check, 1=string (auxValue=length max), 2=integer (auxValue=rango), 3=float (idem), 4=string desplegable (auxValue=valores separados por |)',
                           defaultValue varchar(512) COMMENT 'Valor por defecto del parámetro',
                           auxValue varchar(512) COMMENT 'Varios dependdiendo de type',
                           primary key (id)) engine=InnoDB;
	ALTER TABLE parameters ADD CONSTRAINT fk_parameters_business FOREIGN KEY (idBusiness) REFERENCES business (id);
    ALTER TABLE parameters ADD CONSTRAINT uk_parameters_name unique (idBusiness, name);
END IF;
END //

CREATE PROCEDURE CreateTable_parameters_service()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'parameters_service'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE parameters_service (idService bigint not null COMMENT 'PK FK-services',
                           idParameter bigint not null COMMENT 'PK FK-parameters',
                           position int not null COMMENT 'Orden al preguntar',
                           required tinyint(1) DEFAULT 0 COMMENT 'Valor requerido en la reserva del servicio',
                           storeUserParam bigint COMMENT 'Si no null parámetro de usuario donde se guarda el valor del parámetro de reserva',
                           recoverUserParam bigint COMMENT 'Si no null parámetro de usuario de donde se recupera el valor del parámetro de reserva',
                           defaultValue varchar(512) COMMENT 'Valor por defecto del parámetro al reservar este servicio (prioritario al valor defecto del parámetro)',
                           primary key (idService, idParameter)) engine=InnoDB;
	ALTER TABLE parameters_service ADD CONSTRAINT fk_paramserv_services FOREIGN KEY (idService) REFERENCES services (id);
	ALTER TABLE parameters_service ADD CONSTRAINT fk_paramserv_parameters FOREIGN KEY (idParameter) REFERENCES parameters (id);
END IF;
END //

CREATE PROCEDURE CreateTable_parameters_user()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'parameters_user'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE parameters_user (idParameter bigint not null COMMENT 'PK FK-parameters',
                           position int not null COMMENT 'Orden al editar',
                           required tinyint(1) DEFAULT 0 COMMENT 'Valor requerido en la reserva',
                           showed tinyint(1) DEFAULT 0 COMMENT 'Valor mostrado en la reserva si no guardado',
                           edited tinyint(1) DEFAULT 0 COMMENT 'Valor editado en la reserva',
                           primary key (idParameter)) engine=InnoDB;
	ALTER TABLE parameters_user ADD CONSTRAINT fk_paramuser_parameters FOREIGN KEY (idParameter) REFERENCES parameters (id);
END IF;
END //

CREATE PROCEDURE CreateTable_parameters_user_value()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'parameters_user_value'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE parameters_user_value (idUser bigint not null COMMENT 'PK FK-user_login',
                                        idParameter bigint not null COMMENT 'PK FK-parameters',
                                        value varchar(512) COMMENT 'Valor del parámetro para el usuario',
                                        created datetime(0) not null COMMENT 'Fecha y hora creación valor',
										modificated datetime(0) COMMENT 'Fecha y hora última modificación valor',
                                        primary key (idUser, idParameter)) engine=InnoDB;
	ALTER TABLE parameters_user_value ADD CONSTRAINT fk_paramuserval_users FOREIGN KEY (idUser) REFERENCES user_login (id);
	ALTER TABLE parameters_user_value ADD CONSTRAINT fk_paramuserval_parameters FOREIGN KEY (idParameter) REFERENCES parameters (id);
END IF;
END //

CREATE PROCEDURE CreateTable_parameters_service_value()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'parameters_service_value'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE parameters_service_value (idAppointment bigint not null COMMENT 'PK FK-appointments',
                                        idParameter bigint not null COMMENT 'PK FK-parameters',
                                        value varchar(512) COMMENT 'Valor del parámetro para la reserva',
                                        created datetime(0) not null COMMENT 'Fecha y hora creación valor',
										modificated datetime(0) COMMENT 'Fecha y hora última modificación valor',
                                        primary key (idAppointment, idParameter)) engine=InnoDB;
	ALTER TABLE parameters_service_value ADD CONSTRAINT fk_paramappointval_appointments FOREIGN KEY (idAppointment) REFERENCES appointments (id);
	ALTER TABLE parameters_service_value ADD CONSTRAINT fk_paramappointval_parameters FOREIGN KEY (idParameter) REFERENCES parameters (id);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_parameters();
CALL CreateTable_parameters_service();
CALL CreateTable_parameters_user();
CALL CreateTable_parameters_user_value();
CALL CreateTable_parameters_service_value();
DROP PROCEDURE CreateTable_parameters;
DROP PROCEDURE CreateTable_parameters_service;
DROP PROCEDURE CreateTable_parameters_user;
DROP PROCEDURE CreateTable_parameters_user_value;
DROP PROCEDURE CreateTable_parameters_service_value;
COMMIT;
