package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.DepartmentEmployee;
import co.istad.ems.domain.DepartmentManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentMangerDaoImpl implements DepartmentMangerDao {
    Connection connection;
    public DepartmentMangerDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public List<DepartmentManager> queryDepartmentEmployee() {
        String sql = """
                 SELECT dm.employee_id,
                        dm.department_id,
                        e.first_name,
                        e.last_name,
                        e.gender,
                        d.dept_name,
                        dm.from_date,
                        dm.to_date
                        FROM department_manager dm
                        INNER JOIN employees e ON dm.employee_id = e.id
                        INNER JOIN departments d ON dm.department_id = d.id;
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet  = preparedStatement.executeQuery();){
            List<DepartmentManager> listDepartmentManagers = new ArrayList<>();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                String departmentId = resultSet.getString("department_id");
                DepartmentManager departmentManager = DepartmentManager.builder()
                        .employeeId(employeeId)
                        .departmentId(departmentId)
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
                listDepartmentManagers.add(departmentManager);
            }
            return listDepartmentManagers;
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return List.of();
    }

    @Override
    public void insertDepartmentManager(DepartmentManager newDepartmentManager) {
        String sql = """
                INSERT INTO "department_manager" (employee_id, department_id,from_date,to_date)
                VALUES (?,?,?,?)
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setInt(1, newDepartmentManager.getEmployeeId());
            preparedStatement.setString(2, newDepartmentManager.getDepartmentId());
            preparedStatement.setDate(3, newDepartmentManager.getFromDate());
            preparedStatement.setDate(4, newDepartmentManager.getToDate());
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Insert Success" : "Insert Failed";
            System.out.println(message);

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateByEmployeeId(int employeeId, DepartmentManager updateDepartmentManager) {
        String sql = """
                UPDATE "department_manager"
                SET department_id = ?, from_date = ?, to_date = ?
                WHERE employee_id = ? 
              """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, updateDepartmentManager.getDepartmentId());
            preparedStatement.setDate(2, updateDepartmentManager.getFromDate());
            preparedStatement.setDate(3, updateDepartmentManager.getToDate());
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
                DELETE FROM department_manager WHERE employee_id = ?;
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

    @Override
    public DepartmentManager searchByDepartmentId(String departmentId) {
        String sql = """
            SELECT dm.employee_id,
                   dm.department_id,
                   e.first_name,
                   e.last_name,
                   e.gender,
                   d.dept_name,
                   dm.from_date,
                   dm.to_date
            FROM department_manager dm
            INNER JOIN employees e ON dm.employee_id = e.id
            INNER JOIN departments d ON dm.department_id = d.id
            WHERE dm.department_id = ?;
            """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, departmentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return DepartmentManager.builder()
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
    public DepartmentManager searchByEmployeeId(int employeeId) {
        String sql = """
            SELECT dm.employee_id, dm.department_id, dm.from_date, dm.to_date
            FROM department_manager dm
            WHERE dm.employee_id = ?;
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return DepartmentManager.builder()
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
}
