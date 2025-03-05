package co.istad.ems.dao;

import co.istad.ems.domain.DepartmentEmployee;
import co.istad.ems.domain.DepartmentManager;

import java.util.List;

public interface DepartmentMangerDao {
    List<DepartmentManager> queryDepartmentEmployee();
    void insertDepartmentManager(DepartmentManager newDepartmentManager);
    void updateByEmployeeId(int employeeId,DepartmentManager updateDepartmentManager);
    void deleteByEmployeeId(int employeeId);
    DepartmentManager searchByDepartmentId(String departmentId);
    DepartmentManager searchByEmployeeId(int employeeId);
}
