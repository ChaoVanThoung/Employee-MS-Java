package co.stad.ems.Main;

import co.stad.ems.database.DbSingleton;
import co.stad.ems.view.EmployeeInfoView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        UI ui = new UI();
//        ui.ui();
        Scanner input = new Scanner(System.in);
            EmployeeInfoView emp = new EmployeeInfoView();
            emp.displayEmployeeInfoManagement(input);

//        try {
//            Connection connection = DbSingleton.getInstance().getConnection();
//            if (connection != null) {
//                System.out.println("Database connected successfully!");
//
//                // Run a simple test query
//                Statement statement = connection.createStatement();
//                ResultSet resultSet = statement.executeQuery("SELECT version();");
//                if (resultSet.next()) {
//                    System.out.println("PostgreSQL Version: " + resultSet.getString(1));
//                }
//            } else {
//                System.out.println("Failed to connect to database.");
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
    }
}