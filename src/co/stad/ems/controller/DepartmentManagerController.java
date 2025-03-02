package co.stad.ems.controller;

import co.stad.ems.service.employee.IDepartmentManager;
import co.stad.ems.domain.DepartmentManager;
import java.util.List;

public class DepartmentManagerController {
    private final IDepartmentManager departmentManager;

    // Constructor injection
    public DepartmentManagerController(IDepartmentManager departmentManager) {
        this.departmentManager = departmentManager;
    }

    public void insertDepartmentManager(DepartmentManager dm) {
        departmentManager.insertDepartmentManager(dm);
    }

    public void removeDepartmentManager(int employeeId) {
        departmentManager.deleteDepartmentManager(employeeId);
    }

    public DepartmentManager getDepartmentManager(int employeeId) {
        return departmentManager.getDepartmentManagerById(employeeId);
    }

    public List<DepartmentManager> getAllDepartmentManagers() {
        return departmentManager.getAllDepartmentManagers();
    }
}