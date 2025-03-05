package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.Employees;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    Connection connection;

    public EmployeeDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public void addEmployee(Employees employee) {
        String sql = """
                INSERT INTO employees (id, birth_date, first_name, last_name, gender, hire_date) 
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setDate(2, employee.getBirthDate());
            preparedStatement.setString(3, employee.getFirstName());
            preparedStatement.setString(4, employee.getLastName());
            preparedStatement.setString(5, String.valueOf(employee.getGender()));
            preparedStatement.setDate(6, employee.getHireDate());
            int rowAffected = preparedStatement.executeUpdate();
            String message = rowAffected > 0 ? "Insert Success" : "Insert Failure";
            System.out.println(message);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateEmployee(int id, Employees updatedEmployee) {
        String sql = """
                UPDATE employees
                SET birth_date = ?, first_name = ?, last_name = ?, gender = ?, hire_date = ?
                WHERE id = ?;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setDate(1, updatedEmployee.getBirthDate());
            preparedStatement.setString(2, updatedEmployee.getFirstName());
            preparedStatement.setString(3, updatedEmployee.getLastName());
            preparedStatement.setString(4, String.valueOf(updatedEmployee.getGender()));
            preparedStatement.setDate(5, updatedEmployee.getHireDate());
            preparedStatement.setInt(6, id);
            int affectedRows = preparedStatement.executeUpdate();
            System.out.println(affectedRows != 0 ? "Updated employee successfully!" : "Update failed.");

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void deleteEmployee(int id) {
        String sql = """
                DELETE FROM employees WHERE id=?
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Delete failed: No employee found with ID " + id);
            }
            System.out.println("Employee with " + id + " deleted successfully   ");
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public List<Employees> getAllEmployees() {
        String sql = """
            SELECT * FROM employees
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Employees> listEmployees = new ArrayList<>();
            while (resultSet.next()) {
                Employees employee = new Employees();
                employee.setId(resultSet.getInt("id"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                employee.setGender(resultSet.getString("gender").charAt(0));  // Assuming gender is stored as a String like "M" or "F"
                employee.setBirthDate(resultSet.getDate("birth_date"));
                employee.setHireDate(resultSet.getDate("hire_date"));

                listEmployees.add(employee);
            }
            return listEmployees;
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
        }
        return null;
    }

    @Override
    public Employees searchById(int employeeId) {
        String sql = "select * from employees where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Employees employees = null;
            while (resultSet.next()) {
                employees = Employees.builder()
                        .id(resultSet.getInt("id"))
                        .birthDate(resultSet.getDate("birth_date"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .gender(resultSet.getString("gender").charAt(0))
                        .hireDate(resultSet.getDate("hire_date"))
                        .build();
            }
            return employees;

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }
}
