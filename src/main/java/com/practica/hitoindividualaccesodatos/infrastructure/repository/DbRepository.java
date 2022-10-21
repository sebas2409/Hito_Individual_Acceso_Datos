package com.practica.hitoindividualaccesodatos.infrastructure.repository;

import com.practica.hitoindividualaccesodatos.domain.Account;
import com.practica.hitoindividualaccesodatos.domain.Transaction;
import com.practica.hitoindividualaccesodatos.domain.TransactionDbType;
import com.practica.hitoindividualaccesodatos.domain.TransactionType;
import com.practica.hitoindividualaccesodatos.service.BankManager;
import com.practica.hitoindividualaccesodatos.service.dto.DepositResponse;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public void deleteAccount(String id) {
        try {
            var ps = connection.prepareStatement("DELETE FROM cuenta WHERE id=?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void depositFunds(String clientId, Double amount) {
        try {
            var ps = connection.prepareStatement("UPDATE cuenta SET balance=balance+? WHERE id=?");
            ps.setDouble(1, amount);
            ps.setString(2, clientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean withdrawFunds(String id, Double amount) {
        try {
            var ps = connection.prepareStatement("SELECT * FROM cuenta where id=?");

            ps.setString(1, id);
            var rs = ps.executeQuery();
            DepositResponse cuenta = null;
            while (rs.next()) {
                cuenta = new DepositResponse(id, rs.getString(2), rs.getDouble(3));
            }
            assert cuenta != null;
            if (cuenta.balance() < amount) {
                return false;
            } else {
                var ps2 = connection.prepareStatement("UPDATE cuenta set balance=balance-? where id=?");
                ps2.setDouble(1, amount);
                ps2.setString(2, id);
                ps2.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

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
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DepositResponse getAccountById(String clientId) {
        try {
            var ps = connection.prepareStatement("SELECT * FROM cuenta WHERE id=?");
            ps.setString(1, clientId);
            var rs = ps.executeQuery();
            DepositResponse reponse = null;
            while (rs.next()) {
                reponse = new DepositResponse(rs.getString(1), rs.getString(2), rs.getDouble(3));
            }
            return reponse;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void deleteTransaction(String clientId) {
        try {
            var ps = connection.prepareStatement("DELETE FROM transaccion where clienteid=?"); //este parametro cambia dependiendo del nombre de la tabla.
            ps.setString(1, clientId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public ArrayList<Account> getAllAccounts() {
        try {
            var ps = connection.prepareStatement("SELECT * from cuenta");
            var rs = ps.executeQuery();
            var listaCuentas = new ArrayList<Account>();
            while (rs.next()) {
                listaCuentas.add(new Account(rs.getString(1),
                        rs.getString(2),
                        rs.getDouble(3),
                        LocalDateTime.parse(rs.getString(4))));
            }
            return listaCuentas;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Transaction> getAllTransactions() {
        try {
            var ps = connection.prepareStatement("SELECT * FROM transaccion");
            var rs = ps.executeQuery();
            var listaTransacciones = new ArrayList<Transaction>();
            while (rs.next()) {
                listaTransacciones.add(new Transaction(
                        rs.getString(1),
                        rs.getString(2),
                        TransactionType.valueOf(rs.getString(3)),
                        TransactionDbType.valueOf(rs.getString(4)),
                        LocalDateTime.parse(rs.getString(5))
                ));
            }
            return listaTransacciones;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
