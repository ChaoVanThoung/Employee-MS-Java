package co.istad.ems.dao;

import co.istad.ems.database.DbSingleton;
import co.istad.ems.domain.Title;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TitleDaoImpl implements TitleDao {
    Connection connection;
    private EmployeeDao employeeDao = new EmployeeDaoImpl();

    public TitleDaoImpl() {
        connection = DbSingleton.getInstance().getConnection();
    }

    @Override
    public List<Title> queryAllTitle() {
        String sql = """
                   SELECT
                                   e.id AS employee_id,
                                   e.first_name,
                                   e.last_name,
                                   t.title,
                                   t.from_date,
                                   t.to_date
                               FROM
                                   titles t
                               JOIN
                                   employees e ON t.employee_id = e.id;
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Title> listTitles = new ArrayList<>();
            while (resultSet.next()) {
                int employeeId = resultSet.getInt("employee_id");
                Title title = Title.builder()
                        .employeesId(employeeId)  // Set the employee associated with this title
                        .title(resultSet.getString("title"))
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();

                listTitles.add(title);
            }
            return listTitles;

        } catch (SQLException sqlException) {
            System.out.println("SQLException: " + sqlException.getMessage());
        }
        return List.of();
    }

    @Override
    public void insertTitle(Title newTitle) {
        String sql = """
                INSERT INTO "titles" (employee_id, title,from_date,to_date)
                VALUES (?,?,?,?)
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newTitle.getEmployeesId());
            preparedStatement.setString(2, newTitle.getTitle());
            preparedStatement.setDate(3, newTitle.getFromDate());
            preparedStatement.setDate(4, newTitle.getToDate());
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Inserted successfully" : "Inserted failed";
            System.out.println(message);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateTitleById(Integer id, Title updateTitle) {
        String sql = """                            
                UPDATE titles SET title=?, from_date=?, to_date=?
                WHERE employee_id=?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, updateTitle.getTitle());
            preparedStatement.setDate(2, updateTitle.getFromDate());
            preparedStatement.setDate(3, updateTitle.getToDate());
            preparedStatement.setLong(4, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Updated Successfully" : "Update failed";
            System.out.println(message);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void deleteTitleById(Integer id) {
        String sql = """
                DELETE FROM titles WHERE employee_id=?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Deleted Successfully" : "Deleted failed";
            System.out.println(message);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public Title searchTitleById(Integer id) {
        String sql = """
                SELECT 
                    e.id AS employee_id,
                    e.first_name,
                    e.last_name,
                    t.title,
                    t.from_date,
                    t.to_date
                FROM 
                    titles t
                INNER JOIN 
                    employees e ON t.employee_id = e.id
                WHERE 
                    e.id = ?
                """;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Title title = null;
            while (resultSet.next()) {
                 title = Title.builder()
                        .employeesId(resultSet.getInt("employee_id"))
                        .title(resultSet.getString("title"))
                        .fromDate(resultSet.getDate("from_date"))
                        .toDate(resultSet.getDate("to_date"))
                        .build();
            }

            return title;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public List<Object[]> filterTitleByName(String name) {
        String sql = """              
                SELECT t.*, e.first_name, e.last_name
                FROM "titles" t
                JOIN "employees" e ON t.employee_id = e.id
                WHERE t.title LIKE ?
                """;

        List<Object[]> results = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
//            List<Title> listTitles = new ArrayList<>();
//            Title title = null;
            while (resultSet.next()) {
                Title title = new Title(
                        resultSet.getInt("employee_id"),
                        resultSet.getString("title"),
                        resultSet.getDate("from_date"),
                        resultSet.getDate("to_date")
                );
                String employeeName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                results.add(new Object[]{title, employeeName});
            }

            return results;
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public void updateOnlyTitleById(Integer id,String newTitle) {
        String sql = """
                UPDATE "titles"
                SET title = ?
                WHERE employee_id=?
                """;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,newTitle);
            preparedStatement.setInt(2, id);
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "Updated Successfully" : "Update failed";
            System.out.println(message);
        } catch (SQLException sqlException){
            System.out.println(sqlException.getMessage());
        }
    }

    @Override
    public void updateOnlyFromDateById(Integer id, Date newFromDate) {
        String sql = """
            UPDATE "titles"
            SET from_date = ?
            WHERE employee_id = ?
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, newFromDate); // Set the DATE value
            preparedStatement.setInt(2, id);          // Set the employee_id

            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "✅ Date updated successfully." : "❌ Update failed.";
            System.out.println(message);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void updateOnlyToDateById(Integer id, Date newToDate) {
        String sql = """
            UPDATE "titles"
            SET to_date = ?
            WHERE employee_id = ?
            """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDate(1, newToDate); // Set the DATE value
            preparedStatement.setInt(2, id);          // Set the employee_id
            int rowsAffected = preparedStatement.executeUpdate();
            String message = rowsAffected > 0 ? "✅To Date updated successfully." : "❌ Update failed.";
            System.out.println(message);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
