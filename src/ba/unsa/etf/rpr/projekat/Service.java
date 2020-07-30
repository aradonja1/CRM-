package ba.unsa.etf.rpr.projekat;

import java.util.ArrayList;

public class Service {
    private int id;
    private String name;
    private ArrayList<Package> packages = new ArrayList<>();

    public Service(int id, String name, ArrayList<Package> packages) {
        this.id = id;
        this.name = name;
        this.packages = packages;
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

    public ArrayList<Package> getPackages() {
        return packages;
    }

    public void setPackages(ArrayList<Package> packages) {
        this.packages = packages;
    }
}
