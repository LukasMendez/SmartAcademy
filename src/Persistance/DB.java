package Persistance;

import Domain.*;
import Domain.Date;
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

    // TODO CAN WE AGREE TO USE THIS SOLUTION?
    // TODO TEST THIS WITH NO COMPANIES
    @SuppressWarnings("Duplicates")
    public static Company getFirstCompany() {
        Company company = null;

        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getFirstCompany}");
            ResultSet rs = cs.executeQuery();

            //add data to observableList
            while (rs.next()) {
                String name = rs.getString("fldname");
                String address = rs.getString("fldAddress");
                int zip = rs.getInt("fldZip");
                String email = rs.getString("fldEmail");
                String phoneNumber = rs.getString("fldPhoneNumber");
                String CVRNumber = rs.getString("fldCVRNumber");

                company = new Company(name,address,zip,email,phoneNumber,CVRNumber);
            }

            //close
            close();

            System.out.println("First company selected: " + company.getName());

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return company;
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

            //printing for debugging
            System.out.println();
            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listOfCoursesByPeriod;
    }

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

    public static ObservableList<Employee> getEmployeeList(String cvrNo) {
        ObservableList<Employee> listOfEmployees = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllEmployeesCVR (?)}");
            cs.setString(1,cvrNo);
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


    @SuppressWarnings("Duplicates") //TODO ONLY TEMPORARY SOLUTION. PLEASE SOLVE THIS
    public static ObservableList<Provider> getProviderList() {
        ObservableList<Provider> listOfProviders = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.getAllProviders}");
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

                listOfProviders.add(new Provider(name, address, zip, email, phoneNumber, CVRNumber));
            }

            close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return listOfProviders;
    }

    public ObservableList<EducationPlan> getEducationPlanList(int employeeID, boolean isActive){
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
            if(isActive){isActiveBit = 1;}
            else{isActiveBit = 0;}
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
            /*
            //printing for debugging
            for (int i = 0; i < listOfCourses.size(); i++) {
                System.out.println(listOfCourses.get(i).toString());
            }*/
            //close
            close();

        } catch(Exception e){
            System.err.println(e.getMessage());
        }

        return listOfEducationPlans;
    }


    public static ObservableList<Qualification> getQualifications(Employee employee) {

        ObservableList<Qualification> listOfQualifications = FXCollections.observableArrayList();

        try {
            //connect
            connect();

            // TODO MAKE IT UPDATE BASED ON THE ID THAT THE USER SELECTED

            //create Statement + ResultSet
            CallableStatement cs = con.prepareCall("{call dbo.listSpecificEmployeeQualifications (?)}");

            // Let the stored procedure know which employee to load the list from
            cs.setInt(1, employee.getEmployeeID());

            ResultSet rs = cs.executeQuery();
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();

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
            // close();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        if (rowsAffected > 0) {

            System.out.println(rowsAffected + " rows was affected!");
            qualification = new Qualification(qualificationID, type, description, level, employeeID, typeID, levelID);

        }

        return qualification;


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


    public static void insertQualification(Employee employee) {

        int rowsAffected = 0;

        Qualification qualification = null;

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
