package co.istad.ems.service;

import co.istad.ems.domain.DepartmentEmployee;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface DepartmentEmployeeService {
    void addDepartmentEmployee();
    Pagination<List<DepartmentEmployee>> getAllDepartmentEmployees(int page, int size);
    void searchByEmployeeId();
    void searchByDepartmentId();
    void deleteDepartmentEmployee();
    void updateDepartmentEmployee();
}
