/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
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
import model.entities.Rental;

/**
 *
 * @author mario robres
 */
@Stateless
@Path("customer")
public class CustomerService extends AbstractFacade<Customer>{
    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    public CustomerService() {
        super(Customer.class);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response allCustomersJSON(){
        try {
            Query query = em
                    .createNamedQuery("Customer.findAllCustomer", Customer.class);
            List<Customer> customers = query.getResultList();
            if(customers.isEmpty()){
                return Response.status(Response.Status.NOT_FOUND).entity("No existen Customers.").build();
            }
            return Response.ok(customers).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error retrieving customer data.").build();
        }
    }
    
    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Customer entity) {
        super.create(entity);
    }

    @PUT
    @Secured
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Long id, Customer entity) {
        Customer existingCustomer = super.find(id);
        // Verifica si el cliente con el ID proporcionado existe
        if (existingCustomer == null) {
            // Crea un nuevo Customer con dicho ID
            super.create(entity);
            return Response.status(Response.Status.CREATED).build();
        }
        
        // Modifica datos Customer
        Query query = em
                .createNamedQuery("Rental.findByPrimaryKeys", Rental.class)
                .setParameter("ids", entity.getReservationsId());
        List<Rental> rentals = query.getResultList();
        if(rentals.size() != entity.getReservationsId().size()){
            // Alguna Rental no existe -> No se modifica el Cliente
            return Response.status(Response.Status.NOT_FOUND).entity("Algun Rental asociado al Customer no existe.").build();
        }
        existingCustomer.setReservations(rentals);
        existingCustomer.setName(entity.getName());
        existingCustomer.setEmail(entity.getEmail());
        existingCustomer.setPassword(entity.getPassword());
        // Guarda els canvis al sistema
        super.edit(existingCustomer);
        return Response.status(Response.Status.OK).entity("El Customer ha sido modificado correctamente.").build();
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Secured
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Long id) {
        if(super.find(id) == null){
            return Response.status(Response.Status.NOT_FOUND).entity("No existen Customer con ese ID.").build();
        }
        return Response.ok().entity(super.find(id)).build();
    }

    
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Customer> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Customer> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
