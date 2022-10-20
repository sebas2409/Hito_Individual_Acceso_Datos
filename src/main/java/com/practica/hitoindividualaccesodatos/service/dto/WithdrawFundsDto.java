package com.practica.hitoindividualaccesodatos.service.dto;

import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;

public record WithdrawFundsDto(String clientId, Double amount, TransactionDbType dbType) {
}
