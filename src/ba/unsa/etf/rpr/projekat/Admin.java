package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Admin extends Person {
    private String password, username;

    public Admin(String name, String surname, String umcn, String contact, String email, LocalDate dateOfBirth, String password, String username) {
        super(name, surname, umcn, contact, email, dateOfBirth);
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
