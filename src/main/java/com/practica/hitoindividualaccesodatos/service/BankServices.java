package com.practica.hitoindividualaccesodatos.service;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import com.practica.hitoindividualaccesodatos.service.dto.DespositDto;
import com.practica.hitoindividualaccesodatos.service.dto.WithdrawFundsDto;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
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
            if (rs){
                bankManager.createTransaction(new Transaction(
                        UUID.randomUUID().toString(),
                        withdrawFundsDto.clientId(),
                        TransactionType.RETIRAR,
                        withdrawFundsDto.dbType(),
                        LocalDateTime.now()
                ));
                return bankManager.getAccountById(withdrawFundsDto.clientId());
            }else{
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
}
