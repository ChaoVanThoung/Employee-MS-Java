package co.stad.ems.controller;

import co.stad.ems.dao.DepartmentDao;
import co.stad.ems.dao.DepartmentDaoImpl;
import co.stad.ems.domain.Department;
import co.stad.ems.service.DepartmentService;
import co.stad.ems.service.DepartmentServiceImpl;

import java.util.List;

public class DepartmentController {
    private final DepartmentService departmentService = new DepartmentServiceImpl();
    private final DepartmentDao departmentDao = new DepartmentDaoImpl();

    public void addNewDepartment(Department department) {
        departmentService.addDepartment(department);
    }

    public List<Department> getAllDepartments() {
       return departmentService.getAllDepartments();
    }

    public Department findById(String id) {
        return departmentService.findDepartmentById(id);
    }

    public void deleteDepartment(String id){
        departmentService.deleteDepartmentById(id);
    }
    public void updateDepartment(String id, Department department) {
        departmentService.updateDepartmentById(id, department);
    }
}
