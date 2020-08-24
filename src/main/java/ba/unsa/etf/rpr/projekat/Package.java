package ba.unsa.etf.rpr.projekat;

public class Package {
    private int id;
    private String name;

    public Package(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Package() {
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

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object){
        if(this == object) return true;

        if(!(object instanceof Package)) return false;

        Package p = (Package) object;

        return this.getId() == p.getId();
    }
}
