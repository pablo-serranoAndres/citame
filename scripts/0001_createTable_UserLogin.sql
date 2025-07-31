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
	CREATE TABLE User_login (id bigint not null, email varchar(256), phone varchar(16),
                             name varchar(32) not null, password varchar(128),
                             verificacion varchar (16), fecha_alta datetime(6), fecha_baja datetime(6),
                             fecha_registro_email datetime(6), fecha_verificacion_email datetime(6),
                             primary key (id)) engine=InnoDB;
	ALTER TABLE User_login ADD CONSTRAINT UK_User_login_email unique (email);
	ALTER TABLE User_login ADD CONSTRAINT UK_User_login_phone unique (phone);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_UserLogin();
DROP PROCEDURE CreateTable_UserLogin;
COMMIT;
