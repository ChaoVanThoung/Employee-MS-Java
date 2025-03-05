package co.istad.ems.dao;

import co.istad.ems.domain.Salary;

import java.util.List;

public interface SalaryDao {
    List<Salary> queryAllSalaries();
    int count();
    void insertSalary(Salary newSalary);
    void updateSalaryById(Integer id,Salary updateSalary);
    void deleteSalaryById(Integer id);
    List<Object[]> searchSalary(Integer id);
}
