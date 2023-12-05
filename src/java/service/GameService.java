/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import model.entities.Game;
import model.entities.Console;
import model.entities.Type;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 *
 * @author mario robres
 */
@Stateless
@Path("game")
public class GameService extends AbstractFacade<Game>{
    
        @PersistenceContext(unitName = "Homework1PU")
        private EntityManager em;
        
        public GameService(){
            super(Game.class);
        }
        
        @Override
        @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public void create(Game entity) {
            super.create(entity);
        }
        
        @POST
        @Secured
        @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
        public Response createGame(Game entity){
            try {
                //Buscar si Game ya existe
                 if(entity.getAux()==null){
                    return Response.status(Response.Status.NOT_FOUND).entity("La consola asociada al juego no existe.").build();                
                }
                Console console = em.find(Console.class, entity.getAux());
                if(console == null){
                    return Response.status(Response.Status.NOT_FOUND).entity("La consola asociada al juego no existe.").build();                
                }
                Query query = em.
                        createNamedQuery("Game.findByNombreAndConsole", Game.class);
                query.setParameter("nombre", entity.getName());
                query.setParameter("console", console);
                List<Game> resultList = query.getResultList();
                if (!resultList.isEmpty()) {
                    // El juego ya existe
                    return Response.status(Response.Status.CONFLICT).entity("El juego ya existe").build();
                }
                
                // Guardar el nuevo juego en la base de datos
                // Set Console & Bidirectional Relationship
                entity.setConsole(console);
                // Set types
                Query query2 = em
                            .createNamedQuery("Type.findByPrimaryKeys", Type.class)
                            .setParameter("ids", entity.getGameTypesId());
                List<Type> types = query2.getResultList();
                if (types.size() != entity.getGameTypesId().size()) {
                    // El tipo asociado al juego no existe
                    return Response.status(Response.Status.NOT_FOUND).entity("Algun tipo asociado al juego no existe.").build();
                }
                entity.setGameTypes(types);
                super.create(entity);
                return Response.status(Response.Status.CREATED).build();
            } catch (Exception e) {
                // Manejar otros errores
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error en el servidor").build();
            }
        }
        
    /**
     *
     * @param id_type
     * @param id_console
     * @return
     */
        @GET
        @Produces({MediaType.APPLICATION_JSON})
        public Response allOrderedGames(@QueryParam("type") Long id_type, @QueryParam("console") Long id_console){
            try {
                if(id_type == null && id_console == null){
                    // No Type&Console -> Get ALL Games
                    Query queryNormal = em.createNamedQuery("Game.findAll", Game.class);
                        List<Game> allGames = queryNormal.getResultList();
                        return Response.ok(allGames.toString()).build();
                }else if(id_type == null){
                    if(id_console < 0){
                        return Response.status(Response.Status.BAD_REQUEST).entity("Parametro Console incorrecto").build();
                    }
                }else if(id_console == null){
                    if(id_type < 0){
                        return Response.status(Response.Status.BAD_REQUEST).entity("Parametro Type incorrecto").build();
                    }
                }else if (id_type < 0 || id_console < 0){
                    return Response.status(Response.Status.BAD_REQUEST).entity("Parametro Type y/o Console incorrecto").build();
                }
                if(id_console==null){
                    // Only Type -> Get games by type
                    Query typeQuery = em
                                .createNamedQuery("Game.findByType")
                                .setParameter("id_type", id_type);
                    List<Game> typGames = (List<Game>)typeQuery.getResultList();
                    return Response.ok(typGames).build();
                }
                if(id_type==null){
                    // Only Console -> Get games by console
                    Query typeQuery = em
                                .createNamedQuery("Game.findByType")
                                .setParameter("id_type", id_type);
                    List<Game> typGames = (List<Game>)typeQuery.getResultList();
                    return Response.ok(typGames).build();
                }
                // Get games by Type&Console
                Query queryTotal = em
                        .createNamedQuery("Game.findByTypeAndConsole", Game.class)
                        .setParameter("id_type", id_type)
                        .setParameter("id_console", id_console);
                List<Game> gamesTot = (List<Game>)queryTotal.getResultList();
                return Response.ok(gamesTot).build();
            } catch (IllegalArgumentException e) {
                // Manejar errores en los parámetros
                return Response.status(Response.Status.BAD_REQUEST).entity("Parametro no válido (debe ser un identificador)").build();
            }
        }
        
        @PUT
        @Path("{id}")
        @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public void edit(@PathParam("id") Long id, Game entity) {
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
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public Response find(@PathParam("id") Long id) {
            return Response.ok().entity(super.find(id)).build();
        }
        
        @Override
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public List<Game> findAll() {
            return super.findAll();
        }
        
        @Path("{from}/{to}")
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
        public List<Game> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
