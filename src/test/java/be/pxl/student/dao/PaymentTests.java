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
import java.util.Random;

public class PaymentTests {
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
        Payment expected = dao.addPayment(getTestPayment(1));
        Assert.assertNotNull(expected);

        //Act
        Payment actual = dao.getById(expected.getId());

        //Assert
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void getPaymentsByAccountId_shouldReturnPaymentsWithGivenAccountId() throws PaymentException {
        //Arrange
        Account account = getTestingAccount();
        int size = seedMultiplePayments(account.getId());

        //Act
        List<Payment> paymentsOfAccount = dao.getPaymentsByAccountId(account.getId());

        //Assert
        Assert.assertNotNull(paymentsOfAccount);
        Assert.assertEquals(paymentsOfAccount.size(), size);
    }

    @Test
    public void getPaymentsOfLabel_shouldReturnPaymentsWithGivenLabelId() throws PaymentException {
        //Arrange
        Label label = getTestingLabel();
        int size = seedMultiplePayments(label.getId());

        //Act
        List<Payment> paymentsOfLabel = dao.getPaymentsOfLabel(label.getId());

        //Assert
        Assert.assertNotNull(paymentsOfLabel);
        Assert.assertEquals(paymentsOfLabel.size(), size);
    }

    @Test
    public void addLabelToPayment_shouldAddLabelToPayment() throws PaymentException {
        //Arrange
        Payment payment = dao.addPayment(getTestPayment(1));
        Label label = getTestingLabel();
        int expectedRowsUpdated = 1;

        Assert.assertNotNull(payment);
        Assert.assertNotEquals(payment.getId(), 0);

        //Act
        int actualRowsUpdated = dao.addLabelToPayment(payment, label);
        Payment result = dao.getById(payment.getId());

        //Assert
        Assert.assertEquals(expectedRowsUpdated, actualRowsUpdated);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getLabelId(), label.getId());
    }

    @Test
    public void addLabelToAllPaymentOfAccount() throws PaymentException {
        //Arrange
        Account account = getTestingAccount();
        int expectedUpdatedRows = seedMultiplePayments(account.getId());
        Label label = getTestingLabel();

        //Act
        int actualUpdatedRows = dao.addLabelToAllPaymentOfAccount(account, label);
        List<Payment> paymentsOfAccount = dao.getPaymentsByAccountId(account.getId());

        //Assert
        Assert.assertEquals(expectedUpdatedRows, actualUpdatedRows);
        Assert.assertNotNull(paymentsOfAccount);
        paymentsOfAccount.forEach(payment -> {
            Assert.assertEquals(payment.getLabelId(), label.getId());
        });
    }

    private int seedMultiplePayments(int testingId) throws PaymentException {
        dao.addPayment(getTestPayment(testingId));
        dao.addPayment(getTestPayment(testingId));
        dao.addPayment(getTestPayment(testingId));

        return 3;
    }

    private Label getTestingLabel() {
        return new Label(new Random().nextInt(Integer.MAX_VALUE), "test", "test");
    }

    private Account getTestingAccount() {
        return new Account(1, "test", "test", "test");
    }

    private Payment getTestPayment(int testingId) {
        return new Payment(LocalDate.now(),
                0F,
                "test",
                "test",
                testingId,
                0,
                testingId);
    }
}
