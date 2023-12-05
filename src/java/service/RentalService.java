/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import model.entities.Customer;
import model.entities.Game;
import model.entities.Rental;

/**
 *
 * @author mario robres
 */
@Stateless
@Path("rental")
public class RentalService extends AbstractFacade<Rental> {
    
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    public RentalService() {
        super(Rental.class);
    }
    
    @POST
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response createRental(Rental entity){
        //try {
            // Comprobar que los games existen
            Query queryGame = em
                    .createNamedQuery("Game.findByPrimaryKeys", Game.class)
                    .setParameter("ids", entity.getRentedGamesId());
            List<Game> games = queryGame.getResultList();
            if(games.size() != entity.getRentedGamesId().size()){
                // Si aglun Game no existeo esta BOOKED -> no se crea la rental
                return Response.status(Response.Status.NOT_FOUND).entity("Algun Game asociado al Rental no existe o esta BOOKED.").build();
            }
            // Add Games to Rental, change Game State and sum rentalPrice
            double rentedPrice=0;
            for(Game g:games){
                rentedPrice+=g.getPrice();
                g.setGameState("BOOKED");
            }
            rentedPrice = rentedPrice*entity.getRentedDays();
            entity.setPrice(rentedPrice);
            entity.setRentedGames(games);
            // Set Customer & Bidirectional Relationship
            if(entity.getRentedCustomerId()==null){
                return Response.status(Response.Status.BAD_REQUEST).entity("No hay Consumer asociado al Rental.").build();                
            }
            Customer customer = em.find(Customer.class, entity.getRentedCustomerId());
            if(customer == null){
                return Response.status(Response.Status.NOT_FOUND).entity("El Consumer asociado al Rental no existe.").build();                
            }
            entity.setRentedCustomer(customer);    
            super.create(entity);
            // Devolver la respuesta con la información solicitada
            JsonObject responseJson = Json.createObjectBuilder()
                    .add("rentalId", entity.getId())
                    .add("totalPrice", entity.getPrice())
                    .add("returnDate", entity.getReturnDate().toString())
                    .add("rentedGames",games.toString())
                    .build();

            return Response.status(Response.Status.CREATED).entity(responseJson).build();
        /*}catch (Exception e) {
            // Manejar otros errores
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error en el servidor").build();
        }*/
    }
    
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Rental entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Rental entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Secured
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Long id) {
        try {
                // Si no me mandan Id -> GET ALL RENTALS
                if (id==null){
                    Query queryAll = em
                            .createNamedQuery("Rental.findAll", Rental.class);
                    List<Rental> allRentals = queryAll.getResultList();
                    return Response.ok(allRentals).build();
                }
                // GET Rental ID_parámetro
                Query query = em
                    .createNamedQuery("Rental.findById", Rental.class)
                    .setParameter("id", id);
                Rental rental = (Rental) query.getSingleResult();
                
                if (rental != null) {
                    return Response.ok(rental).build();
                } else {
                    return Response.status(Response.Status.NOT_FOUND).entity("Rental con ID " + id + " no encontrado.").build();
                }
            } catch (NoResultException e) {
                return Response.status(Response.Status.NOT_FOUND).entity("Rental con ID " + id + " no encontrado.").build();
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Parametros invalidos.").build();
            }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Rental> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Rental> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
