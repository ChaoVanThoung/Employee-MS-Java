package co.istad.ems.controller;

import co.istad.ems.domain.DepartmentManager;
import co.istad.ems.service.DepartmentManagerService;
import co.istad.ems.service.DepartmentMangerServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class DepartmentMangerController {
    private DepartmentManagerService departmentManagerService = new DepartmentMangerServiceImpl();

    public Pagination<List<DepartmentManager>> getAllDepartmentsManager() {
        return departmentManagerService.getAllDepartmentsManager(1,5);
    }

    public void addNewDepartmentManager() {
        departmentManagerService.addDepartmentManager();
    }
    public void searchByDepartmentId() {
        departmentManagerService.searchByDepartmentId();
    }
    public void deleteDepartmentManager() {
        departmentManagerService.deletedDepartmentManager();
    }
    public void updateDepartmentManager() {
        departmentManagerService.updateDepartmentManager();
    }
}
