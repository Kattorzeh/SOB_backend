package model.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;

/**
 *
 * @author mario robres
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Customer.findAllCustomer",
                query="SELECT c FROM Customer c")
})
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String email;
    @JsonbTransient 
    private String password;
    //favGames
    //#rentals
    @OneToMany(mappedBy="rentedCustomer")
    @JsonbTransient
    private Collection<Rental> reservations;
    private Collection<Long> reservationsId;
    
    public Customer(){
        reservations = new ArrayList<Rental>();
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Rental> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Rental> reservations) {
        this.reservations = reservations;
    }
    
    public Collection<Long> getReservationsId() {
        return reservationsId;
    }

    public void setReservationsId(Collection<Long> reservationsId) {
        this.reservationsId = reservationsId;
    }
    
    public void addRental(Rental rental){
        if(!getReservations().contains(rental)){
            getReservations().add(rental);
        }
    }
    @Override
    public String toString() {
        return "Customer{" + "id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", reservations=" + reservations + '}';
    }
}
