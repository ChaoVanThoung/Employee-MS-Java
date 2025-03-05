package co.istad.ems.dao;

import co.istad.ems.domain.DepartmentEmployee;

import java.util.List;

public interface DepartmentEmployeeDao {
    List<DepartmentEmployee> queryDepartmentEmployee();
    void insertDepartmentEmployee(DepartmentEmployee newDepartmentEmployee);
    DepartmentEmployee searchByEmployeeId(int employeeId);
    List<DepartmentEmployee> searchByDepartmentId(String departmentId);
    void updateByEmployeeId(int employeeId,DepartmentEmployee updateDepartmentEmployee);
    void deleteByEmployeeId(int employeeId);
}
