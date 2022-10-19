package com.practica.hitoindividualaccesodatos.infrastructure.repository;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.service.BankManager;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Repository
public class DbRepository implements BankManager {

    private Connection connection;

    public void checkDb(TransactionDbType dbType) throws SQLException {
        if (dbType.equals(TransactionDbType.MYSQL)) {
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/banco", "root", "root");
        } else {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/banco", "root", "root");
        }
    }

    @Override
    public void createAccount(Account account) {
        try {
            var ps = connection.prepareStatement("INSERT INTO cuenta values(?,?,?,?)");
            ps.setString(1, account.getId());
            ps.setString(2, account.getNombre());
            ps.setDouble(3, account.getBalance());
            ps.setString(4, String.valueOf(account.getFechaCreacion()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String deleteAccount(String id) {
        return null;
    }

    @Override
    public Account depositFunds(String id, Double amount) {
        return null;
    }

    @Override
    public Account withdrawFunds(String id, Double amount) {
        return null;
    }

    @Override
    public void createTransaction(Transaction transaction) {
        try {
            var ps = connection.prepareStatement("INSERT INTO transaccion values (?,?,?,?,?)");
            ps.setString(1, transaction.getId());
            ps.setString(2, transaction.getAccountId());
            ps.setString(3, transaction.getTipoTransaccion().toString());
            ps.setString(4, transaction.getTipoDb().toString());
            ps.setString(5, LocalDateTime.now().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
