package com.practica.hitoindividualaccesodatos.util;

import com.practica.hitoindividualaccesodatos.service.dto.AccountDto;
import com.practica.hitoindividualaccesodatos.domain.Account;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class AccountMapper {

    public Account toAccount(AccountDto accountDto) {
        return Account.builder()
                .id(UUID.randomUUID().toString())
                .nombre(accountDto.nombre())
                .balance(accountDto.balance())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }
}
