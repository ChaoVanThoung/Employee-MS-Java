package co.istad.ems.service;

import co.istad.ems.domain.Salary;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface SalaryService {
    void addSalary();
//    void getAllSalaries();
    Pagination<List<Salary>> getSalaries(int page, int size);
    void updateSalary();
    void deleteSalary();
    void filterSalary();
}
