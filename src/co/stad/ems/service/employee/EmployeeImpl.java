package co.stad.ems.service.employee;

import co.stad.ems.dao.EmployeeDaoImpl;
import co.stad.ems.dao.IEmployeeDao;
import co.stad.ems.domain.Employees;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeImpl implements IEmployeeInfo {
    private IEmployeeDao employeeDao = new EmployeeDaoImpl();

    public void showMenu() {
        System.out.println("""
    ╔════════════════════════════════════════════════════════════════╗
    ║                         Manage Employees                       ║
    ╠════════════════════════════════════════════════════════════════╣
    ║       1.➕ Add Employee                                        ║
    ║       2.📋 View Employee List                                  ║
    ║       3.🔎 Search Employee                                     ║
    ║       4.📈 Update Employee                                     ║
    ║       5. 🗑 Delete Employee                                     ║
    ║       0.◀️ Exit                                                ║
    ╚════════════════════════════════════════════════════════════════╝
    """);
    }

    public void addEmployee(Employees employee) {
        if (employee.getFirstName() == null || employee.getLastName() == null) {
            System.out.println("\u001B[31mError: First name or last name cannot be null.\u001B[0m");
            return;
        }
        employeeDao.addEmployee(employee);
        System.out.println("\u001B[32m[+] Employee added successfully!\u001B[0m");
    }

    @Override
    public void updateEmployee(int id, Employees updatedEmployee) {
        if (employeeDao.getEmployeeById(id) == null) {
            System.out.println("Employee not found!");
            return;
        }
        employeeDao.updateEmployee(id, updatedEmployee);
        System.out.println("Employee updated successfully!");
    }

    @Override
    public void deleteEmployee(int id) {
        if (employeeDao.getEmployeeById(id) == null) {
            System.out.println("Employee not found!");
            return;
        }
        employeeDao.deleteEmployee(id);
        System.out.println("Employee deleted successfully!");
    }

    @Override
    public List<Employees> listEmployees() {
        return employeeDao.getAllEmployees();
    }

    @Override
    public Employees searchEmployee(int id) {
        Employees employee = employeeDao.getEmployeeById(id);
        if (employee == null) {
            System.out.println("Employee not found!");
        }
        return employee;
    }
}
