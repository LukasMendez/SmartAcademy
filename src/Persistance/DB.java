package Persistance;

import Domain.Course;
import Domain.Employee;
import Domain.Qualification;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileReader;
import java.sql.*;
import java.util.Properties;

/**
 * Created by Lukas
 * 21-05-2019.
 */
public class DB {

    private static Connection con;
    private static Properties props = new Properties();
    private static String propertiesPath = System.getProperty("user.dir") + "\\src\\Foundation\\db.properties";

    private static DB instance;

    private DB() {
    }

    /**
     * Singleton DB object (note private constructor)
     *
     * @return
     */
    public static synchronized DB getInstance() {
        if (instance == null)
            instance = new DB();

        return instance;
    }

    /**
     * open connection using properties object
     */
    public static void connect() {
        try {
            //loading db.properties into Properties object
            FileReader fReader = new FileReader(propertiesPath);
            props.load(fReader);
            //setting variables
            String port = props.getProperty("port");
            String databaseName = props.getProperty("databaseName");
            String userName = props.getProperty("userName");
            String password = props.getProperty("password");
            //connecting to database
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:" + port + ";databaseName=" + databaseName, userName, password);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * close connection
     */
    public static void close() {
        try {
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static ObservableList<Course> getCourseList() {
        ObservableList<Course> listOfCourses = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllCourses}");
            ResultSet rs = cs.executeQuery();
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData(); //TODO Will you use this or not??

            //add data to observableList
            while (rs.next()) {
                String courseNumber = rs.getString("fldAMUNumber");
                String information = rs.getString("fldInformation");
                String additionalInformation = rs.getString("fldAdditionalInformation");
                int numberOfDays = rs.getInt("fldNumberOfDays");
                String location = rs.getString("fldLocation");
                String provider = rs.getString("fldProvider");
                listOfCourses.add(new Course(courseNumber, information, additionalInformation, numberOfDays, location, provider));
            }
            /*
            //printing for debugging
            for (int i = 0; i < listOfCourses.size(); i++) {
                System.out.println(listOfCourses.get(i).toString());
            }*/
            //close
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listOfCourses;
    }

    public static ObservableList<Employee> getEmployeeList() {
        ObservableList<Employee> listOfEmployees = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllEmployees}");
            ResultSet rs = cs.executeQuery();
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData(); //TODO Will you use this or not??

            //add data to observableList
            while (rs.next()) {
                int employeeID = rs.getInt("fldEmployeeID");
                String name = rs.getString("fldname");
                String CPRNumber = rs.getString("fldCPRNumber");
                String email = rs.getString("fldEmail");
                String phoneNumber = rs.getString("fldPhoneNumber");
                String company = rs.getString("fldCompany");
                listOfEmployees.add(new Employee(employeeID, name, CPRNumber, email, phoneNumber, company));
            }

            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listOfEmployees;
    }


    public static ObservableList<Qualification> getQualifications(Employee employee){

        ObservableList<Qualification> listOfQualifications = FXCollections.observableArrayList();


        // TODO Make it possible to get a list of all qualifications for a specific employee. Make sure that we display description and not just ID's.





        return listOfQualifications;

    }








    public static Employee updateEmployee(String name, String cprNumber, String email, String phoneNumber, String company, int employeeID) {

        int rowsAffected = 0;

        Employee employee = null;

        try {

            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.updateEmployee(?,?,?,?,?)}");

            cs.setString(1, cprNumber);
            cs.setString(2, name);
            cs.setString(3, email);
            cs.setString(4, phoneNumber);
            cs.setInt(5, employeeID);

            rowsAffected = cs.executeUpdate();


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0) {

            employee = new Employee(employeeID, name, cprNumber, email, phoneNumber, company);

        }

        return employee;


    }

}
