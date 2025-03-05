package co.istad.ems.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbSingleton {
    private static volatile DbSingleton dbSingleton;
    private Connection connection;

    private DbSingleton() {
        try {
            String url = "jdbc:postgresql://localhost:5432/employee_ms";
            String user = "postgres";
            String password = "6229";
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, user, password);
//            System.out.println("Database connection established successfully.");
        } catch (SQLException sqlException) {
            throw new RuntimeException("Failed to connect to database: " + sqlException.getMessage(), sqlException);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static DbSingleton getInstance() {
        if (dbSingleton == null) {
            synchronized (DbSingleton.class) {  // Double-checked locking
                if (dbSingleton == null) {
                    dbSingleton = new DbSingleton();
                }
            }
        }
        return dbSingleton;
    }

    public Connection getConnection() {
        if (connection == null) {
            throw new IllegalStateException("Database connection is null. Check DbSingleton.");
        }
        return connection;
    }
}
