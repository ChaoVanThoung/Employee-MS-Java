package co.stad.ems.dao;

import co.stad.ems.database.DbSingleton;
import co.stad.ems.domain.DepartmentManager;
import co.stad.ems.domain.Employees;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DepartmentManagerDaoImpl implements IDepartmentManagerDao {
    private final Connection connection;

    public DepartmentManagerDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    private boolean isEmployeeExists(int employeeId) {
        String sql = "SELECT COUNT(*) FROM Employees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
        return false;
    }

    private boolean isDepartmentManagerExists(int employeeId) {
        String sql = "SELECT COUNT(*) FROM department_manager WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
        return false;
    }

    @Override
    public void insertDepartmentManager(DepartmentManager manager) {
        int employeeId = manager.getEmployees().getId();
        if (!isEmployeeExists(employeeId)) {
            System.out.println("Error: Employee ID " + employeeId + " does not exist!");
            return;
        }

        String sql = """
                INSERT INTO department_manager (employee_id, department_id, from_date, to_date)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setString(2, manager.getDepartmentId());
            preparedStatement.setDate(3, manager.getFromDate());
            preparedStatement.setDate(4, manager.getToDate());
            int affectedRow = preparedStatement.executeUpdate();
            System.out.println(affectedRow > 0 ? "Inserted successfully!" : "Failed to insert");
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
    }

    @Override
    public void deleteDepartmentManager(int id) {
        if (!isDepartmentManagerExists(id)) {
            System.out.println("Error: Employee ID " + id + " is not a Department Manager!");
            return;
        }

        String sql = "DELETE FROM department_manager WHERE employee_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            int affectedRow = preparedStatement.executeUpdate();
            System.out.println(affectedRow > 0 ? "Deleted successfully!" : "Failed to delete");
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
    }

    @Override
    public DepartmentManager getDepartmentManagerById(int id) {
        String sql = """
            SELECT e.id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date, 
                   d.dept_name, dm.department_id, dm.from_date, dm.to_date
            FROM Employees e
            INNER JOIN department_manager dm ON e.id = dm.employee_id
            INNER JOIN departments d ON dm.department_id = d.id
            WHERE e.id = ?
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Employees employee = Employees.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .birthDate(resultSet.getDate("birth_date"))
                        .gender(resultSet.getString("gender").charAt(0))
                        .hireDate(resultSet.getDate("hire_date"))
                        .build();

                return DepartmentManager.builder()
                        .employees(employee)
                        .departmentId(resultSet.getString("department_id"))
                        .departmentName(resultSet.getString("dept_name")) // ✅ Added department name
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
        return null;
    }



    @Override
    public List<DepartmentManager> getAllDepartmentManagers() {
        String sql = """
            SELECT e.id, e.first_name, e.last_name, e.birth_date, e.gender, e.hire_date, 
                   d.dept_name, dm.department_id, dm.from_date, dm.to_date
            FROM Employees e
            INNER JOIN department_manager dm ON e.id = dm.employee_id
            INNER JOIN departments d ON dm.department_id = d.id
            """;

        List<DepartmentManager> departmentManagers = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Employees employee = Employees.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .birthDate(resultSet.getDate("birth_date"))
                        .gender(resultSet.getString("gender").charAt(0))
                        .hireDate(resultSet.getDate("hire_date"))
                        .build();

                departmentManagers.add(DepartmentManager.builder()
                        .employees(employee)
                        .departmentId(resultSet.getString("department_id"))
                        .departmentName(resultSet.getString("dept_name")) // ✅ Fixed: Added department name
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build());
            }
        } catch (SQLException sqlException) {
            System.out.println("SQL Error: " + sqlException.getMessage());
        }
        return departmentManagers;
    }
}