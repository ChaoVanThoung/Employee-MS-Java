package co.stad.ems.service.employee;

import co.stad.ems.domain.DepartmentManager;
import co.stad.ems.domain.Employees;

import java.util.List;
import java.util.Scanner;

public interface IDepartmentManager {
    void insertDepartmentManager(DepartmentManager manager);
    void deleteDepartmentManager(int id);
    DepartmentManager getDepartmentManagerById(int id);
    List<DepartmentManager> getAllDepartmentManagers();
}
