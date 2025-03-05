package co.istad.ems.dao;

import co.istad.ems.domain.Department;

import java.util.List;

public interface DepartmentDao {
    List<Department> queryAllDepartments();
    void insertNewDepartment(Department department);
    void updateDepartmentById(String id,Department department);
    void deleteDepartmentById(String id);
    Department searchDepartmentById(String id);
}
