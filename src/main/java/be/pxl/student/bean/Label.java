package be.pxl.student.bean;

import java.util.Objects;

public class Label {
    private int id;
    private String name;
    private String description;

    public Label() {
    }

    public Label(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Label(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return Objects.equals(name, label.name) &&
                Objects.equals(description, label.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
