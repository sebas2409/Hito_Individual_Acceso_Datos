package com.practica.hitoindividualaccesodatos.service;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.service.dto.AccountResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;

import java.util.ArrayList;

public interface BankManager {

    void createAccount(Account account);

    void deleteAccount(String id);

    void depositFunds(String id, Double amount);

    Boolean withdrawFunds(String id, Double amount);

    void createTransaction(Transaction transaction);

    AccountResponse getAccountById2(String clientId);
    DepositResponse getAccountById(String id);

    void deleteTransaction(String clientId);
    ArrayList<Account> getAllAccounts();
    ArrayList<Transaction> getAllTransactions();
    ArrayList<Transaction> getAllTransactionsFromId(String id);
    DepositResponse login(String id);
}
