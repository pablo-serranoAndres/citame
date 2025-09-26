use citame;

-- Crear el procedimiento almacenado
-- Es necesario cambiar el delimitador de sentencia para grabar todo el procedure como una sentencia
DELIMITER //
CREATE PROCEDURE CreateTable_messages()
BEGIN
-- para saber si existe la tabla antes de intentar crearla
SET @tabla_existe = (
    SELECT COUNT(*) FROM information_schema.tables
    WHERE table_schema = 'citame' AND table_name = 'messages'
);
-- crear la tabla si no existe
IF @tabla_existe = 0 THEN
	CREATE TABLE messages (id bigint not null COMMENT 'PK',
                           idAppointment bigint COMMENT 'FK appointments opcional. Reserva asociada a la conversación',
						   idUser bigint not null COMMENT 'FK user_login. Usuario/cliente de la conversación',
                           idBusiness bigint not null COMMENT 'FK business. Negocio de la conversación',
                           idUserBusiness bigint COMMENT 'FK user_login opcional. Usuario del negocio al que pertenece la conversación',
                           fromUser tinyint(1) not null default 0 COMMENT 'Origen del mensaje (0=negocio, 1=usuario)',
                           created datetime(0) not null COMMENT 'Fecha y hora escritura mensaje',
                           showed datetime(0) COMMENT 'Fecha y hora mostrado mensaje',
                           emailed datetime(0) COMMENT 'Fecha y hora envío por email',
                           deleted datetime(0) COMMENT 'Fecha y hora borrado mensaje',
                           message varchar(256) not null COMMENT 'Mensaje',
						   primary key (id)) engine=InnoDB;
	ALTER TABLE messages ADD CONSTRAINT fk_messages_appointments FOREIGN KEY (idAppointment) REFERENCES appointments (id);
	ALTER TABLE messages ADD CONSTRAINT fk_messages_user_login FOREIGN KEY (idUser) REFERENCES user_login (id);
	ALTER TABLE messages ADD CONSTRAINT fk_messages_business FOREIGN KEY (idBusiness) REFERENCES business (id);
	ALTER TABLE messages ADD CONSTRAINT fk_messages_user_business FOREIGN KEY (idUserBusiness) REFERENCES user_login (id);
END IF;
END //

DELIMITER ;  -- restaurar el delimitador normal

CALL CreateTable_messages();
DROP PROCEDURE CreateTable_messages;
COMMIT;
