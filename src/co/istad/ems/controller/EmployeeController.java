package co.istad.ems.controller;

import co.istad.ems.domain.Employees;
import co.istad.ems.service.EmployeeService;
import co.istad.ems.service.EmployeeServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class EmployeeController {
    private EmployeeService employeeService = new EmployeeServiceImpl();

    public Employees FindEmployeeById(){
        return employeeService.findById();
    }

    public void updateEmployee(){
        employeeService.updateEmployee();
    }

    public void deleteEmployee(){
        employeeService.deleteEmployee();
    }

    public void addNewEmployee(){
        employeeService.addEmployee();
    }

    public Pagination<List<Employees>> getAllEmployees(){
        return employeeService.getAllEmployees(1,10);
    }
}
