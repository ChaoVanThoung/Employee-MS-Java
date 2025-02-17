package co.stad.ems.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbSingleton {
    private static DbSingleton dbSingleton;
    private Connection connection;
    private DbSingleton() {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "6229";

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url,user,password);
        } catch (SQLException sqlException){
            System.out.println("SQLException: " + sqlException.getMessage());
        } catch (ClassNotFoundException classNotFoundException){
            System.out.println("ClassNotFoundException: " + classNotFoundException.getMessage());
        }
    }
    public static DbSingleton getInstance() {
        if (dbSingleton == null) {
            dbSingleton = new DbSingleton();
        }
        return dbSingleton;
    }
    public Connection getConnection() {
        return connection;
    }
}
