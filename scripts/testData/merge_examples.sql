use citame;

INSERT IGNORE INTO user_login (`id`,`email`,`phone`,`name`,`password`,`verification`,`registrationDate`,`deactivationDate`,`emailRegistrationDate`,`emailVerificationDate`) VALUES (5,'sebas_serrano@hotmail.com','+34665184004','Sebas','$2a$10$4HbKoihH17edfYyKBUbQquF4/FWNpRA9GIibGAkzZlwasjRq8cXi.','vdghh25grAHjnkl2','2025-08-16 21:22:01.725000',NULL,NULL,NULL);
INSERT IGNORE INTO user_login (`id`,`email`,`phone`,`name`,`password`,`verification`,`registrationDate`,`deactivationDate`,`emailRegistrationDate`,`emailVerificationDate`) VALUES (7,'rosario.andres@hotmail.com','+34675650793','Rosario','$2a$10$FI2fvkK3zRtEmJbcpfLNJObNb/UZKzyWApZ5Fw39/DxhvrURsW4Xq','zbGccNFxkqfsvDYB','2025-08-18 21:33:30.405000',NULL,NULL,NULL);
INSERT IGNORE INTO user_login (`id`,`email`,`phone`,`name`,`password`,`verification`,`registrationDate`,`deactivationDate`,`emailRegistrationDate`,`emailVerificationDate`) VALUES (8,'guest@citame.com','+34666999666','Pepe López','$2a$10$rtav3qXJImEgXLZHrrjyh.JeT.mZGSZ0NdpoECGkCsIOhRL.ThWZe','rj8HlU50K1XQhaGy','2025-09-24 09:30:32.331000',NULL,NULL,NULL);

INSERT IGNORE INTO business (`id`,`idString`,`email`,`phone`,`name`,`registrationDate`,`deactivationDate`,`bookingDays`,`prevBookingDays`,`prevBookingMins`,`bookingStep`) VALUES (1,'salonvaleras','salonvaleras@gmail.com',NULL,'Salón Valeras','2025-08-20 20:07:00.000000',NULL,60,0,30,15);
INSERT IGNORE INTO business (`id`,`idString`,`email`,`phone`,`name`,`registrationDate`,`deactivationDate`,`bookingDays`,`prevBookingDays`,`prevBookingMins`,`bookingStep`) VALUES (2,'prueba',NULL,NULL,'Negocio de prueba','2025-08-21 00:00:00.000000',NULL,30,NULL,NULL,NULL);

INSERT IGNORE INTO timetable (`id`,`idBusiness`,`name`,`validity`,`opening`,`closing`,`startDate`,`endDate`) VALUES (1,1,'Horario entre semana mañana','W,F','10:00:00','14:00:00',NULL,NULL);
INSERT IGNORE INTO timetable (`id`,`idBusiness`,`name`,`validity`,`opening`,`closing`,`startDate`,`endDate`) VALUES (2,1,'Horario entre semana tarde','M-F','17:30:00','20:00:00',NULL,NULL);
INSERT IGNORE INTO timetable (`id`,`idBusiness`,`name`,`validity`,`opening`,`closing`,`startDate`,`endDate`) VALUES (3,1,'Horario sábado','S','09:00:00','15:00:00',NULL,NULL);
INSERT IGNORE INTO timetable (`id`,`idBusiness`,`name`,`validity`,`opening`,`closing`,`startDate`,`endDate`) VALUES (4,1,'Horario caducado',NULL,'02:00:00','22:00:00','2025-01-01','2025-01-01');
INSERT IGNORE INTO timetable (`id`,`idBusiness`,`name`,`validity`,`opening`,`closing`,`startDate`,`endDate`) VALUES (5,1,'Nuevo horario','M-F','10:00:00','20:00:00','2025-09-01','2025-09-15');

INSERT IGNORE INTO services (`id`,`idBusiness`,`name`,`type`,`position`,`bookingStep`,`bookingDays`,`prevBookingDays`,`prevBookingMins`,`startDate`,`endDate`,`duration`) VALUES (1,1,'Corte de pelo',1,2,NULL,10,NULL,NULL,'2025-08-20 00:00:00.000000',NULL,20);
INSERT IGNORE INTO services (`id`,`idBusiness`,`name`,`type`,`position`,`bookingStep`,`bookingDays`,`prevBookingDays`,`prevBookingMins`,`startDate`,`endDate`,`duration`) VALUES (2,1,'Tinte',1,1,NULL,NULL,NULL,NULL,'2025-09-01 00:00:00.000000',NULL,45);

INSERT IGNORE INTO parameters (`id`,`idBusiness`,`name`,`caption`,`typeValue`,`defaultValue`,`auxValue`) VALUES (1,1,'observaciones_servicio','Observaciones',1,NULL,NULL);
INSERT IGNORE INTO parameters (`id`,`idBusiness`,`name`,`caption`,`typeValue`,`defaultValue`,`auxValue`) VALUES (2,1,'observaciones_cliente','Observaciones',1,NULL,NULL);
INSERT IGNORE INTO parameters (`id`,`idBusiness`,`name`,`caption`,`typeValue`,`defaultValue`,`auxValue`) VALUES (3,1,'pelo','Tipo de pelo',4,NULL,'Corto|Media melena|Melena');

INSERT IGNORE INTO parameters_user (`idParameter`,`position`,`required`,`showed`,`edited`) VALUES (2,1,0,0,0);
INSERT IGNORE INTO parameters_user (`idParameter`,`position`,`required`,`showed`,`edited`) VALUES (3,2,0,0,0);

INSERT IGNORE INTO parameters_service (`idService`,`idParameter`,`position`,`required`,`storeUserParam`,`recoverUserParam`,`defaultValue`) VALUES (1,1,2,0,NULL,NULL,NULL);
INSERT IGNORE INTO parameters_service (`idService`,`idParameter`,`position`,`required`,`storeUserParam`,`recoverUserParam`,`defaultValue`) VALUES (1,3,1,1,3,3,NULL);
INSERT IGNORE INTO parameters_service (`idService`,`idParameter`,`position`,`required`,`storeUserParam`,`recoverUserParam`,`defaultValue`) VALUES (2,1,1,0,NULL,NULL,NULL);

COMMIT;
