package be.pxl.student;

import be.pxl.student.bean.Account;
import be.pxl.student.bean.Label;
import be.pxl.student.dao.AccountDao;
import be.pxl.student.exceptions.AccountException;
import be.pxl.student.dao.LabelDao;
import be.pxl.student.utils.ConnectionFactory;
import be.pxl.student.utils.ConnectionStringFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BudgetPlannerFeeder {
    private static String url = "jdbc:mysql://192.168.33.22";
    private static String database = "StudentDB";
    private static String user = "student";
    private static String password = "root";
    private final Connection connection;

    public BudgetPlannerFeeder() throws SQLException {
        String connectionString = ConnectionStringFactory.getJdbcMysqlConnectionString(url, database);
        this.connection = ConnectionFactory.getJdbcMysqlConnection(connectionString, user, password);
    }

    public void feedRandomData() throws AccountException {
        feedThreeAccounts();
        feedThreeLabels();
        feedThreePayments();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void close() throws SQLException {
        connection.close();
    }

    private void feedThreePayments() {

    }

    private void feedThreeLabels() {
        List<Label> labels = new ArrayList<>();
        labels.add(new Label("Label 1 ", "First label"));
        labels.add(new Label("Label 2 ", "second label"));
        labels.add(new Label("Label 3 ", "Third label"));
        labels.add(new Label("Label 4 ", "Fourth label"));

        LabelDao dao = new LabelDao(connection);
        for (Label label : labels) {

        }

    }

    private void feedThreeAccounts() throws AccountException {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account("1111", "BE01", "John"));
        accounts.add(new Account("2222", "BE02", "Jane"));
        accounts.add(new Account("3333", "BE03", "Foo"));
        accounts.add(new Account("4444", "BE04", "Bar"));

        AccountDao dao = new AccountDao(connection);
        for (Account account : accounts) {
            Account addedAccount = dao.addAccount(account);
            System.out.println("Account with id: " + addedAccount.getId() + " added to DB");
        }
    }


}
