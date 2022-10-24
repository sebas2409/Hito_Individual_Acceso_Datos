package com.practica.hitoindividualaccesodatos.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DespositDto;
import com.practica.hitoindividualaccesodatos.service.dto.WithdrawFundsDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
        try {
            var uuid = UUID.randomUUID().toString();
            bankManager.checkDb(TransactionDbType.MYSQL);
            bankManager.createAccount(account);
            bankManager.createTransaction(new Transaction(uuid,
                    account.getId(),
                    TransactionType.CREAR,
                    TransactionDbType.MYSQL,
                    account.getFechaCreacion()));
            bankManager.checkDb(TransactionDbType.POSTGRES);
            bankManager.createAccount(account);
            bankManager.createTransaction(new Transaction(uuid,
                    account.getId(),
                    TransactionType.CREAR,
                    TransactionDbType.POSTGRES,
                    account.getFechaCreacion()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Object withdrawFunds(WithdrawFundsDto withdrawFundsDto) {
        try {
            bankManager.checkDb(withdrawFundsDto.dbType());
            var rs = bankManager.withdrawFunds(withdrawFundsDto.clientId(), withdrawFundsDto.amount());
            if (rs) {
                bankManager.createTransaction(new Transaction(
                        UUID.randomUUID().toString(),
                        withdrawFundsDto.clientId(),
                        TransactionType.RETIRAR,
                        withdrawFundsDto.dbType(),
                        LocalDateTime.now()
                ));
                return bankManager.getAccountById(withdrawFundsDto.clientId());
            } else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DepositResponse depositFunds(DespositDto depositDto) {
        try {
            bankManager.checkDb(depositDto.dbType());
            bankManager.depositFunds(depositDto.clientId(), depositDto.amount());
            bankManager.createTransaction(new Transaction(UUID.randomUUID().toString(),
                    depositDto.clientId(),
                    TransactionType.INGRESAR,
                    depositDto.dbType(),
                    LocalDateTime.now()));
            return bankManager.getAccountById(depositDto.clientId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(String clientId) {
        try {
            bankManager.checkDb(TransactionDbType.MYSQL);
            bankManager.deleteTransaction(clientId);
            bankManager.deleteAccount(clientId);
            bankManager.checkDb(TransactionDbType.POSTGRES);
            bankManager.deleteTransaction(clientId);
            bankManager.deleteAccount(clientId);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Account> getAllPostgresAccounts() {
        try {
            bankManager.checkDb(TransactionDbType.POSTGRES);
            return bankManager.getAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public ArrayList<Account> getAllMysqlAccounts() {
        try {
            bankManager.checkDb(TransactionDbType.MYSQL);
            return bankManager.getAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try {
            bankManager.checkDb(TransactionDbType.MYSQL);
            return bankManager.getAllTransactions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Transaction> getAllPostgreslTransactions() {
        try {
            bankManager.checkDb(TransactionDbType.POSTGRES);
            return bankManager.getAllTransactions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String login(String id) {
        try {
            bankManager.checkDb(TransactionDbType.MYSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bankManager.login(id);
    }
}
