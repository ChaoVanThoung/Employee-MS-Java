package co.stad.ems.service;

import co.stad.ems.dao.EmployeeDaoImpl;
import co.stad.ems.domain.Employees;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();

    @Override
    public Employees findById(int id) {
        return employeeDaoImpl.searchById(id);
    }
}
