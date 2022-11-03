package com.practica.hitoindividualaccesodatos.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;
import com.practica.hitoindividualaccesodatos.service.dto.AccountResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DespositDto;
import com.practica.hitoindividualaccesodatos.service.dto.WithdrawFundsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class BankServices {

    private final BankManager bankManager;

    public BankServices(BankManager bankManager) {
        this.bankManager = bankManager;
    }

    public void createAccount(Account account) {
        var uuid = UUID.randomUUID().toString();
        bankManager.createAccount(account);
        bankManager.createTransaction(new Transaction(uuid,
                account.getId(),
                TransactionType.CREAR,
                0.0,
                account.getFechaCreacion()));
    }

    public Object withdrawFunds(WithdrawFundsDto withdrawFundsDto) {
        var rs = bankManager.withdrawFunds(withdrawFundsDto.clientId(), withdrawFundsDto.amount());
        if (rs) {
            bankManager.createTransaction(new Transaction(
                    UUID.randomUUID().toString(),
                    withdrawFundsDto.clientId(),
                    TransactionType.RETIRAR,
                    withdrawFundsDto.amount(),
                    LocalDate.now()
            ));
            return bankManager.getAccountById(withdrawFundsDto.clientId());
        } else {
            return false;
        }
    }

    public DepositResponse depositFunds(DespositDto depositDto) {
        bankManager.depositFunds(depositDto.clientId(), depositDto.amount());
        bankManager.createTransaction(new Transaction(UUID.randomUUID().toString(),
                depositDto.clientId(),
                TransactionType.INGRESAR,
                depositDto.amount(),
                LocalDate.now()));
        return bankManager.getAccountById(depositDto.clientId());
    }

    public void deleteAccount(String clientId) {
        bankManager.deleteTransaction(clientId);
        bankManager.deleteAccount(clientId);

    }


    public ArrayList<Account> getAllMysqlAccounts() {
        return bankManager.getAllAccounts();
    }

    public void getCsvAccounts(ArrayList<Account> lista, HttpServletResponse response, String nombreArchivo) {
        var filename = "cuenta_" + nombreArchivo + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        try {
            var writer = new StatefulBeanToCsvBuilder<Account>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();
            writer.write(lista);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    public void getCsvTransacctions(ArrayList<Transaction> lista, HttpServletResponse response, String nombreArchivo) {
        var filename = "transaccion_" + nombreArchivo + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        try {
            var writer = new StatefulBeanToCsvBuilder<Transaction>(response.getWriter())
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(false)
                    .build();
            writer.write(lista);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Transaction> getAllMysqlTransactions() {
        return bankManager.getAllTransactions();
    }


    public DepositResponse login(String id) {
        return bankManager.login(id);
    }

    public AccountResponse getAccount(String id) {
        return bankManager.getAccountById2(id);
    }

    public ArrayList<Transaction> getTransactionsById(String id) {
        return bankManager.getAllTransactionsFromId(id);
    }
}
