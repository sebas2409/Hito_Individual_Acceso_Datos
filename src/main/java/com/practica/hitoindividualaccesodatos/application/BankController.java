package com.practica.hitoindividualaccesodatos.application;

import com.practica.hitoindividualaccesodatos.service.dto.AccountDto;
import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.service.BankServices;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DespositDto;
import com.practica.hitoindividualaccesodatos.util.AccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BankController {

    private final BankServices bankServices;
    private final AccountMapper accountMapper;

    public BankController(BankServices bankServices, AccountMapper accountMapper) {
        this.bankServices = bankServices;
        this.accountMapper = accountMapper;
    }

    @PostMapping("create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto) {
        var account = accountMapper.toAccount(accountDto);
        bankServices.createAccount(account);
        return new ResponseEntity<>(account, HttpStatus.CREATED);
    }

    @PostMapping("deposit")
    public ResponseEntity<DepositResponse> depositFunds(@RequestBody DespositDto despositDto) {
        var rs = bankServices.depositFunds(despositDto);
        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }
}