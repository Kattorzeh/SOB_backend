/*package test;

import java.util.Collection;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import model.entities.*;
import service.*;

public class AppTest {

    private final static String TITLE = "Id Generation Example";
    private final static String DESCRIPTION
            = "This example demonstates how to use auto id generation/table id generation\n"
            + "It allows you to create/find employees. All operations\n"
            + "are persisted to the database.";

    public static void main(String[] args) {
        EntityManagerFactory emf
                = Persistence.createEntityManagerFactory("EmployeeService");
        EntityManager em = emf.createEntityManager();
        EmployeeService service = new EmployeeService(em);

        System.out.println(TITLE);
        System.out.println(DESCRIPTION);

        //  create and persist an employee
        em.getTransaction().begin();
        Employee emp = service.createEmployee("John Doe", 45000, "6133447241");
        em.getTransaction().commit();
        System.out.println("Persisted " + emp);

        em.getTransaction().begin();
        emp = service.createEmployee("Open Systems", 100000, "1234567");
        em.getTransaction().commit();
        System.out.println("Persisted " + emp);

        // find all employees
        Collection<Employee> emps = service.findAllEmployees();
        for (Employee e : emps) {
            System.out.println("Found: " + e);
        }

        // close the EM and EMF when done
        em.close();
        emf.close();
    }
}*/
