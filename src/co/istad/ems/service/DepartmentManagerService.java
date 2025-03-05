package co.istad.ems.service;

import co.istad.ems.domain.DepartmentManager;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface DepartmentManagerService {
    Pagination<List<DepartmentManager>> getAllDepartmentsManager(int page, int size);
    void addDepartmentManager();
    void searchByDepartmentId();
    void deletedDepartmentManager();
    void updateDepartmentManager();
}
