package co.istad.ems.controller;

import co.istad.ems.domain.DepartmentEmployee;
import co.istad.ems.service.DepartmentEmployeeService;
import co.istad.ems.service.DepartmentEmployeeServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class DepartmentEmployeeController {
    private final DepartmentEmployeeService departmentEmployeeService = new DepartmentEmployeeServiceImpl();

    public void addNew(){
        departmentEmployeeService.addDepartmentEmployee();
    }
//    public void getAll(){
//        departmentEmployeeService.getAllDepartmentEmployees();
//    }
    public Pagination<List<DepartmentEmployee>> getAll(){
        return departmentEmployeeService.getAllDepartmentEmployees(1,10);
    }
    public void findByEmpId(){
        departmentEmployeeService.searchByEmployeeId();
    }
    public void findByDeptId(){
        departmentEmployeeService.searchByDepartmentId();
    }
    public void deleteById(){
        departmentEmployeeService.deleteDepartmentEmployee();
    }
    public void updateById(){
        departmentEmployeeService.updateDepartmentEmployee();
    }
}
