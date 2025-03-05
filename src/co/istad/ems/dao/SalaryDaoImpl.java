package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.Salary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalaryDaoImpl implements SalaryDao {
    Connection connection;

    public SalaryDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public List<Salary> queryAllSalaries() {
        String sql = """
                SELECT 
                e.id AS employee_id,
                e.first_name,
                e.last_name,
                s.from_date,
                s.amount,
                s.to_date
                FROM
                salaries s
                JOIN
                employees e ON s.employee_id = e.id;
                """;

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            ResultSet resultSet= preparedStatement.executeQuery();
            List<Salary> listSalary = new ArrayList<>();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                Salary salary = Salary.builder()
                        .employeesId(employeeId)
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .amount(resultSet.getInt("amount"))
                        .build();
                listSalary.add(salary);
            }
            return listSalary;
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }

        return List.of();
    }

    @Override
    public int count() {
        String sql= """
                SELECT COUNT(*) AS count FROM "salaries"
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            ResultSet resultSet= preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        }catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
     return 0;
    }

    @Override
    public void insertSalary(Salary newSalary) {
        String sql = """
                INSERT INTO "salaries" (employee_id, from_date, amount, to_date)
                VALUES (?, ?, ?, ?)
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, newSalary.getEmployeesId());
            preparedStatement.setDate(2, newSalary.getFromDate());
            preparedStatement.setInt(3, newSalary.getAmount());
            preparedStatement.setDate(4, newSalary.getToDate());
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Inserted successfully" : "Insert failed";
            System.out.println(message);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateSalaryById(Integer id, Salary updateSalary) {
        String sql = """
                UPDATE "salaries"
                SET from_date = ?, to_date = ?,amount = ?
                WHERE employee_id = ?
                """;
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
            preparedStatement.setDate(1, updateSalary.getFromDate());
            preparedStatement.setDate(2, updateSalary.getToDate());
            preparedStatement.setInt(3, updateSalary.getAmount());
            preparedStatement.setInt(4, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Updated successfully" : "Update failed";
            System.out.println(message);

        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void deleteSalaryById(Integer id) {
            String sql = """
                    DELETE FROM "salaries"
                    WHERE employee_id = ?
                    """;
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql);){
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                String message = rowsAffected > 0 ? "Deleted successfully" : "Delete failed";
                System.out.println(message);
            }catch (SQLException sqlException){
                System.out.println(sqlException.getMessage());
            }
    }

    @Override
    public List<Object[]> searchSalary(Integer amount) {
        String sql = """
                SELECT s.*, e.first_name, e.last_name
                FROM "salaries" s
                JOIN employees e ON s.employee_id = e.id
                WHERE s.amount = ?
                """;
        List<Object[]> results = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, amount);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Salary salary = new Salary(
                        resultSet.getInt("employee_id"),
                        resultSet.getDate("from_date"),
                        resultSet.getInt("amount"),
                        resultSet.getDate("to_date")
                );
                String employeeName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                results.add(new Object[]{salary, employeeName});
            }
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return results;
    }
}
