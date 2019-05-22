package Persistance;

import Domain.Course;
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
    private DB(){
    }

    /**
     * Singleton DB object (note private constructor)
     * @return
     */
    public static synchronized DB getInstance(){
        if(instance == null)
            instance =new DB();

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


    public static ObservableList<Course> testGetCourseList(){
        ObservableList<Course> listOfCourses = FXCollections.observableArrayList();
        try {
            //connect
            connect();
            //create Statement + ResultSet
            Statement stmt = con.createStatement();
            String record="SELECT * FROM tblCourse";
            ResultSet rs = stmt.executeQuery(record);
            //create ResultSetMetaData
            ResultSetMetaData rsmd = rs.getMetaData();

            //add data to observableList
            while (rs.next()) {
                int courseNumber = rs.getInt("fldCourseNumber");
                String information = rs.getString("fldInformation");
                String additionalInformation = rs.getString("fldAdditionalInformation");
                int numberOfDays = rs.getInt("fldNumberOfDays");
                int locationID = rs.getInt("fldLocationID");
                String CVRNumber = rs.getString("fldCVRNumber");
                listOfCourses.add(new Course(courseNumber, information, additionalInformation, numberOfDays, locationID, CVRNumber));
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

        return listOfCourses;
    }

}
