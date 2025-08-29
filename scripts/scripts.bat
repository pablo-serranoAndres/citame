@echo off
setlocal enabledelayedexpansion

set "path_mysql_bin=C:\Program Files\MySQL\MySQL Server 8.0\bin"

rem Configuración
rem guardar en directorio actual el directorio del bat
rem todos los scripts estaran en este directorio
set "directorio_scripts=%~dp0"

set "usuario=citame"
set "contrasena=Ninguna.1999"
set "nombre_bd=citame"
set "host=localhost"

rem consultar ultima version ejecutada
"%path_mysql_bin%\mysql.exe" -u %usuario% -p%contrasena% -h %host% --batch -N -e "SELECT Version FROM Version" %nombre_bd% > result.txt
for /f "tokens=1" %%a in ('type result.txt') do (
    set "valor_base=%%a"
)
del result.txt

echo start in %directorio_scripts% desde version %valor_base%...
set "sorted_scripts="

rem Iterar sobre los archivos en el directorio y almacenar en un array
rem %%~nxf obtiene el nombre completo del archivo sin path, %%~nf sin extension
for %%f in (%directorio_scripts%*.sql) do (
    set "script_name=%%~nxf"
    
    rem Extraer la versión del script hasta el guion bajo (_)
	rem for /f procesa tokens separados por delims
    for /f "tokens=1 delims=_" %%a in ("!script_name!") do (
        set "script_version=%%a"
    )

    rem Almacenar el nombre del script y la versión en un array
	rem si es mayor que valor_base
	if !script_version! gtr %valor_base% (
		set "scripts[!script_version!]=!script_name!"
		set "sorted_scripts=!sorted_scripts! !script_version!"	
	)
)

rem Crear un archivo temporal con las versiones ordenadas
(for %%a in (%sorted_scripts%) do echo %%a) > temp.txt
type temp.txt | sort > temp_order.txt
del temp.txt

set "version_ejecutada=0"
(for /f "tokens=*" %%a in ('type temp_order.txt') do (
    set "version=%%a"
    call :EjecutarScript
	if not !errorlevel! equ 0 (
		echo Error al ejecutar el script: !version!
		if !version_ejecutada! gtr 0 (
			call :UpdateVersion
			echo Ultimo ejecutado OK: !version_ejecutada!
		)
		del temp_order.txt
		exit /b 1
	)
	set "version_ejecutada=%%a"
))
del temp_order.txt

if !version_ejecutada! gtr 0 (
	call :UpdateVersion
	echo finalizado con version !version_ejecutada!
) else (
	echo Ninguna actualizacion pendiente
)

endlocal
goto :eof

:EjecutarScript
set "script_name=!scripts[%version%]!"
echo Ejecutando script %directorio_scripts%%script_name%
"%path_mysql_bin%\mysql.exe" --local-infile=1 -u %usuario% -p%contrasena% -h %host% %nombre_bd% < "%directorio_scripts%%script_name%"
goto :eof

:UpdateVersion
"%path_mysql_bin%\mysql.exe" -u %usuario% -p%contrasena% -h %host% -e "UPDATE Version SET Version='!version_ejecutada!'" %nombre_bd%
echo Actualizada version a !version_ejecutada!
goto :eof