package com.practica.hitoindividualaccesodatos.domain;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Transaction {
    private String id;
    private String AccountId;
    private TransactionType tipoTransaccion;
    private TransactionDbType tipoDb;
    private LocalDateTime timeStamp;
}
