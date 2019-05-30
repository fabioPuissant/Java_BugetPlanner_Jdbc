package be.pxl.student.dao;

import be.pxl.student.bean.Account;
import be.pxl.student.bean.Label;
import be.pxl.student.bean.Payment;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentDao implements IDao {
    private Connection connection;

    public PaymentDao(Connection connection) {
        this.connection = connection;
    }

    public Payment addPayment(Payment payment) throws PaymentException {
        if (payment == null) {
            throw new PaymentException("Given payment in addPayment was null!");
        }

        String query = "INSERT INTO `Payment`" +
                "(`Date`, `Amount`, `Currency`, `Detail`, `AccountId`, `CounterAccountId`, `LabelId`)" +
                "VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, payment.getDate());
            statement.setFloat(2, payment.getAmount());
            statement.setString(3, payment.getCurrency());
            statement.setString(4, payment.getDetail());
            statement.setInt(5, payment.getAccountId());
            statement.setInt(6, payment.getCounterAccountId());
            statement.setInt(7, payment.getLabelId());
            statement.executeUpdate();

            try (ResultSet set = statement.getGeneratedKeys()) {
                if (set.first()) {
                    payment = new Payment(set.getInt(1),
                            payment.getDate(),
                            payment.getAmount(),
                            payment.getCurrency(),
                            payment.getDetail(),
                            payment.getAccountId(),
                            payment.getCounterAccountId(),
                            payment.getLabelId());
                }

                return payment;
            }
        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    public Payment getById(int id) throws PaymentException {
        if (id < 1) {
            throw new PaymentException("Id must be greater than 0!");
        }

        String query = "SELECT * FROM `Payment` WHERE `Id`=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                if (set.first()) {
                    return createPaymentFromResultSetRow(set);
                }

                throw new PaymentException("No Payment found with id: " + id);
            }
        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    public List<Payment> getAllPayments() throws PaymentException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Payment`");
             ResultSet set = statement.executeQuery()) {
            List<Payment> payments = new ArrayList<>();
            while (set.next()) {
                Payment payment = createPaymentFromResultSetRow(set);
                payments.add(payment);
            }

            return payments;
        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    public List<Payment> getPaymentsByAccountId(int accountId) throws PaymentException {
        String query = "SELECT * FROM `Payment` WHERE `AccountId`=?";
        return getPayments(accountId, query);
    }

    public List<Payment> getPaymentsOfLabel(int labelId) throws PaymentException {
        String query = "SELECT * FROM `Payment` WHERE `LabelId`=?";
        return getPayments(labelId, query);
    }

    public int addLabelToPayment(Payment payment, Label label) throws PaymentException {
        if (payment == null || label == null) {
            throw new PaymentException("Params where null in addLabelToPayment!");
        }

        try (PreparedStatement statement =
                     connection.prepareStatement("UPDATE `Payment` SET `LabelId`=? WHERE `Id`=?")) {
            statement.setInt(1, label.getId());
            statement.setInt(2, payment.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    public int addLabelToAllPaymentOfAccount(Account account, Label label) throws PaymentException {
        if (account == null || label == null) {
            throw new PaymentException("Params where null in addLabelToAllPaymentOfAccount!");
        }

        String query = "UPDATE `Payment` SET `LabelId`=? WHERE `AccountId`=?";
        try (PreparedStatement statement =
                     connection.prepareStatement(query)) {
            statement.setInt(1, label.getId());
            statement.setInt(2, account.getId());

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    private List<Payment> getPayments(int id, String query) throws PaymentException {
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet set = statement.executeQuery()) {
                List<Payment> paymentsOfAccount = new ArrayList<>();
                while (set.next()) {
                    paymentsOfAccount.add(createPaymentFromResultSetRow(set));
                }

                return paymentsOfAccount;
            }
        } catch (SQLException e) {
            throw new PaymentException(e);
        }
    }

    private Payment createPaymentFromResultSetRow(ResultSet set) throws SQLException {
        int id = set.getInt("Id");
        LocalDate date = set.getDate("Date").toLocalDate();
        float amount = set.getFloat("Amount");
        String currency = set.getString("Currency");
        String detail = set.getString("Detail");
        int accountId = set.getInt("AccountId");
        int counterAccountId = set.getInt("CounterAccountId");
        int labeId = set.getInt("LabelId");

        return new Payment(id, date, amount, currency, detail, accountId, counterAccountId, labeId);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }
}
