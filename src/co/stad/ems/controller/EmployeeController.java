package co.stad.ems.controller;

import co.stad.ems.domain.Employees;
import co.stad.ems.service.EmployeeService;
import co.stad.ems.service.EmployeeServiceImpl;

public class EmployeeController {
    private EmployeeService employeeService = new EmployeeServiceImpl();

    public Employees FindEmployeeById(int id){
        return employeeService.findById(id);
    }
}
