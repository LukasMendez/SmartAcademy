import Domain.Employee;
import Persistance.DB;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * Created by Lukas
 * 09-06-2019.
 */
public class DBTest {

    public DB instance;


    @org.junit.Before
    public void setUp() throws Exception {
        instance = DB.getInstance();

    }


    /**
     * In this test we check that the object that we are trying to create from the DB method is not null when its being assigned
     * to the Employee instance. We do that by using valid input in the employeeID.
     */
    @org.junit.Test
    public void updateEmployeeNotNull() {

        int employeeID = 5;
        Employee employee = instance.updateEmployee("DemoEmployee", "123456",
                "newName@email.com","+4500000000","UTCompany",employeeID);

        assertNotNull(employee);

    }

    /**
     * In this test we check that the object that we are trying to create from the DB method is null when its being assigned
     * to the Employee instance. We do that by using an invalid input in the employeeID.
     */
    @org.junit.Test
    public void updateEmployeeNull() {

        int employeeID = -1;
        Employee employee = instance.updateEmployee("DemoEmployee", "123456",
                "newName@email.com","+4500000000","UTCompany",employeeID);

        assertNull(employee);

    }

    /**
     * In this test we want to check if a record is updated correctly. We try to change the e-mail address from a specific employee,
     * and then check if the data was actually stored in the database.
     */
    @org.junit.Test
    public void updateEmployeeAttributes() {

        int employeeID = 5;
        Employee employee = instance.updateEmployee("DemoEmployee", "123456",
                "newName@email.com","+4500000000","UTCompany",employeeID);

        String expectedEmail = "newName@email.com";
        String actualEmail = employee.getEmail();

        assertEquals(expectedEmail,actualEmail);

    }


    /**
     * In this test we try to insert a Company record in the database. We then check the amount of rows affected to see
     * if it equals the amount expected.
     */
    @org.junit.Test
    public void insertCompany() {

        // Rows affected
        int expected = 0;
        int actual = instance.insertCompany("55828415","Skanderborgvej 277",
                "STARK","info@stark.dk","+4589343434",1234);

        assertEquals(expected,actual);
    }
}


