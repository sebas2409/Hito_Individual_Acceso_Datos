package com.practica.hitoindividualaccesodatos.service;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;

import java.sql.SQLException;

public interface BankManager {
    void checkDb(TransactionDbType dbType) throws SQLException;

    void createAccount(Account account);

    void deleteAccount(String id);

    void depositFunds(String id, Double amount);

    Boolean withdrawFunds(String id, Double amount);

    void createTransaction(Transaction transaction);

    DepositResponse getAccountById(String clientId);

    void deleteTransaction(String clientId);
}
