package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.DepartmentEmployee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentEmployeeDaoImpl implements DepartmentEmployeeDao {
    Connection connection;
    public DepartmentEmployeeDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }
    @Override
    public List<DepartmentEmployee> queryDepartmentEmployee() {
        String sql = """
                 SELECT de.employee_id,
                        de.department_id,
                        e.first_name,
                        e.last_name,
                        e.gender,
                        d.dept_name,
                        de.from_date,
                        de.to_date
                        FROM employees_department de
                        INNER JOIN employees e ON de.employee_id = e.id
                        INNER JOIN departments d ON de.department_id = d.id;
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet  = preparedStatement.executeQuery();){
            List<DepartmentEmployee> listDepartmentEmployee = new ArrayList<DepartmentEmployee>();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                String departmentId = resultSet.getString("department_id");
                DepartmentEmployee departmentEmployee = DepartmentEmployee.builder()
                        .employeeId(employeeId)
                        .departmentId(departmentId)
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
                listDepartmentEmployee.add(departmentEmployee);
            }
            return listDepartmentEmployee;
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return List.of();
    }

    @Override
    public void insertDepartmentEmployee(DepartmentEmployee newDepartmentEmployee) {
        String sql = """
                INSERT INTO "employees_department" (employee_id, department_id,from_date,to_date)
                VALUES (?,?,?,?)
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setInt(1, newDepartmentEmployee.getEmployeeId());
            preparedStatement.setString(2, newDepartmentEmployee.getDepartmentId());
            preparedStatement.setDate(3, newDepartmentEmployee.getFromDate());
            preparedStatement.setDate(4, newDepartmentEmployee.getToDate());
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Insert Success" : "Insert Failed";
            System.out.println(message);

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public DepartmentEmployee searchByEmployeeId(int employeeId) {
        String sql = """
            SELECT de.employee_id, de.department_id, de.from_date, de.to_date
            FROM employees_department de
            WHERE de.employee_id = ?;
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return DepartmentEmployee.builder()
                        .employeeId(resultSet.getInt("employee_id"))
                        .departmentId(resultSet.getString("department_id"))
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public List<DepartmentEmployee> searchByDepartmentId(String departmentId) {
        String sql = """
            SELECT de.employee_id,
                   de.department_id,
                   e.first_name,
                   e.last_name,
                   e.gender,
                   d.dept_name,
                   de.from_date,
                   de.to_date
            FROM employees_department de
            INNER JOIN employees e ON de.employee_id = e.id
            INNER JOIN departments d ON de.department_id = d.id
            WHERE de.department_id = ?;
            """;
        List<DepartmentEmployee> departmentEmployees = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                DepartmentEmployee departmentEmployee = DepartmentEmployee.builder()
                        .employeeId(resultSet.getInt("employee_id"))
                        .departmentId(resultSet.getString("department_id"))
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
                departmentEmployees.add(departmentEmployee);
            }
            return departmentEmployees;
        } catch (SQLException e) {
            System.out.println("❌ SQL Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;

    }

    @Override
    public void updateByEmployeeId(int employeeId, DepartmentEmployee updateDepartmentEmployee) {
        String sql = """
                UPDATE "employees_department"
                SET department_id = ?, from_date = ?, to_date = ?
                WHERE employee_id = ? 
              """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, updateDepartmentEmployee.getDepartmentId());
            preparedStatement.setDate(2, updateDepartmentEmployee.getFromDate());
            preparedStatement.setDate(3, updateDepartmentEmployee.getToDate());
            preparedStatement.setInt(4, employeeId);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Update Success" : "Update Failed";
            System.out.println(message);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }

    }

    @Override
    public void deleteByEmployeeId(int employeeId) {
        String sql = """
                DELETE FROM employees_department WHERE employee_id = ?;
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setInt(1, employeeId);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Delete Success" : "Delete Failed";
            System.out.println(message);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }
}
