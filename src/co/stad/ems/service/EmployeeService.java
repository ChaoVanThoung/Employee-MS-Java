package co.stad.ems.service;

import co.stad.ems.domain.Employees;

public interface EmployeeService {
    Employees findById(int id);
}
