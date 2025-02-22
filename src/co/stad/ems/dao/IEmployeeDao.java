package co.stad.ems.dao;

import co.stad.ems.domain.Employees;

import java.sql.SQLException;
import java.util.List;

public interface IEmployeeDao {
    void addEmployee(Employees employee);
    void updateEmployee(int id, Employees updatedEmployee);
    void deleteEmployee(int id);
    Employees getEmployeeById(int id);
    List<Employees> getAllEmployees();
}
