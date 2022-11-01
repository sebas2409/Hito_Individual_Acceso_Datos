package com.practica.hitoindividualaccesodatos.application;

import com.practica.hitoindividualaccesodatos.service.dto.AccountDto;
import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.service.BankServices;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DespositDto;
import com.practica.hitoindividualaccesodatos.service.dto.WithdrawFundsDto;
import com.practica.hitoindividualaccesodatos.util.AccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PostMapping("delete/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable String id) {
        bankServices.deleteAccount(id);
        return ResponseEntity.ok("Eliminado Correctamente!");
    }

    @PostMapping("withdraw")
    public ResponseEntity<Object> withdrawFunds(@RequestBody WithdrawFundsDto withdrawFundsDto) {
        var rs = bankServices.withdrawFunds(withdrawFundsDto);
        if (Boolean.FALSE.equals(rs)) {
            return new ResponseEntity<>("No posees saldo suficiente", HttpStatus.FORBIDDEN);
        } else {
            return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
        }
    }


    @GetMapping("accounts-mysql")
    public void getCsvMysqlAccounts(HttpServletResponse response) {
        bankServices.getCsvAccounts(bankServices.getAllMysqlAccounts(), response, "mysql");
    }


    @GetMapping("transactions-mysql")
    public void getCsvMysqlTransactions(HttpServletResponse response) {
        bankServices.getCsvTransacctions(bankServices.getAllMysqlTransactions(), response, "mysql");
    }


    @GetMapping("login/{id}")
    public ResponseEntity<String> login(@PathVariable String id) {
        System.out.println(id);
        var rs = bankServices.login(id);
        if (rs != null) {
            return ResponseEntity.status(HttpStatus.OK).body("");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }
    }

}
