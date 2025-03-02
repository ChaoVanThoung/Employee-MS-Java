package co.stad.ems.service.employee;

import co.stad.ems.dao.DepartmentManagerDaoImpl;
import co.stad.ems.dao.IDepartmentManagerDao;
import co.stad.ems.domain.DepartmentManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class DepartmentManagerImpl implements IDepartmentManager {
    private final IDepartmentManagerDao managerDao = new DepartmentManagerDaoImpl();

    @Override
    public void insertDepartmentManager(DepartmentManager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("Department Manager cannot be null.");
        }
        managerDao.insertDepartmentManager(manager);
    }

    @Override
    public void deleteDepartmentManager(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid Manager ID.");
        }
        managerDao.deleteDepartmentManager(id);
    }

    @Override
    public List<DepartmentManager> getAllDepartmentManagers() {
        List<DepartmentManager> managers = managerDao.getAllDepartmentManagers();
        return (managers.isEmpty()) ? Collections.emptyList() : managers;
    }

    @Override
    public DepartmentManager getDepartmentManagerById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID provided.");
        }
        return managerDao.getDepartmentManagerById(id);
    }
}
