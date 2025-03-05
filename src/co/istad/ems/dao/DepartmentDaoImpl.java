package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {

    public static final String GREEN = "\u001B[32m"; // Success color
    public static final String RED = "\u001B[31m";   // Error color
    public static final String RESET = "\u001B[0m";  // Reset color to default

    private Connection connection;
    public DepartmentDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();

    }

    @Override
    public List<Department> queryAllDepartments() {
        String sql = "select * from departments";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Department> listDepartment = new ArrayList<>();
            while (resultSet.next()) {
                Department department =  new Department(
                  resultSet.getString("id"),
                  resultSet.getString("dept_name")
                );
                listDepartment.add(department);
            }
            return listDepartment;
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return List.of();
    }

    @Override
    public void insertNewDepartment(Department department) {

        String sql = """
                INSERT INTO "departments" (id , dept_name)
                VALUES (?, ?)
                """;
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setString(1, department.getId());
            preparedStatement.setString(2, department.getDepartmentName());
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? GREEN + " new successfully" + RESET : RED +"[31mAdd new failed" + RESET;
            System.out.println(message);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateDepartmentById(String id, Department department) {
        String sql = """
                UPDATE "departments"
                SET dept_name=?
                WHERE id=?
                """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, department.getDepartmentName());
            preparedStatement.setString(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Update Success" : "Update failed";
            System.out.println(message);
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void deleteDepartmentById(String id) {
        String sql = """
                DELETE FROM departments
                WHERE id = ?
                """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? GREEN + "Deleted successfully" + RESET : RED +"Delete failed" + RESET;
            System.out.println(message);

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public Department searchDepartmentById(String id) {

        String sql = """
                select * from departments where id = ?
                """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Department department = null;
            while (resultSet.next()) {
                department = Department.builder()
                        .id(resultSet.getString("id"))
                        .departmentName(resultSet.getString("dept_name"))
                        .build();
            }
            return department;

        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return null;
    }


}
