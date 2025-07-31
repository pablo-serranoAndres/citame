-- Creacion de la base de datos y la tabla version

CREATE SCHEMA `citame` ;

CREATE USER 'citame'@'localhost' IDENTIFIED WITH 'mysql_native_password' BY 'Ninguna.1999';

-- Permisos globales
GRANT DROP, INDEX, TRIGGER, ALTER ROUTINE, CREATE ROUTINE, EVENT, 
      EXECUTE, FILE, PROCESS, RELOAD, REPLICATION CLIENT, 
      REPLICATION SLAVE, SHOW DATABASES, SHUTDOWN, SUPER 
      ON *.* TO 'citame'@'localhost';

-- Permisos específicos de la base de datos 'citame'
GRANT ALTER, CREATE, SELECT, SHOW VIEW, CREATE VIEW, CREATE TEMPORARY TABLES, 
      UPDATE, DELETE, INSERT, LOCK TABLES, REFERENCES 
      ON citame.* TO 'citame'@'localhost';

ALTER USER 'citame'@'localhost' WITH MAX_QUERIES_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 

MAX_CONNECTIONS_PER_HOUR 0 MAX_USER_CONNECTIONS 0;

USE citame;

-- tabla secuencias
	CREATE TABLE hibernate_sequences (
		sequence_name VARCHAR(255) NOT NULL,
		next_val BIGINT DEFAULT 1,
		PRIMARY KEY (sequence_name)
);
-- tabla version
	CREATE TABLE version (version varchar(6)) engine=InnoDB;
	INSERT INTO version VALUES ('0000');
    COMMIT;
