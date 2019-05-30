package be.pxl.student.dao;

import be.pxl.student.bean.Account;
import be.pxl.student.exceptions.AccountException;
import be.pxl.student.utils.ConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AccountDaoTests {
    private static final String url = "jdbc:mysql://192.168.33.22/StudentDBTest";
    private static final String user = "student";
    private static final String password = "root";
    private Connection connection;
    private AccountDao dao;

    @Before
    public void beforeTests() throws SQLException {
        this.connection = ConnectionFactory.getJdbcMysqlConnection(url, user, password);
        connection.setAutoCommit(false);
        this.dao = new AccountDao(connection);
    }

    @After
    public void afterTests() throws SQLException {
        this.connection.rollback();
        this.connection.close();

        this.dao = null;
    }

    @Test
    public void addAccount_shouldReturnAnAccountWithAnI_account() throws AccountException {
        //Arrange
        Account testAccount = new Account("TestAccount", "TestAccount", "TestAccount");

        //Act
        Account actual = dao.addAccount(testAccount);

        //Assert
        Assert.assertNotEquals(actual.getId(), 0);
    }

    @Test
    public void getAll_shouldReturnListOfAccounts() throws AccountException {
        //Arrange
        Account expectedAccount =
                dao.addAccount(new Account("TestAccount", "TestAccount", "TestAccount"));

        //Act
        Assert.assertNotNull(expectedAccount);
        List<Account> foundAccounts = dao.getAllAccounts();

        //Assert
        Assert.assertNotNull(foundAccounts);
        Assert.assertEquals(foundAccounts.size(), 1);
    }

    @Test
    public void getById_shouldReturnAccountWithGivenId_Account() throws AccountException {
        //Arrange
        Account expectedAccount =
                dao.addAccount(new Account("TestAccount", "TestAccount", "TestAccount"));

        //Act
        Assert.assertNotNull(expectedAccount);
        Account actualAccountFound = dao.getById(expectedAccount.getId());

        //Assert
        Assert.assertEquals(expectedAccount, actualAccountFound);
        AccountException exception = null;
        try {
            dao.getById(0);
        } catch (AccountException ex) {
            exception = ex;
        }
        Assert.assertNotNull(exception);
        Assert.assertEquals(exception.getMessage(), "No account found with id of 0");
    }

    @Test
    public void updateAccountById_shouldReturnNumberOfUpdatedRows() throws AccountException {
        //Arrange
        Account expectedAccount =
                dao.addAccount(new Account("TestAccount", "TestAccount", "TestAccount"));
        Assert.assertNotNull(expectedAccount);
        expectedAccount.setName("New Name");

        //Act
        int rowsUpdated = dao.updateAccount(expectedAccount);
        Account updated = dao.getById(expectedAccount.getId());
        //Assert
        Assert.assertEquals(rowsUpdated, 1);
        Assert.assertEquals(expectedAccount.getName(), updated.getName());
    }
}
