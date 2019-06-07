package Persistance;

import Domain.*;
import Domain.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

    public static boolean DBConnectionFailed = false;

    private DB() {
    }

    /**
     * Singleton DB object (note private constructor)
     *
     * @return instance of the DB class
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
            DBConnectionFailed = false;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            DBConnectionFailed = true;
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

    //EDUCATION PLAN RELATED METHODS

    /**
     * Queries database for courses by period using stored procedure
     * @return list of courses by period
     */
    public static ObservableList<CourseByPeriod> getCoursesByPeriod() {
        ObservableList<CourseByPeriod> listOfCoursesByPeriod = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getCoursesByPeriod}");
            ResultSet rs = cs.executeQuery();

            //add data to observableList
            while (rs.next()) {
                String information = rs.getString("fldInformation");
                String provider = rs.getString("fldProvider");
                String location = rs.getString("fldLocation");
                String period = rs.getString("fldPeriod");
                int periodID = rs.getInt("fldPeriodID");
                listOfCoursesByPeriod.add(new CourseByPeriod(information, provider, location, period, periodID));
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listOfCoursesByPeriod;
    }

    /**
     * Inserts new tblCoursePlan record into database
     * @param coursePlan the education plan object to be inserted
     * @param coursePeriod the courseByPeriod object used to set the PeriodID of the CoursePlan
     */
    public static void addCoursePlan(EducationPlan coursePlan, CourseByPeriod coursePeriod) {
        int rowsAffected = 0;
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.addCoursePlan(?,?,?,?)}");
            cs.setInt(1, coursePlan.getPriority());
            cs.setInt(2, coursePlan.getIsCompleted());
            cs.setInt(3, coursePlan.getPlanID());
            cs.setInt(4, coursePeriod.getPeriodID());
            rowsAffected = cs.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " rows was affected!");
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Queries the database for a CoursePlanID that has the provided dateID and planID
     * @param dateID dateID used to find the correct CoursePlanID
     * @param planID planID used to find the correct CoursePlanID
     * @return
     */
    @SuppressWarnings("Duplicates")
    public static int getCoursePlanID(int dateID, int planID) {
        int coursePlanID = 0;
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("SELECT dbo.getCoursePlanID(?,?)");
            cs.setInt(1, dateID);
            cs.setInt(2, planID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                coursePlanID = rs.getInt(1);
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return coursePlanID;
    }

    /**
     * Inserts a new record into tblEducationPlan in the database
     * @param employeeID employeeID used in the new education plan
     * @param consultantID consultantID used in the new education plan
     */
    public static void createNewEducationPlan(int employeeID, int consultantID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.createNewEducationPlan(?,?)}");
            cs.setInt(1, employeeID);
            cs.setInt(2, consultantID);
            rowsAffected = cs.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " rows was affected!");
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Calls UDF that finds an employees active education plan
     * @param employeeID the employee whos plan to find
     * @param consultantID the consultant who created the active plan
     * @return
     */
    @SuppressWarnings("Duplicates")
    public static int getActivePlanID(int employeeID, int consultantID) {
        int activePlanID = 0; //sentinel value
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("SELECT dbo.getActivePlanID(?,?)");
            cs.setInt(1, employeeID);
            cs.setInt(2, consultantID);
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                activePlanID = rs.getInt(1);
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return activePlanID;
    }

    /**
     * Toggles the field fldIsCompleted in tblCoursePlan between 1 an 0
     * @param coursePlanID the ID of the course plan to be changed
     */
    @SuppressWarnings("Duplicates")
    public static void toggleCoursePlanCompletion(int coursePlanID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.setCoursePlanAsCompleted(?)}");
            cs.setInt(1, coursePlanID);
            rowsAffected = cs.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " rows was affected!");
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Deletes a course plan record from tblCoursePlan
     * @param coursePlanID the ID of the course plan to be deleted
     */
    @SuppressWarnings("Duplicates")
    public static void removeCoursePlan(int coursePlanID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.deleteCoursePlan(?)}");
            cs.setInt(1, coursePlanID);
            rowsAffected = cs.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " rows was affected!");
            }
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Finds all course records in tblCourse
     * @return an observable list of courses
     */
    public static ObservableList<Course> getCourseList() {
        ObservableList<Course> listOfCourses = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllCourses}");
            ResultSet rs = cs.executeQuery();
            //add data to observableList
            while (rs.next()) {
                int courseID = rs.getInt("fldCourseID");
                String amuNumber = rs.getString("fldAMUNumber");
                String information = rs.getString("fldInformation");
                String additionalInformation = rs.getString("fldAdditionalInformation");
                int numberOfDays = rs.getInt("fldNumberOfDays");
                String location = rs.getString("fldLocation");
                String provider = rs.getString("fldProvider");
                listOfCourses.add(new Course(courseID, amuNumber, information, additionalInformation, numberOfDays, location, provider));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfCourses;
    }

    /**
     * Retrieve an ObservableList of Employees based on the CVR number they belong to.
     * @return an observable list of employees
     */
    public static ObservableList<Employee> getEmployeeList(String cvrNo) {
        ObservableList<Employee> listOfEmployees = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllEmployeesCVR (?)}");
            cs.setString(1, cvrNo);
            ResultSet rs = cs.executeQuery();

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

    /**
     * Retrieve an ObservableList of all Companies
     * @return an observable list of companies
     */
    @SuppressWarnings("Duplicates")
    public static ObservableList<Company> getCompanyList() {
        ObservableList<Company> listOfCompanies = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllCompanies}");
            ResultSet rs = cs.executeQuery();
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //add data to observableList
            while (rs.next()) {
                String name = rs.getString("fldname");
                String address = rs.getString("fldAddress");
                int zip = rs.getInt("fldZip");
                String email = rs.getString("fldEmail");
                String phoneNumber = rs.getString("fldPhoneNumber");
                String CVRNumber = rs.getString("fldCVRNumber");
                listOfCompanies.add(new Company(name, address, zip, email, phoneNumber, CVRNumber));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfCompanies;
    }

    /**
     * Retrieve an ObservableList of all Locations
     * @return an observable list of locations
     */
    public static ObservableList<Location> getLocationList() {
        ObservableList<Location> listOfLocations = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllLocations}");
            ResultSet rs = cs.executeQuery();
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();
            //add data to observableList
            while (rs.next()) {
                int locationID = rs.getInt("fldLocationID");
                String name = rs.getString("fldName");
                String address = rs.getString("fldAddress");
                int zip = rs.getInt("fldZip");
                listOfLocations.add(new Location(locationID, name, address, zip));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfLocations;
    }

    /**
     * Retrieve an ObservableList of Integers that represent all the PeriodIDs for a single course
     * @param courseID the courseID of the course
     * @return an observable list of Integer periodIDs
     */
    public static ObservableList<Integer> getPeriodList(int courseID) {
        ObservableList<Integer> listOfPeriods = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllPeriods (?)}");
            //insert the courseID
            cs.setInt(1, courseID);
            ResultSet rs = cs.executeQuery();
            //add data to observableList
            while (rs.next()) {
                int locationID = rs.getInt("fldPeriodID");
                listOfPeriods.add(new Integer(locationID));
            }

            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfPeriods;
    }

    /**
     * Retrieve an ObservableList of all Dates in a single Period
     * @param periodID the periodID of the period
     * @return an observable list of Dates
     */
    public static ObservableList<Date> getDatesList(int periodID) {
        ObservableList<Date> listOfDates = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getDates (?)}");
            //insert the courseID
            cs.setInt(1, periodID);
            ResultSet rs = cs.executeQuery();
            //add data to observableList
            while (rs.next()) {
                System.out.println("Found a record!");
                int dateID = rs.getInt("fldDateID");
                String date = rs.getString("fldDate");
                listOfDates.add(new Date(date, dateID));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfDates;
    }

    /**
     * Retrieve an ObservableList of all Providers
     * @return an observable list of Providers
     */
    @SuppressWarnings("Duplicates")
    public static ObservableList<Provider> getProviderList() {
        ObservableList<Provider> listOfProviders = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllProviders}");
            ResultSet rs = cs.executeQuery();
            //add data to observableList
            while (rs.next()) {
                String name = rs.getString("fldname");
                String address = rs.getString("fldAddress");
                int zip = rs.getInt("fldZip");
                String email = rs.getString("fldEmail");
                String phoneNumber = rs.getString("fldPhoneNumber");
                String CVRNumber = rs.getString("fldCVRNumber");
                listOfProviders.add(new Provider(name, address, zip, email, phoneNumber, CVRNumber));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfProviders;
    }

    /**
     * Retrieve an ObservableList of all EducationPlans for a single employee
     * @param employeeID the employeeID to be used for the search
     * @param isActive   true or false (is the education plan active)
     * @return an observable list of education plans
     */
    public ObservableList<EducationPlan> getEducationPlanList(int employeeID, boolean isActive) {
        ObservableList<EducationPlan> listOfEducationPlans = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.getEmployeeEducationPlan(?,?)}");
            //set values
            cs.setInt(1, employeeID);
            //converting parameter boolean to bit for sql use
            int isActiveBit;
            if (isActive) {
                isActiveBit = 1;
            } else {
                isActiveBit = 0;
            }
            cs.setInt(2, isActiveBit);
            //Create ResultSet
            ResultSet rs = cs.executeQuery();
            //add data to observableList
            while (rs.next()) {
                int dateID = rs.getInt("fldDateID");
                String date = rs.getString("fldDate");
                String information = rs.getString("fldInformation");
                String provider = rs.getString("fldProviderName");
                String location = rs.getString("fldLocationName");
                int priority = rs.getInt("fldPriority");
                int planID = rs.getInt("fldPlanID");
                int isCompleted = rs.getInt("fldIsCompleted");
                listOfEducationPlans.add(new EducationPlan(dateID, date, information, provider, location, priority, planID, isActiveBit, isCompleted, employeeID));
            }

            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfEducationPlans;
    }

    /**
     * Retrieve an ObservableList of all Qualifications for a single employee
     * @param employee the employee object
     * @return an observable list of qualifications
     */
    public static ObservableList<Qualification> getQualifications(Employee employee) {
        ObservableList<Qualification> listOfQualifications = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.listSpecificEmployeeQualifications (?)}");
            // Let the stored procedure know which employee to load the list from
            cs.setInt(1, employee.getEmployeeID());
            ResultSet rs = cs.executeQuery();

            //add data to observableList
            while (rs.next()) {
                int qualificationID = rs.getInt("fldQualificationID");
                String typeName = rs.getString("fldTypeName");
                String description = rs.getString("fldDescription");
                String levelName = rs.getString("fldLevelName");
                int employeeID = rs.getInt("fldEmployeeID");
                int typeID = rs.getInt("fldTypeID");
                int levelID = rs.getInt("fldLevelID");
                listOfQualifications.add(new Qualification(qualificationID, typeName, description, levelName, employeeID, typeID, levelID));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfQualifications;
    }

    /**
     * Retrieve an ObservableList of all Types for a single Qualification
     * @return an observable list of types
     */
    public static ObservableList<Type> getQualificationTypes() {
        ObservableList<Type> listOfQualificationTypes = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllTypes}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int typeID = rs.getInt("fldTypeID");
                String type = rs.getString("fldTypeName");
                listOfQualificationTypes.add(new Type(typeID, type));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfQualificationTypes;
    }

    /**
     * Retrieve an ObservableList of all Levels for a single Qualification
     * @return an observable list of levels
     */
    public static ObservableList<Level> getQualificationLevel() {
        ObservableList<Level> listOfLevels = FXCollections.observableArrayList();
        try {
            //connect
            connect();

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllLevels}");
            ResultSet rs = cs.executeQuery();
            while (rs.next()) {
                int levelID = rs.getInt("fldLevelID");
                String level = rs.getString("fldLevelName");
                listOfLevels.add(new Level(levelID, level));
            }
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return listOfLevels;
    }


    // UPDATE METHODS:

    /**
     * Make changes to the qualification of an employee in the database.
     * @param qualificationID the qualificationID
     * @param type            type of qualification
     * @param description     description of the employees qualification
     * @param level           level
     * @param employeeID      the employeeID
     * @param typeID          typeID (because Type has its own table in the DB)
     * @param levelID         levelID (because Level has its own table in the DB)
     * @return a qualification object. If the changes couldn't apply it will just return the object as null.
     */
    public static Qualification updateQualification(int qualificationID, String type, String description, String level, int employeeID, int typeID, int levelID) {
        int rowsAffected = 0;
        Qualification qualification = null;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.updateQualification (?, ?, ?, ?, ?)}");
            cs.setInt(1, typeID);
            cs.setString(2, description);
            cs.setInt(3, levelID);
            cs.setInt(4, employeeID);
            cs.setInt(5, qualificationID);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println(rowsAffected + " rows was affected!");
            qualification = new Qualification(qualificationID, type, description, level, employeeID, typeID, levelID);
        }
        return qualification;
    }

    /**
     * Make changes to an existing employee in the database.
     *
     * @param name        the new name
     * @param cprNumber   the new CPR number
     * @param email       the new e-mail address
     * @param phoneNumber the new phone number
     * @param company     the company that the employee works on (only used for creating a new employee instance)
     * @param employeeID  the employeeID
     * @return an employee object. If the changes couldn't apply it will return the object as null.
     */
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
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            employee = new Employee(employeeID, name, cprNumber, email, phoneNumber, company);
        }
        return employee;
    }

    // ADD/INSERT

    /**
     * Insert a new course into the database. The course will be bound to the provider you give it, and auto-generate a courseID.
     *
     * @param amuNumber      AMU number of the course
     * @param information    description of the course (short)
     * @param additionalInfo description in-depth
     * @param numberOfDays   course duration in days
     * @param locationID     locationID of the location where the course takes place
     * @param cvrNo          cvr number
     */
    public static void insertCourse(int amuNumber, String information, String additionalInfo, int numberOfDays, int locationID, String cvrNo) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addCourse(?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, amuNumber);
            cs.setString(2, information);
            cs.setString(3, additionalInfo);
            cs.setInt(4, numberOfDays);
            cs.setInt(5, locationID);
            cs.setString(6, cvrNo);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
    }

    /**
     * Add a qualification to an employee in the database.
     *
     * @param employee the employee object containing the employeeID
     */
    public static void insertQualification(Employee employee) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //default values
            int typeID = 1;
            String description = "Unspecified";
            int levelID = 1;

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addQualification(?,?,?,?)}");
            cs.setInt(1, typeID);
            cs.setString(2, description);
            cs.setInt(3, levelID);
            cs.setInt(4, employee.getEmployeeID());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
    }

    /**
     * Add a date to a period in the database
     *
     * @param newDate  the date you want to add
     * @param periodID periodID of the period
     */
    public static void insertDate(String newDate, int periodID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addDate(?,?)}");
            cs.setString(1, newDate);
            cs.setInt(2, periodID);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
    }

    /**
     * Add a period to a course in the database
     *
     * @param courseID courseID
     */
    public static void insertPeriod(int courseID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addPeriod(?)}");
            cs.setInt(1, courseID);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
    }

    /**
     * Insert a company into the database
     *
     * @param cvrNo   cvr number
     * @param address address/location
     * @param name    name of the company
     * @param mail    e-mail address
     * @param phoneNo phone number
     * @param zip     zip code
     * @return rows affected
     */
    @SuppressWarnings("Duplicates")
    public static int insertCompany(String cvrNo, String address, String name, String mail, String phoneNo, int zip) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addCompany(?, ?, ?, ?, ?, ?)}");
            cs.setString(1, cvrNo);
            cs.setString(2, address);
            cs.setString(3, name);
            cs.setString(4, mail);
            cs.setString(5, phoneNo);
            cs.setInt(6, zip);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
        return rowsAffected;
    }

    /**
     * Insert a provider into the database
     *
     * @param cvrNo   cvr number
     * @param name    name of the provider
     * @param address address/location
     * @param mail    e-mail address
     * @param phoneNo phone number
     * @param zip     zup code
     * @return rows affected
     */
    @SuppressWarnings("Duplicates")
    public static int insertProvider(String cvrNo, String name, String address, String mail, String phoneNo, int zip) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.addProvider(?, ?, ?, ?, ?, ?)}");
            cs.setString(1, cvrNo);
            cs.setString(2, name);
            cs.setString(3, address);
            cs.setString(4, mail);
            cs.setString(5, phoneNo);
            cs.setInt(6, zip);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
        return rowsAffected;
    }

    /**
     * Insert an employee into the database
     *
     * @param cprNo   cpr number
     * @param name    full name
     * @param mail    e-mail address
     * @param phoneNo phone number
     * @param cvrNo   cvr of the company the employee works on
     * @return rows affected
     */
    @SuppressWarnings("Duplicates")
    public static int insertEmployee(String cprNo, String name, String mail, String phoneNo, String cvrNo) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create statement
            CallableStatement cs = con.prepareCall("{call dbo.addEmployee(?, ?, ?, ?, ?)}");
            cs.setString(1, cprNo);
            cs.setString(2, name);
            cs.setString(3, mail);
            cs.setString(4, phoneNo);
            cs.setString(5, cvrNo);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0) {
            System.out.println("Added " + rowsAffected + " new record");
        }
        return rowsAffected;
    }

    // REMOVE/DELETE

    /**
     * Delete a qualification based on its qualificationID from the database. We receive a qualification object, but only use it to fetch the qualificationID.
     *
     * @param qualification a qualification object.
     */
    public static void deleteQualification(Qualification qualification) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteQualification (?)}");
            cs.setInt(1, qualification.getQualificationID());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }

    /**
     * Delete a course based on its course number from the database. We receive a course object, but only use it to fetch the course number.
     *
     * @param course a course object.
     */
    public static void deleteCourse(Course course) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteCourse (?)}");
            cs.setInt(1, course.getCourseID());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }

    /**
     * Delete a company and its employees based on its CVR number from the database. We receive a company object, but only use it to fetch the CVR number.
     *
     * @param company a company object.
     */
    public static void deleteCompany(Company company) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteCompany (?)}");
            cs.setString(1, company.getCVRNumber());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }

    /**
     * Delete a provider based on its CVR number from the database. We receive a provider object, but only use it to fetch the CVR number.
     *
     * @param provider a provider object.
     */
    public static void deleteProvider(Provider provider) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteProvider (?)}");
            cs.setString(1, provider.getCVRNumber());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }

    /**
     * Delete an employee based on its employeeID from the database. We receive an employee object, but only use it to fetch the employeeID.
     *
     * @param employee an employee object
     */
    @SuppressWarnings("Duplicates")
    public static void deleteEmployee(Employee employee) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteEmployee (?)}");
            cs.setInt(1, employee.getEmployeeID());

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }

    /**
     * Delete a date based on its dateID from the database.
     *
     * @param dateID an integer with the dateID
     */
    public static void deleteDate(int dateID) {
        int rowsAffected = 0;
        try {
            //connect
            connect();

            //create Statement
            CallableStatement cs = con.prepareCall("{call dbo.deleteDate (?)}");
            cs.setInt(1, dateID);

            rowsAffected = cs.executeUpdate();
            cs.close();
            close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        if (rowsAffected > 0) {
            System.out.println("Removed " + rowsAffected + " new record");
        }
    }


}
