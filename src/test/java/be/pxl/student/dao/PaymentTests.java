package be.pxl.student.dao;

import be.pxl.student.bean.Account;
import be.pxl.student.bean.Label;
import be.pxl.student.bean.Payment;
import be.pxl.student.utils.ConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PaymentTest {
    private static final String url = "jdbc:mysql://192.168.33.22/StudentDBTest";
    private static final String user = "student";
    private static final String password = "root";
    private Connection connection;
    private PaymentDao dao;

    @Before
    public void beforeTests() throws SQLException {
        this.connection = ConnectionFactory.getJdbcMysqlConnection(url, user, password);
        connection.setAutoCommit(false);
        this.dao = new PaymentDao(connection);
    }

    @After
    public void afterTests() throws SQLException {
        this.connection.rollback();
        this.connection.close();

        this.dao = null;
    }

    @Test
    public void addPayment_shouldAddPayment_Payment() throws PaymentException {
        //Arrange
        Payment payment = getTestPayment(1);

        //Act
        Payment result = dao.addPayment(payment);

        //Assert
        Assert.assertNotNull(result);
        Assert.assertNotEquals(result.getId(), 0);
    }

    @Test
    public void getAllPayments_shouldReturnAllPayments() throws PaymentException {
        //Arrange
        dao.addPayment(getTestPayment(1));

        //Act
        List<Payment> result = dao.getAllPayments();

        //Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(result.size(), 1);
    }

    @Test
    public void getById_shouldReturnPaymentWithGivenId() throws PaymentException {
        //Arrange
        Payment expected = getTestPayment(1);
        Assert.assertNotNull(expected);

        //Act
        Payment actual = dao.getById(expected.getId());

        //Assert
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getPaymentsByAccountId_shouldReturnPaymentsWithGivenAccountId(){
        Account account = getTestingAccount();
        seedMultiplePayments(account.getId());
    }

    private Label getTestingLabel() {
        return new Label(1, "test", "test");
    }

    private Account getTestingAccount() {
        return new Account(1, "test", "test", "test");
    }

    private Payment getTestPayment(int accountId){
        return new Payment(LocalDate.now(),
                0F,
                "test",
                "test",
                accountId,
                0,
                0);
    }
}
