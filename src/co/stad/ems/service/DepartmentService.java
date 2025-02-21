package co.stad.ems.service;

import co.stad.ems.domain.Department;

import java.util.List;

public interface DepartmentService {
    void addDepartment(Department department);
    List<Department> getAllDepartments();
    Department findDepartmentById(String id);
    void deleteDepartmentById(String id);
}
