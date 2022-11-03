package com.practica.hitoindividualaccesodatos.domain;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Transaction {
    private String id;
    private String idCliente;
    private TransactionType tipoTransaccion;
    private Double importe;
    private LocalDate timeStamp;
}
