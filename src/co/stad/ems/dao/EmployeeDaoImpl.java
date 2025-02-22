package co.stad.ems.dao;

import co.stad.ems.database.DbSingleton;
import co.stad.ems.domain.Employees;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDaoImpl implements EmployeeDao {
    Connection connection;
    public EmployeeDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public Employees searchById(int employeeId) {
        String sql = "select * from employees where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Employees employees = null;
            while (resultSet.next()) {
                employees = Employees.builder()
                        .id(resultSet.getInt("id"))
                        .birth_date(resultSet.getDate("birth_date"))
                        .first_name(resultSet.getString("first_name"))
                        .last_name(resultSet.getString("last_name"))
                        .gender(resultSet.getString("gender").charAt(0))
                        .hire_date(resultSet.getDate("hire_date"))
                        .build();
            }
            return employees;

        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
        return null;
    }
}
