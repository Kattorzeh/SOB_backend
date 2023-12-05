package model.entities;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

/**
 *
 * @author mario robres
 */
@Entity
@NamedQueries({
        @NamedQuery(
                name = "Rental.findById",
                query = "SELECT r FROM Rental r WHERE r.id = :id"
        ),
    @NamedQuery(
                name = "Rental.findAll",
                query = "SELECT r FROM Rental r"
        ),
    @NamedQuery(name="Rental.findByPrimaryKeys",
                query="SELECT r FROM Rental r WHERE r.id IN :ids")
})
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss", locale = "en_US")
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @JsonbDateFormat(value = "yyyy-MM-dd HH:mm:ss", locale = "en_US")
    private Date returnDate;
    private double price;
    private int rentedDays;
    @JsonbTransient
    @ManyToMany
    private Collection<Game> rentedGames;
    @Transient
    private Collection<Long> rentedGamesId;
    @ManyToOne
    private Customer rentedCustomer;
    @Transient
    private Long rentedCustomerId;
    public Rental(){}
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getRentedDays() {
        return rentedDays;
    }

    public void setRentedDays(int rentedDays) {
        this.rentedDays = rentedDays;
    }
    
    public Collection<Game> getRentedGames() {
        return rentedGames;
    }

    public void setRentedGames(Collection<Game> rentedGames) {
        this.rentedGames = rentedGames;
    }
    
    public Collection<Long> getRentedGamesId() {
        return rentedGamesId;
    }
    
    public void setRentedGamesId(Collection<Long> rentedGamesId) {
        this.rentedGamesId = rentedGamesId;
    }

    public Customer getRentedCustomer() {
        return rentedCustomer;
    }

    public void setRentedCustomer(Customer rentedCustomer) {
        this.rentedCustomer = rentedCustomer;
    }
    public Long getRentedCustomerId() {
        return rentedCustomerId;
    }

    public void setRentedCustomerId(Long rentedCustomerId) {
        this.rentedCustomerId = rentedCustomerId;
    }
    
    public void addRentalGame(Game game){
        if(!getRentedGames().contains(game)){
            getRentedGames().add(game);
        }
        if(!game.getRentalList().contains(this)){
            game.getRentalList().add(this);
        }
    }
    @Override
    public String toString() {
        return "Rental{" + "id=" + id + ", startDate=" + startDate + ", price=" + price + ", rentedGames=" + rentedGames + ", rentedCustomer=" + rentedCustomer + '}';
    }
}
