package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Employee extends Person {
    private String username, password;

    public Employee(String name, String surname, String umcn, String contact, String email, LocalDate dateOfBirth, String username, String password) {
        super(name, surname, umcn, contact, email, dateOfBirth);
        this.username = username;
        this.password = password;
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
}
