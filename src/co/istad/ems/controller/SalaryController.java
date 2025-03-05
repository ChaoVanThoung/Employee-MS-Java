package co.istad.ems.controller;

import co.istad.ems.domain.Salary;
import co.istad.ems.service.SalaryService;
import co.istad.ems.service.SalaryServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class SalaryController {
    private final SalaryService salaryService = new SalaryServiceImpl();

    public void addNewSalary(){
        salaryService.addSalary();
    }
    public Pagination<List<Salary>> getAllSalary(){
        return salaryService.getSalaries(1,10);
    }
    public void updateSalary(){
        salaryService.updateSalary();
    }
    public void deleteSalary(){
        salaryService.deleteSalary();
    }
    public void filterSalary(){
        salaryService.filterSalary();
    }
}
