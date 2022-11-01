package com.practica.hitoindividualaccesodatos.util;

import com.github.javafaker.Faker;
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
                .cc(getAccountNumber())
                .balance(accountDto.balance())
                .fechaCreacion(LocalDateTime.now())
                .build();
    }

    public String getAccountNumber() {
        return "ES24" + Faker.instance().number().numberBetween(1111111111, 10000000000L);
    }
}
