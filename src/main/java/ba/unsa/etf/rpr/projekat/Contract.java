package ba.unsa.etf.rpr.projekat;

import java.time.LocalDate;

public class Contract {
    private int id;
    private LocalDate startContract, endContract;
    private Service service;

    public Contract(int id, LocalDate startContract, LocalDate endContract, Service service) {
        this.id = id;
        this.startContract = startContract;
        this.endContract = endContract;
        this.service = service;
    }

    public Contract() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartContract() {
        return startContract;
    }

    public void setStartContract(LocalDate startContract) {
        this.startContract = startContract;
    }

    public LocalDate getEndContract() {
        return endContract;
    }

    public void setEndContract(LocalDate endContract) {
        this.endContract = endContract;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object) return true;

        if(!(object instanceof Contract)) return false;

        Contract c = (Contract) object;

        return this.getId() == c.getId();
    }
}
