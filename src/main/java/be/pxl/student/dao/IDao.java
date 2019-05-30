package be.pxl.student.dao;

import java.sql.SQLException;

public interface IDao {
    void commit() throws SQLException;
}
