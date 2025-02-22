package co.stad.ems.service.employee;

import co.stad.ems.domain.Employees;
import java.util.List;

public interface IEmployeeInfo {
    void showMenu();
    void addEmployee(Employees employee);
    void updateEmployee(int id, Employees updatedEmployee);
    void deleteEmployee(int id);
    List<Employees> listEmployees();
    Employees searchEmployee(int id);
}
