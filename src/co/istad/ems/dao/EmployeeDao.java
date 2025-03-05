package co.istad.ems.dao;

import co.istad.ems.domain.Employees;

import java.util.List;

public interface EmployeeDao {
    void addEmployee(Employees employee);
    void updateEmployee(int id, Employees updatedEmployee);
    void deleteEmployee(int id);
    List<Employees> getAllEmployees();
    Employees searchById(int employeeId);
}
