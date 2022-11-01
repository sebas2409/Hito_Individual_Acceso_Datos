create database banco;
use banco;

create table cuenta(
                       id varchar(50) primary key,
                       nombre varchar(50),
                       cc varchar(100),
                       balance double,
                       fechaCreacion varchar(50)
);

create table transaccion(
                            id varchar(50) primary key,
                            idcliente varchar(50),
                            tipoTransaccion varchar(50),
                            importe double,
                            fechaTransaccion varchar(50),
                            foreign key transaccion(idCliente) references cuenta(id)

);
-------------
create database banco;
create table cuenta(
                       id varchar(50) primary key,
                       nombre varchar(50),
                       balance decimal,
                       fechaCreacion varchar(50)
);

create table transaccion(
                            id varchar(50) primary key,
                            idcliente varchar(50) references cuenta(id), --columna problematica
                            tipotransaccion varchar(50),
                            tipodb varchar(50),
                            fechatransaccion varchar(50)
);