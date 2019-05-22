package Persistance;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
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

    /*
    public static void test(){
        try {
            Statement stmt = con.createStatement();
            String record="INSERT INTO tblCity "
                    + "VALUES (6200,'Aabenraa')";
            stmt.executeUpdate(record);
            stmt.close();

        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }*/

}
