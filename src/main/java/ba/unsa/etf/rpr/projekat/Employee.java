package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleObjectProperty;

import java.io.Serializable;

public class Employee extends Person {
    private int id;
    private String  username, password;

    public Employee(int id, String firstName, String lastName, String username, String password) {
        super(firstName, lastName);
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return super.getFirstName();
    }

    public void setFirstName(String firstName) {
        super.setFirstName(firstName);
    }

    public String getLastName() {
        return super.getLastName();
    }

    public void setLastName(String lastName) {
        super.setLastName(lastName);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
