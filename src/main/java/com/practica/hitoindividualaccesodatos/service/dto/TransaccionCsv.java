package com.practica.hitoindividualaccesodatos.service.dto;

import com.opencsv.bean.CsvBindByName;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;

import java.time.LocalDate;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TransaccionCsv {
    @CsvBindByName(column = "ID")
    private String id;
    @CsvBindByName(column = "IDCLIENTE")
    private String idCliente;
    @CsvBindByName(column = "TIPOTRANSACCION")
    private TransactionType tipoTransaccion;
    @CsvBindByName(column = "IMPORTE")
    private Double importe;
    @CsvBindByName(column = "TIMESTAMP")
    private String timeStamp;
}
