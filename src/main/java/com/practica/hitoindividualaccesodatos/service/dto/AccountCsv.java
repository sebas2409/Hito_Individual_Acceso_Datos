package com.practica.hitoindividualaccesodatos.service.dto;

import com.opencsv.bean.CsvBindByName;

import java.time.LocalDate;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccountCsv {
    @CsvBindByName(column = "ID")
    private String id;
    @CsvBindByName(column = "NOMBRE")
    private String nombre;
    @CsvBindByName(column = "DNI")
    private String dni;
    @CsvBindByName(column = "CC")
    private String cc;
    @CsvBindByName(column = "BALANCE")
    private Double balance;
    @CsvBindByName(column = "FECHACREACION")
    private String fechaCreacion;
}
