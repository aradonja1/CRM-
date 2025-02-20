package ba.unsa.etf.rpr.projekat;

import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

public class Service {
    private int id;
    private String name;
    private ArrayList<Package> listPackages;

    public Service(int id, String name, ArrayList<Package> listPackages) {
        this.id = id;
        this.name = name;
        this.listPackages = listPackages;
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

    public ArrayList<Package> getListPackages() {
        return listPackages;
    }

    public void setListPackages(ArrayList<Package> listPackages) {
        this.listPackages = listPackages;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object){
        if(this == object) return true;

        if(!(object instanceof Service)) return false;

        Service s = (Service) object;

        return this.getId() == s.getId();
    }
}
