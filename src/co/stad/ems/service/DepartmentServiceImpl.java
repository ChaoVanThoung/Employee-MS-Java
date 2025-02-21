package co.stad.ems.service;

import co.stad.ems.dao.DepartmentDao;
import co.stad.ems.dao.DepartmentDaoImpl;
import co.stad.ems.domain.Department;

import java.util.List;
import java.util.Scanner;

public class DepartmentServiceImpl implements DepartmentService {

//    Scanner scanner = new Scanner(System.in);
    private final DepartmentDaoImpl departmentDaoImpl = new DepartmentDaoImpl();


    @Override
    public void addDepartment(Department department) {
        if (department.getId() == null || department.getId().length() != 4) {
            throw new IllegalArgumentException("⚠ Department ID must be exactly 4 characters.");
        }
        if (department.getDepartmentName() == null || department.getDepartmentName().length() > 40) {
            throw new IllegalArgumentException("⚠ Department Name must be 40 characters or less.");
        }

    departmentDaoImpl.insertNewDepartment(department);

    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentDaoImpl.queryAllDepartments();
    }

    @Override
    public Department findDepartmentById(String id) {
        return departmentDaoImpl.searchDepartmentById(id);
    }

    @Override
    public void deleteDepartmentById(String id) {
        departmentDaoImpl.deleteDepartmentById(id);
    }


}
