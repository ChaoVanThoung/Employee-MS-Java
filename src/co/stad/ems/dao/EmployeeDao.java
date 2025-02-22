package co.stad.ems.dao;

import co.stad.ems.domain.Employees;

public interface EmployeeDao {
    Employees searchById(int employeeId);
}
