package be.pxl.student.bean;

import java.util.Objects;

public class Account {
    private int id;
    private String number;
    private String iban;
    private String name;

    public Account() {
    }

    public Account(String number, String iban, String name) {
        this.id = id;
        this.number = number;
        this.iban = iban;
        this.name = name;
    }

    public Account(int id, String number, String iban, String name) {
        this.id = id;
        this.number = number;
        this.iban = iban;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", iban='" + iban + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(number, account.number) &&
                Objects.equals(iban, account.iban) &&
                Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, iban, name);
    }
}
