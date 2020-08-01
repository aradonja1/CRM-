package ba.unsa.etf.rpr.projekat.DAL;

import ba.unsa.etf.rpr.projekat.Contract;
import ba.unsa.etf.rpr.projekat.Customer;
import ba.unsa.etf.rpr.projekat.Package;
import ba.unsa.etf.rpr.projekat.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CustomerDAO {
    private DatabaseConnection db = DatabaseConnection.getInstance();
    private Connection conn;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PreparedStatement allCustomersStatement, getConnectionFromIdStatement, getServiceFromIdStatement, getPackageFromIdStatement,
            getAllContractsForCustomerStatement, addCustomerStatement, getConnectionFromServiceAndPackageIdStatement, getIdForNewCustomerStatement,
            addContractStatement, getIdForNewContractStatement, editCustomerStatement, getCurrentContractStatement, editCurrentContract,
            deleteCustomerStatement, deleteContractsForCustomerStatement, allPackagesStatement, allServicesStatement, getPackagesForServiceStatement;

    public CustomerDAO() {
        conn = db.getConn();
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM customer");
        } catch (SQLException e) {
            db.regenerateBase();
        }
        try {
            allCustomersStatement = conn.prepareStatement("SELECT * FROM customer");
            getConnectionFromIdStatement = conn.prepareStatement("SELECT * FROM connection WHERE id=?");
            getServiceFromIdStatement = conn.prepareStatement("SELECT * FROM service WHERE id=?");
            getPackageFromIdStatement = conn.prepareStatement("SELECT * FROM package WHERE id=?");
            getPackagesForServiceStatement = conn.prepareStatement("SELECT package_id FROM connection WHERE service_id=?");
            getAllContractsForCustomerStatement = conn.prepareStatement("SELECT * FROM contract WHERE customer_id=?");

            addCustomerStatement = conn.prepareStatement("INSERT INTO customer VALUES(?,?,?,?,?,?,?,?,?)");
            getConnectionFromServiceAndPackageIdStatement = conn.prepareStatement("SELECT id FROM connection WHERE service_id=? AND package_id=?");
            getIdForNewCustomerStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM customer");
            addContractStatement = conn.prepareStatement("INSERT INTO contract VALUES(?,?,?,?,?)");
            getIdForNewContractStatement = conn.prepareStatement("SELECT MAX(id)+1 FROM contract");

            editCustomerStatement = conn.prepareStatement("UPDATE customer SET first_name=?, last_name=?, email=?, adress=?, contact=?, begin_contract=?, end_contract=?, connection=? WHERE id=? ");
            getCurrentContractStatement = conn.prepareStatement("SELECT a.id, a.customer_id, a.begin_contract, a.end_contract, a.connection FROM contract a, customer b WHERE b.id=a.customer_id AND b.id=?");
            editCurrentContract = conn.prepareStatement("UPDATE contract SET customer_id=?, begin_contract=?, end_contract=?, connection=? WHERE id=?");

            deleteCustomerStatement = conn.prepareStatement("DELETE FROM customer WHERE id=?");
            deleteContractsForCustomerStatement = conn.prepareStatement("DELETE FROM contract WHERE customer_id=?");

            allPackagesStatement = conn.prepareStatement("SELECT * FROM package");
            allServicesStatement = conn.prepareStatement("SELECT * FROM service");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    //daje sve korisnike iz baze
    public ArrayList<Customer> customers() {
        ArrayList<Customer> result = new ArrayList<>();
        try {
            ResultSet rs = allCustomersStatement.executeQuery();
            while (rs.next()) {
                Customer c = getCustomerFromResultSet(rs);
                result.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Customer getCustomerFromResultSet(ResultSet rs) {
          try {
             getConnectionFromIdStatement.setInt(1,  rs.getInt(9));
             ResultSet rs2 = getConnectionFromIdStatement.executeQuery();
             Customer c = new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                     rs.getString(5), rs.getString(6), LocalDate.parse(rs.getString(7), formatter),
                     LocalDate.parse(rs.getString(8), formatter), getServiceFromConnectionResultSet(rs2), null);
            c.setContracts(getContractsFromCustomer(c));
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Service getServiceFromConnectionResultSet(ResultSet rs) {
        try {
            getServiceFromIdStatement.setInt(1, rs.getInt(2));
            getPackageFromIdStatement.setInt(1,  rs.getInt(3));
            ResultSet rs3 = getServiceFromIdStatement.executeQuery();
            ResultSet rs4 = getPackageFromIdStatement.executeQuery();
            return getServiceFromId(rs3.getInt(1), getPackageFromId(rs4.getInt(1)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Service getServiceFromId(int serviceId, Package aPackage) {
        try {
            getServiceFromIdStatement.setInt(1, serviceId);
            ResultSet rs = getServiceFromIdStatement.executeQuery();
            getPackagesForServiceStatement.setInt(1, serviceId);
            ResultSet rs2 = getPackagesForServiceStatement.executeQuery();
            ArrayList<Package> listPackages = new ArrayList<>();
            while (rs2.next()) {
                int packageId = rs2.getInt(1);
                getPackageFromIdStatement.setInt(1, packageId);
                ResultSet rs3 = getPackageFromIdStatement.executeQuery();
                listPackages.add(new Package(rs3.getInt(1), rs3.getString(2)));
            }
            return new Service(rs.getInt(1), rs.getString(2), listPackages);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //vraća paket iz baze koji ima id = packageId
    private Package getPackageFromId(int packageId) {
        try {
            getPackageFromIdStatement.setInt(1, packageId);
            ResultSet rs = getPackageFromIdStatement.executeQuery();
            return new Package(rs.getInt(1), rs.getString(2));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    private ArrayList<Contract> getContractsFromCustomer(Customer customer) {
        ArrayList<Contract> result = new ArrayList<>();
        try {
            getAllContractsForCustomerStatement.setInt(1, customer.getId());
            ResultSet rs = getAllContractsForCustomerStatement.executeQuery();
            while (rs.next()) {
                getConnectionFromIdStatement.setInt(1,  rs.getInt(5));
                ResultSet rs2 = getConnectionFromIdStatement.executeQuery();
                result.add(new Contract(rs.getInt(1), LocalDate.parse(rs.getString(3), formatter), LocalDate.parse(rs.getString(4), formatter), getServiceFromConnectionResultSet(rs2)));
                Contract c = new Contract(rs.getInt(1), LocalDate.parse(rs.getString(3), formatter), LocalDate.parse(rs.getString(4), formatter), getServiceFromConnectionResultSet(rs2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public DatabaseConnection getDb() {
        return db;
    }

    public ArrayList<Contract> getArchivedContracts(Customer customer) {
        ArrayList<Contract> all = getContractsFromCustomer(customer);
        ArrayList<Contract> all2 = customer.getContracts();
        ArrayList<Contract> result = new ArrayList<>();
        for (Contract c : all) {
            if (c.getEndContract().isBefore(LocalDate.now())) {
                result.add(c);
            }
        }
        return result;

     /*
     ArrayList<Contract> result = getContractsFromCustomer(customer);
     return result.stream().filter(c -> {
         return c.getEndContract().isBefore(LocalDate.now());
     }).collect(Collectors.toCollection(ArrayList::new));*/
    }

    //kada se dodaje novi ugovor dodaje se i u tabelu contract ne samo customer

    public void addCustomer(Customer customer) {
        try {
            //find id for new customer
            ResultSet rs = getIdForNewCustomerStatement.executeQuery();
            int id = rs.getInt(1);
            customer.setId(id);
            addCustomerStatement.setInt(1, customer.getId());
            addCustomerStatement.setString(2, customer.getFirstName());
            addCustomerStatement.setString(3, customer.getLastName());
            addCustomerStatement.setString(4, customer.getEmail());
            addCustomerStatement.setString(5, customer.getAdress());
            addCustomerStatement.setString(6, customer.getContact());
            addCustomerStatement.setString(7, customer.getBeginContract().format(formatter));
            addCustomerStatement.setString(8, customer.getEndContract().format(formatter));
            int connectionId = getConnectionId(customer);
            addCustomerStatement.setInt(9, connectionId);
            addCustomerStatement.execute();
            //after we have added a customer, let's add a contract
            addContract(customer, connectionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContract(Customer customer, int connectionId) {
        ResultSet rs2 = null;
        try {
            rs2 = getIdForNewContractStatement.executeQuery();
            int contractId = rs2.getInt(1);
            addContractStatement.setInt(1, contractId);
            addContractStatement.setInt(2, customer.getId());
            addContractStatement.setString(3, customer.getBeginContract().format(formatter));
            addContractStatement.setString(4, customer.getEndContract().format(formatter));
            addContractStatement.setInt(5, connectionId);
            addContractStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCustomer(Customer customer) {
        try {
            editCustomerStatement.setString(1, customer.getFirstName());
            editCustomerStatement.setString(2, customer.getLastName());
            editCustomerStatement.setString(3, customer.getEmail());
            editCustomerStatement.setString(4, customer.getAdress());
            editCustomerStatement.setString(5, customer.getContact());
            editCustomerStatement.setString(6, customer.getBeginContract().format(formatter));
            editCustomerStatement.setString(7, customer.getEndContract().format(formatter));
            int connectionId = getConnectionId(customer);
            editCustomerStatement.setInt(8, connectionId);
            editCustomerStatement.setInt(9, customer.getId());
            editCustomerStatement.execute();
            //razmisli sta uraditi sa ugovorom kad izmjenjujes korisnika...
            //zakljuci trenutni i kreiraj novi
            getCurrentContractStatement.setInt(1, customer.getId());
            ResultSet rs = getCurrentContractStatement.executeQuery();
            Contract c = getContractFromResultSet(rs);
            concludeCurrentContract(c);
            addContract(customer, connectionId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
     }

    private void concludeCurrentContract(Contract contract) {
        try {
            editCurrentContract.setString(3, LocalDate.now().format(formatter));
            editCurrentContract.setInt(5, contract.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editCurrentContract(Customer customer) {
        try {
            editCurrentContract.setInt(1, customer.getId());
            editCurrentContract.setString(2, customer.getBeginContract().format(formatter));
            editCurrentContract.setString(3, customer.getEndContract().format(formatter));
            getConnectionFromIdStatement.setInt(1, getConnectionId(customer));
            ResultSet rs = getConnectionFromIdStatement.executeQuery();
            editCurrentContract.setInt(4, rs.getInt(1));
            editCurrentContract.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Contract getContractFromResultSet(ResultSet rs) {
        try {
            int id = rs.getInt(1);
            int customerId = rs.getInt(2);
            LocalDate beginContract = LocalDate.parse(rs.getString(3), formatter);
            LocalDate endContract = LocalDate.parse(rs.getString(4), formatter);
            int connectionId = rs.getInt(5);
            getConnectionFromIdStatement.setInt(1, connectionId);
            ResultSet rs2 = getConnectionFromIdStatement.executeQuery();
            Service s = getServiceFromConnectionResultSet(rs2);
            return new Contract(id, beginContract, endContract, s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private int getConnectionId(Customer customer) {
         int packageId = customer.getService().getListPackages().get(0).getId();
         int serviceId = customer.getService().getId();
         try {
             getConnectionFromServiceAndPackageIdStatement.setInt(1, serviceId);
             getConnectionFromServiceAndPackageIdStatement.setInt(2, packageId);
             ResultSet rs = getConnectionFromServiceAndPackageIdStatement.executeQuery();
             return rs.getInt(1);
         } catch (SQLException e) {
             e.printStackTrace();
         }
         return 0;
     }

     public void deleteCustomer(Customer customer) {
         try {
             deleteCustomerStatement.setInt(1, customer.getId());
             deleteContractsForCustomerStatement.setInt(1, customer.getId());
             deleteContractsForCustomerStatement.execute();
             deleteCustomerStatement.execute();
         } catch (SQLException e) {
             e.printStackTrace();
         }
     }

    public ArrayList<Service> services() {
        ArrayList<Service> result = new ArrayList<>();
        try {
            ResultSet rs = allServicesStatement.executeQuery();
            while (rs.next()) {
                Service s = new Service(rs.getInt(1), rs.getString(2), null);
                s.setListPackages(getPackagesForService(s));
                result.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private ArrayList<Package> getPackagesForService(Service service) {
        ArrayList<Package> result = new ArrayList<>();
        try {
            getPackagesForServiceStatement.setInt(1, service.getId());
            ResultSet rs = getPackagesForServiceStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                getPackageFromIdStatement.setInt(1, id);
                ResultSet rs2 = getPackageFromIdStatement.executeQuery();
                result.add(new Package(rs2.getInt(1), rs2.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //malo logike ovdje!!!!
        return result;
    }

    public ArrayList<Package> packages(Service service) {
        ArrayList<Package> result = new ArrayList<>();
        try {
            getPackagesForServiceStatement.setInt(1, service.getId());
            ResultSet rs = getPackagesForServiceStatement.executeQuery();
            while (rs.next()) {
                int packageId = rs.getInt(1);
                getPackageFromIdStatement.setInt(1, packageId);
                ResultSet rs2 = getPackageFromIdStatement.executeQuery();
                result.add(new Package(rs2.getInt(1), rs2.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
