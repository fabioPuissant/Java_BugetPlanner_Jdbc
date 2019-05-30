package be.pxl.student.dao;

import be.pxl.student.bean.Account;
import be.pxl.student.exceptions.AccountException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao implements IDao{

    private Connection connection;

    public AccountDao(Connection connection) {

        this.connection = connection;
    }

    public Account addAccount(Account account) throws AccountException {
        String query = "INSERT INTO Account(number, iban, name) VALUES(?, ?, ?)";
        try (PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getNumber());
            statement.setString(2, account.getIban());
            statement.setString(3, account.getName());

            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            if (set.next()) {
                account = new Account(set.getInt(1),
                        account.getNumber(),
                        account.getIban(),
                        account.getName());
                return account;
            }

            throw new AccountException("Account was not added");

        } catch (SQLException exception) {
            throw new AccountException(exception.getMessage());
        }
    }

    public List<Account> getAllAccounts() throws AccountException {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM Account";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet set = statement.executeQuery()) {

            while (set.next()) {
                Account account = makeAccountOutOfResultSetRow(set);
                accounts.add(account);
            }

            return accounts;
        } catch (SQLException e) {
            throw new AccountException(e.getMessage());
        }
    }

    public Account getById(int id) throws AccountException {
        String query = "SELECT * FROM Account WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet set = statement.executeQuery();
            if (set.first()){
                return makeAccountOutOfResultSetRow(set);
            }

            throw new AccountException("No account found with id of " + id);
        } catch (SQLException e) {
            throw new AccountException(e.getMessage());
        }
    }

    public int updateAccount(Account account) throws AccountException {
        String query = "UPDATE Account SET Name=?, Number=?, Iban=? WHERE Id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, account.getName());
            statement.setString(2, account.getNumber());
            statement.setString(3, account.getIban());
            statement.setInt(4, account.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new AccountException(e);
        }
    }

    private Account makeAccountOutOfResultSetRow(ResultSet set) throws SQLException {
        String name = set.getString("Name");
        String iban = set.getString("Iban");
        String number = set.getString("Number");
        int id = set.getInt("Id");

        return new Account(id, number, iban, name);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }
}
