package be.pxl.student.bean;
import java.time.LocalDate;
import java.util.Objects;

public class Payment {
    private int id;
    private LocalDate date;
    private float amount;
    private String currency;
    private String detail;
    private int accountId;
    private int counterAccountId;
    private int labelId;

    public Payment() {
        date = LocalDate.now();
    }

    public Payment(LocalDate date, float amount, String currency, String detail, int accountId, int counterAccountId, int labelId) {
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.detail = detail;
        this.accountId = accountId;
        this.counterAccountId = counterAccountId;
        this.labelId = labelId;
    }

    public Payment(int id, LocalDate date, float amount, String currency, String detail, int accountId, int counterAccountId, int labelId) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.currency = currency;
        this.detail = detail;
        this.accountId = accountId;
        this.counterAccountId = counterAccountId;
        this.labelId = labelId;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCounterAccountId() {
        return counterAccountId;
    }

    public void setCounterAccountId(int counterAccountId) {
        this.counterAccountId = counterAccountId;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Float.compare(payment.amount, amount) == 0 &&
                accountId == payment.accountId &&
                counterAccountId == payment.counterAccountId &&
                labelId == payment.labelId &&
                Objects.equals(date, payment.date) &&
                Objects.equals(currency, payment.currency) &&
                Objects.equals(detail, payment.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, amount, currency, detail, accountId, counterAccountId, labelId);
    }
}
