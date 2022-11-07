package com.practica.hitoindividualaccesodatos.application;

import com.opencsv.bean.CsvToBeanBuilder;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.service.dto.*;
import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.service.BankServices;
import com.practica.hitoindividualaccesodatos.util.AccountMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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


    @GetMapping("accounts")
    @CrossOrigin
    public void getCsvMysqlAccounts(HttpServletResponse response) {
        bankServices.getCsvAccounts(bankServices.getAllMysqlAccounts(), response, "mysql");
    }


    @GetMapping("transactions")
    public void getCsvMysqlTransactions(HttpServletResponse response) {
        bankServices.getCsvTransacctions(bankServices.getAllMysqlTransactions(), response, "mysql");
    }


    @GetMapping("login/{id}")
    public ResponseEntity<DepositResponse> login(@PathVariable String id) {
        System.out.println(id);
        var rs = bankServices.login(id);
        System.out.println(rs);
        if (rs != null) {
            return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(DepositResponse.builder().id("").nombre("").balance(0.0).build(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("account/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String id) {
        var rs = bankServices.getAccount(id);
        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @GetMapping("transaction/{id}")
    public ResponseEntity<ArrayList<Transaction>> getAllTransaction(@PathVariable String id) {
        var rs = bankServices.getTransactionsById(id);
        return new ResponseEntity<>(rs, HttpStatus.ACCEPTED);
    }

    @PostMapping("uploadaccount")
    public void uploadCsv(@RequestParam("file") MultipartFile multipartFile) {

        try {
            var reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
            var csvToBean = new CsvToBeanBuilder<>(reader)
                    .withType(AccountCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Object> listaCsv = csvToBean.parse();
            System.out.println(listaCsv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("uploadtransaction")
    public void uploadTransactionCsv(@RequestParam("file") MultipartFile file){
        try {
            var reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            var csvToBean = new CsvToBeanBuilder<>(reader)
                    .withType(TransaccionCsv.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<Object> listaCsv = csvToBean.parse();
            System.out.println(listaCsv);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
