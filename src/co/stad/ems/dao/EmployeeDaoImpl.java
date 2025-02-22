package co.stad.ems.dao;

import co.stad.ems.dao.IEmployeeDao;
import co.stad.ems.database.DbSingleton;
import co.stad.ems.domain.Employees;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@Builder
public class EmployeeDaoImpl implements IEmployeeDao {
    private final Connection connection;

    public EmployeeDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public void addEmployee(Employees employee) {
        String sql = """
                INSERT INTO employees (id, birth_date, first_name, last_name, gender, hire_date) VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.setDate(2, employee.getBirthDate());
            preparedStatement.setString(3, employee.getFirstName());
            preparedStatement.setString(4, employee.getLastName());
            preparedStatement.setString(5, String.valueOf(employee.getGender()));
            preparedStatement.setDate(6, employee.getHireDate());

            preparedStatement.executeUpdate();
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateEmployee(int id, Employees updatedEmployee) {
        String sql = """
        UPDATE employees (birth_date, first_name, last_name, gender, hire_date)\s
            VALUE (?, ?, ?, ?, ?);
       \s""";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, updatedEmployee.getBirthDate());
            preparedStatement.setString(3, updatedEmployee.getFirstName());
            preparedStatement.setString(4, updatedEmployee.getLastName());
            preparedStatement.setString(5, String.valueOf(updatedEmployee.getGender()));
            preparedStatement.setDate(6, updatedEmployee.getHireDate());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                System.out.println("Update failed: No employee found with ID " + id);
            }
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
            System.out.println(sqlException.getMessage());        }
    }

    @Override
    public Employees getEmployeeById(int id) {
        String sql = """
            SELECT * FROM employees WHERE id=?
        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Employees employee = null;
            while (resultSet.next()) {
                employee = Employees.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .gender(resultSet.getString("gender").charAt(0))
                        .birthDate(resultSet.getDate("birth_date"))
                        .hireDate(resultSet.getDate("hire_date"))
                        .build();
            }
            return employee;
        } catch (SQLException sqlException) {
            System.out.println("Error: " + sqlException.getMessage());
        }
        return null;
    }

    @Override
    public List<Employees> getAllEmployees() {
        String sql = "SELECT * FROM employees";
        List<Employees> employees = new ArrayList<>(); // Move this outside try block

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date birthDate = resultSet.getDate("birth_date");
                Date hireDate = resultSet.getDate("hire_date");

                Employees employee = new Employees(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        birthDate,  // No need for conversion
                        resultSet.getString("gender").charAt(0),
                        hireDate   // No need for conversion
                );
                employees.add(employee);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return employees;
    }


}
