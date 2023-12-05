/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
    @NamedQuery(name="Console.findGameByConsole",
                query="SELECT c FROM Console c WHERE c.gameList IN :console"),
    @NamedQuery(name="Console.findByPrimaryKey",
                query="SELECT e FROM Console e WHERE e.id = :id")
})
public class Console implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToMany (mappedBy="console")
    @JsonbTransient
    private Collection<Game> gameList;
    
    public Console(){
        gameList = new ArrayList<Game>();
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Collection<Game> getGameList() {
        return gameList;
    }

    public void setGameList(Collection<Game> gameList) {
        this.gameList = gameList;
    }
    
    public void addGame(Game game) {
        if (!getGameList().contains(game)) {
            getGameList().add(game);
        }
    }
    
    @Override
    public String toString() {
        return "Console{" + "id=" + id + ", gameList=" + gameList + '}';
    }
}
