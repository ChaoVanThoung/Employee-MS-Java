package co.istad.ems.service;

import co.istad.ems.domain.Employees;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface EmployeeService {
    void addEmployee();
    void updateEmployee();
    void deleteEmployee();
    Pagination<List<Employees>> getAllEmployees(int page, int size);
    Employees findById();
}
