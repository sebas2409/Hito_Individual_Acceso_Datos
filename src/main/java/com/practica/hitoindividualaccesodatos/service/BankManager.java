package com.practica.hitoindividualaccesodatos.service;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;

import java.sql.SQLException;

public interface BankManager {
    void checkDb(TransactionDbType dbType) throws SQLException;

    void createAccount(Account account);

    String deleteAccount(String id);

    Account depositFunds(String id, Double amount);

    Account withdrawFunds(String id, Double amount);
    void createTransaction(Transaction transaction);
}
