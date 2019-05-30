package be.pxl.student.dao;

import be.pxl.student.bean.Label;
import be.pxl.student.exceptions.LabelException;
import be.pxl.student.utils.ConnectionFactory;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class LabelDaoTest {
    private static final String url = "jdbc:mysql://192.168.33.22/StudentDBTest";
    private static final String user = "student";
    private static final String password = "root";
    private Connection connection;
    private LabelDao dao;

    @Before
    public void beforeTests() throws SQLException {
        this.connection = ConnectionFactory.getJdbcMysqlConnection(url, user, password);
        connection.setAutoCommit(false);
        this.dao = new LabelDao(connection);
    }

    @After
    public void afterTests() throws SQLException {
        this.connection.rollback();
        this.connection.close();

        this.dao = null;
    }

    @Test
    public void addLabel_shouldAddALabel_Label() throws LabelException {
        //Arrange
        Label seededLabel = dao.addLabel(new Label("TestLabel", "A Label for test purposes"));

        Assert.assertNotNull(seededLabel);
        Assert.assertNotEquals(seededLabel.getId(), 0);
    }

    @Test
    public void getAllLabels_shouldReturnListOfLabels() throws LabelException {
        //Arrange
        Label seededLabel = dao.addLabel(new Label("TestLabel", "A Label for test purposes"));

        //Act
        List<Label> foundLabels = dao.getAllLabels();

        //Assert
        Assert.assertNotNull(foundLabels);
        Assert.assertEquals(foundLabels.size(), 1);
    }

    @Test
    public void getById_shouldReturnOneLabelWithGivenId_Label() throws LabelException {
        //Arrange
        Label seededLabel = dao.addLabel(new Label("TestLabel", "A Label for test purposes"));
        Assert.assertNotEquals(seededLabel.getId(), 0);

        //Act
        Label actual = dao.getById(seededLabel.getId());

        //Assert
        Assert.assertEquals(seededLabel, actual);
    }

    @Test
    public void updateLabel_shouldUpdateAllFieldsOfALabel_int() throws LabelException {
        //Arrange
        Label seededLabel = dao.addLabel(new Label("TestLabel", "A Label for test purposes"));
        Assert.assertNotNull(seededLabel);

        //Act
        seededLabel.setName("new name");
        seededLabel.setDescription("New description");
        int rowsUpdated = dao.updateLabel(seededLabel);
        Assert.assertNotEquals(rowsUpdated, 0);
        Label actualLabel = dao.getById(seededLabel.getId());

        //Assert
        Assert.assertNotNull(actualLabel);
        Assert.assertEquals(seededLabel, actualLabel);
    }
}
