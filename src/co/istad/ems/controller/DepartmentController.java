package co.istad.ems.controller;

import co.istad.ems.dao.DepartmentDao;
import co.istad.ems.dao.DepartmentDaoImpl;
import co.istad.ems.domain.Department;
import co.istad.ems.service.DepartmentService;
import co.istad.ems.service.DepartmentServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class DepartmentController {
    private final DepartmentService departmentService = new DepartmentServiceImpl();
    private final DepartmentDao departmentDao = new DepartmentDaoImpl();

    public void addNewDepartment() {
        departmentService.addDepartment();
    }

    public Pagination<List<Department>> getAllDepartments() {
       return departmentService.getAllDepartments(1,5);
    }

    public void findById() {
        departmentService.findDepartmentById();
    }

    public void deleteDepartment(){
        departmentService.deleteDepartmentById();
    }
    public void updateDepartment() {
        departmentService.updateDepartmentById();
    }
}
