package co.stad.ems.controller;

import co.stad.ems.domain.Employees;
import co.stad.ems.service.employee.EmployeeImpl;
import co.stad.ems.service.employee.IEmployeeInfo;

import java.util.List;

public class EmployeeController {
    private final IEmployeeInfo employeeService = new EmployeeImpl(); // Use the service layer

    public void addEmployee(Employees employee) {
        employeeService.addEmployee(employee);
    }

    public void updateEmployee(int id, Employees updatedEmployee) {
        employeeService.updateEmployee(id, updatedEmployee);
    }

    public void deleteEmployee(int id) {
        employeeService.deleteEmployee(id);
    }

    public List<Employees> listEmployees() {
        return employeeService.listEmployees();
    }

    public Employees searchEmployee(int id) {
        return employeeService.searchEmployee(id);
    }
}
