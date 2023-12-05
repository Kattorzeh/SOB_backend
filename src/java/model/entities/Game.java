package model.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Embedded;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Transient;

/**
 *
 * @author mario robres
 */
@Entity
@NamedQueries({
    @NamedQuery(name="Game.findAll",
                query="SELECT e FROM Game e"),
    @NamedQuery(name="Game.findByPrimaryKey",
                query="SELECT e FROM Game e WHERE e.id = :id"),
    @NamedQuery(name="Game.findByPrimaryKeys",
            query="SELECT e FROM Game e WHERE e.id IN :ids AND e.gameState <> 'BOOKED'"),
    @NamedQuery(name="Game.findByName",
                query="SELECT e FROM Game e WHERE e.name = :name"),
    @NamedQuery(name="Game.findByType",
                query="SELECT g FROM Game g LEFT JOIN g.gameTypes t WHERE t.id=:id_type"),
    @NamedQuery(name="Game.findByConsole",
                query="SELECT e FROM Game e WHERE e.console.id = :console_id"),
    @NamedQuery(name="Game.findByTypeAndConsole",
                query="SELECT g FROM Game g LEFT JOIN g.gameTypes t LEFT JOIN g.console c WHERE t.id = :id_type AND c.id = :id_console ORDER BY g.name"),
    @NamedQuery(name = "Game.findByNombreAndConsole", 
                query = "SELECT g FROM Game g WHERE g.name = :nombre AND g.console = :console")

})
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private double price;
    private String gameState;
    
    @Embedded 
    private Address address;
    @JsonbTransient
    @ManyToMany(mappedBy="rentedGames")
    private Collection<Rental> rentalList;
    
    // Hacerlo Entidad con una tabla (id,consola) para darle más seguridad y qué nadie meta nuevas Consolas (sólo usamos las de nuestra BD)
    @ManyToOne
    //@JsonbTransient
    private Console console;
    @Transient
    private Long aux;
    
    @JsonbTransient
    @ManyToMany
    private Collection<Type> gameTypes;
    @Transient
    private Collection<Long> gameTypesId;
    public Game(){
        gameTypes = new ArrayList<Type>();
        rentalList = new ArrayList<Rental>();
    }
    public long getId() {
        return id;
    }
    public Long getAux(){
        return aux;
    }
    public void setAux(Long aux){
        this.aux = aux;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Collection<Rental> getRentalList() {
        return rentalList;
    }

    public void setRentalList(Collection<Rental> rentalList) {
        this.rentalList = rentalList;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    public Collection<Type> getGameTypes() {
        return gameTypes;
    }
    
    public Collection<Long> getGameTypesId() {
        return gameTypesId;
    }

    public void setGameTypes(Collection<Type> gameTypes) {
        this.gameTypes = gameTypes;
    }
    
    public void setGameTypesId(Collection<Long> gameTypesId) {
        this.gameTypesId = gameTypesId;
    }
    
    public void addGameTypeList(Type type){
        if(!getGameTypes().contains(type)){
            getGameTypes().add(type);
        }
        if(!type.getGameList().contains(this)){
            type.getGameList().add(this);
        }
    }
    
    public void addRental(Rental rental) {
        if (!getRentalList().contains(rental)) {
            getRentalList().add(rental);
        }
        if(!rental.getRentedGames().contains(this)){
            rental.getRentedGames().add(this);
        }
    }
    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", name=" + name + ", gameState=" + gameState + ", address=" + address + ", rentalList=" + rentalList + ", console=" + console + ", gameTypes=" + gameTypes + '}';
    }
}
