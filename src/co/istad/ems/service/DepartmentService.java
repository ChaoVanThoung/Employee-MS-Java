package co.istad.ems.service;

import co.istad.ems.domain.Department;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface DepartmentService {
    void addDepartment();
//    void getAllDepartments();
    Pagination<List<Department>> getAllDepartments(int page, int size);
    Department findDepartmentById();
    void deleteDepartmentById();
    void updateDepartmentById();
}
