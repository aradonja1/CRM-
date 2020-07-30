package ba.unsa.etf.rpr.projekat;

import java.util.ArrayList;

public class Service {
    private int id;
    private String name;
    private Package aPackage;

    public Service(int id, String name, Package aPackage) {
        this.id = id;
        this.name = name;
        this.aPackage = aPackage;
    }

    public Service() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Package getaPackage() {
        return aPackage;
    }

    public void setaPackage(Package aPackage) {
        this.aPackage = aPackage;
    }
}
