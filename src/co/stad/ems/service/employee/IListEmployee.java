package co.stad.ems.service.employee;

public interface IListEmployee {
    void listEmployeesByJobTitle(String jobTitle);
    void listEmployeesBySalaryRange(double startSalary, double endSalary);
    void listAllEmployees();
}
