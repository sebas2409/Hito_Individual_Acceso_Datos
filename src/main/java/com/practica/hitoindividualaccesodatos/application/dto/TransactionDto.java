package com.practica.hitoindividualaccesodatos.application.dto;

import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;

public record TransactionDto(String clientId, TransactionType transactionType, TransactionDbType dbType) {
}
