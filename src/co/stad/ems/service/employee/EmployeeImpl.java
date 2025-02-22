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
//        System.out.println("=================================| Manage Employees |=================================");
//        System.out.println("1. Add Employee");
//        System.out.println("2. View Employee List");
//        System.out.println("3. Search & Filter Employee");
//        System.out.println("4. Update Employee");
//        System.out.println("5. Delete Employee");
//        System.out.println("0. Exit");
//        System.out.println("=====================================================================================");
//        System.out.println("                                   >> SELECT AN OPTION <<                            ");
//        System.out.println("=====================================================================================");
        System.out.println("""
    \u001B[34m╔══════════════════════════════════════╗
    ║         \u001B[36mManage Employees\u001B[34m           ║
    ╠══════════════════════════════════════╣
    ║       \u001B[33m1. Add Employee\u001B[34m          ║
    ║       \u001B[33m2. View Employee List\u001B[34m           ║
    ║       \u001B[33m3. Search Employee\u001B[34m                ║
    ║       \u001B[33m4. Update Employee\u001B[34m                ║
    ║       \u001B[33m5. Delete Employee\u001B[34m        ║
    ║       \u001B[31m0. Exit\u001B[34m                        ║
    ╚══════════════════════════════════════╝
    \u001B[0m""");
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
