-- Windows [Ruta instalación Oracle]
-- create tablespace bo datafile '[C:\]oraclexe\oradata\bo.dbf' size 2048M autoextend on;

-- Linux [Ruta instalación Oracle]
create tablespace bo datafile '[/usr/lib]/oracle/xe/oradata/bo.dbf' size 1024M autoextend on;

-- Creación del usuario office
create user office identified by office default tablespace BO temporary tablespace TEMP; 

grant resource, connect, dba, create table, create view, create procedure, 
create sequence, create trigger to office;

-- Conectarse como usuario office para correr los scripts ddl (data-description-language).
-- Windows Sql+ 
--Abrir una ventana de comandos cmd
sqlplus office/office

-- Linux Sql+
--Abrir una terminal
cd /usrs/lib/oracle/xe/app/oracle/product/10.2.0/server/bin
./sqlplus tjavao/tjavao9po