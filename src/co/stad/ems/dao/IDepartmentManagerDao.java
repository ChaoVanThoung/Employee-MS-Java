package co.stad.ems.dao;
import java.util.*;
import co.stad.ems.domain.DepartmentManager;
import co.stad.ems.domain.Employees;

public interface IDepartmentManagerDao {
    void insertDepartmentManager(DepartmentManager manager);
    void deleteDepartmentManager(int id);
    DepartmentManager getDepartmentManagerById(int id);
    List<DepartmentManager> getAllDepartmentManagers();
}
