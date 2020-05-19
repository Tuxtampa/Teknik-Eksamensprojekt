
import java.sql.*;


public class SqliteDB {
    Connection c = null;
    Statement stmt = null;

    SqliteDB()  {
        // try connect to DB
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:MediaSystem.sqlite");
            System.out.println("Connected to DB succesfully!");
        } catch (Exception e)  {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public void closeConnection() {
        try {

            System.out.println("Disconnected from database");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
