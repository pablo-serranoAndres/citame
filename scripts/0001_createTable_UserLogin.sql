use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_UserLogin()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'User_login'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE User_login (id bigint not null COMMENT 'PK', 
                             email varchar(256) COMMENT 'Correo electrónico. Unico porque sirve para loguear (alternativo a phone)',
                             phone varchar(16) COMMENT 'Teléfono. Unico porque sirve para loguear (alternativo a email)',
                             name varchar(32) not null COMMENT 'Nombre (puede coincidir con otros usuarios)',
                             password varchar(128) COMMENT 'Contraseña encriptada',
                             verification varchar (16) COMMENT 'Código aleatorio generado automáticamente para verificación de email',
                             registrationDate datetime(6) COMMENT 'Fecha de alta',
                             deactivationDate datetime(6) COMMENT 'Fecha de baja (si no null el usuario está inactivo)',
                             emailRegistrationDate datetime(6) COMMENT 'Fecha de registro de email', 
                             emailVerificationDate datetime(6) COMMENT 'Fecha de verificación de email (si null pendiente de verificar y no se admite para login)',
                             mailMessages tinyint(1) NOT NULL DEFAULT 0 COMMENT 'Envío de mensajes por email',
                             primary key (id)) engine=InnoDB;
	ALTER TABLE User_login ADD CONSTRAINT UK_User_login_email unique (email);
	ALTER TABLE User_login ADD CONSTRAINT UK_User_login_phone unique (phone);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_UserLogin();
DROP PROCEDURE CreateTable_UserLogin;
COMMIT;
