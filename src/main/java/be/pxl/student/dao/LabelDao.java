package be.pxl.student.dao;

import be.pxl.student.bean.Label;
import be.pxl.student.exceptions.LabelException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LabelDao implements IDao{

    private Connection connection;

    public LabelDao(Connection connection) {
        this.connection = connection;
    }

    public Label addLabel(Label label) throws LabelException {
        String query = "INSERT INTO `Label`(`Name`,`Description`) VALUES(?,?)";
        try (PreparedStatement statement =
                     connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, label.getName());
            statement.setString(2, label.getDescription());
            statement.executeUpdate();

            try (ResultSet set = statement.getGeneratedKeys()) {
                if (set.first()) {
                    label = new Label(set.getInt(1), label.getName(), label.getDescription());
                    return label;
                }
            }

            throw new LabelException("No Label added.");
        } catch (SQLException e) {
            throw new LabelException(e);
        }
    }

    public List<Label> getAllLabels() throws LabelException {
        String query = "SELECT * FROM Label";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet set = statement.executeQuery()) {

            List<Label> labels = new ArrayList<>();
            while (set.next()) {
                Label label = createLabelFromResultSetRow(set);
                labels.add(label);
            }

            return labels;
        } catch (SQLException e) {
            throw new LabelException(e);
        }
    }

    public Label getById(int id) throws LabelException {
        String query = "SELECT * FROM `Label` WHERE `Id`=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet set = statement.executeQuery()) {
                if (set.first()) {
                    return createLabelFromResultSetRow(set);
                }

                throw new LabelException("No Label found with id: " + id);
            }
        } catch (SQLException e) {
            throw new LabelException(e);
        }
    }

    public int updateLabel(Label label) throws LabelException {
        if(label == null){
            throw new LabelException("Given Label was null!");
        }

        String query = "UPDATE `Label` SET `Name`=?, `Description`=?";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, label.getName());
            statement.setString(2, label.getDescription());
            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new LabelException(e);
        }
    }

    private Label createLabelFromResultSetRow(ResultSet set) throws SQLException {
        int id = set.getInt("Id");
        String name = set.getString("Name");
        String description = set.getString("Description");
        return new Label(id, name, description);
    }

    @Override
    public void commit() throws SQLException {
        connection.commit();
    }
}
