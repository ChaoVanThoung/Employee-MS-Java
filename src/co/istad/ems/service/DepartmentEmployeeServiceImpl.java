package co.istad.ems.service;

import co.istad.ems.dao.*;
import co.istad.ems.domain.Department;
import co.istad.ems.domain.DepartmentEmployee;
import co.istad.ems.domain.Employees;
import co.istad.ems.util.Pagination;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DepartmentEmployeeServiceImpl implements DepartmentEmployeeService {

    private final EmployeeDao employeeDaoImpl = new EmployeeDaoImpl();
    private final DepartmentDao departmentDaoImpl = new DepartmentDaoImpl();
    private final DepartmentEmployeeDao departmentEmployeeDaoImpl = new DepartmentEmployeeDaoImpl();

    Scanner scanner = new Scanner(System.in);

    int id;

    public Date inputValidDate(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            // Allow blank input for a null date (e.g., ongoing period)
            if (input.isEmpty()) {
                return null;
            }
            try {
                // Parse and return a valid date
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
                System.out.print("Please Input Again: ");
            }
        }
    }

    @Override
    public void addDepartmentEmployee() {
        System.out.println("""
                \u001B[34m╔═════════════════════════════════════════╗
                ║  \u001B[33m\u2795 Add New Employee For Department\u001B[34m     ║
                ╚═════════════════════════════════════════╝\u001B[0m""");
        DepartmentEmployee newDepartmentEmployee = new DepartmentEmployee();
        while (true) {
            try {
                System.out.print("Input Employee Id: ");
                int employeeId = scanner.nextInt();
                Employees employees = employeeDaoImpl.searchById(employeeId);
                if (employees != null) {
                    newDepartmentEmployee.setEmployeeId(employeeId);
                    break;
                } else {
                    System.out.println("Employee not found");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.nextLine();
        while (true) {
            System.out.print("Input Department Id: ");
            String departmentId = scanner.nextLine().trim();
            Department department = departmentDaoImpl.searchDepartmentById(departmentId);
            if (department != null) {
                newDepartmentEmployee.setDepartmentId(departmentId);
                break;
            } else {
                System.out.println("Department not found");
            }
        }
        System.out.print("Input From Date (YYYY-MM-DD): ");
        Date fromDate = inputValidDate(scanner);
        newDepartmentEmployee.setFromDate(fromDate);
        System.out.print("Input To Date (YYYY-MM-DD): ");
        Date toDate = inputValidDate(scanner);
        newDepartmentEmployee.setToDate(toDate);

        departmentEmployeeDaoImpl.insertDepartmentEmployee(newDepartmentEmployee);
    }

    @Override
    public Pagination<List<DepartmentEmployee>> getAllDepartmentEmployees(int page, int size) {
        System.out.println("""
                \u001B[34m╔════════════════════════════════════════════╗
                ║      \u001B[33m\uD83D\uDCCB Show All Department Employee\u001B[34m       ║
                ╚════════════════════════════════════════════╝\u001B[0m""");

        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Emp_ID", "Emp_Name", "Gender", "Dept_ID", "Dept_Name", "from Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 40);
        }

        List<DepartmentEmployee> allDepartmentEmployee = departmentEmployeeDaoImpl.queryDepartmentEmployee();
        allDepartmentEmployee.sort(Comparator.comparing(DepartmentEmployee::getEmployeeId));

        // Ensure valid page and size
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1

        Pagination<List<DepartmentEmployee>> data = new Pagination<>();
        int totalRecords = allDepartmentEmployee.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        if (page > totalPages) page = totalPages;
        // Calculate pagination indices
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);
        List<DepartmentEmployee> paginatedDepartmentEmployee= allDepartmentEmployee.subList(fromIndex, toIndex);
        for (DepartmentEmployee departmentEmployee : paginatedDepartmentEmployee) {
            Employees employees = employeeDaoImpl.searchById(departmentEmployee.getEmployeeId());
            Department department = departmentDaoImpl.searchDepartmentById(departmentEmployee.getDepartmentId());
            table.addCell(String.valueOf(departmentEmployee.getEmployeeId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employees.getFirstName() + " " + employees.getLastName(),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(employees.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentEmployee.getDepartmentId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentEmployee.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentEmployee.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        }
        System.out.println(table.render());
        data.setData(paginatedDepartmentEmployee);
        data.setTotalRecord(totalRecords);
        data.setTotalPages(totalPages);
        data.setCurrentPage(page);

        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("Page " + page + " of " + totalPages);
        System.out.println("(N) Next Page");
        System.out.println("(P) Previous Page");
        System.out.println("(E) Exit");
        System.out.print("> Paginate (Enter page number or option): ");

        String option = scanner.nextLine();

        if (option.matches("\\d+")) {
            int requestedPage = Integer.parseInt(option);
            if (requestedPage >= 1 && requestedPage <= totalPages) {
                getAllDepartmentEmployees(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getAllDepartmentEmployees(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getAllDepartmentEmployees(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getAllDepartmentEmployees(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getAllDepartmentEmployees(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getAllDepartmentEmployees(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getAllDepartmentEmployees(page, size);
                }
            }
        }

        return data;
    }



    @Override
    public void searchByEmployeeId() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[33m \uD83D\uDD0D Search Employee\u001B[34m          ║
                ╚══════════════════════════════════════╝\u001B[0m""");

        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Emp_ID", "Emp_Name", "Gender", "Dept_ID", "Dept_Name", "from Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 40);
        }

        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
                scanner.nextLine();
            }
        }
        Employees employees = employeeDaoImpl.searchById(id);
        if (employees == null) {
            System.out.println("Employee not found");
            return;
        }
        DepartmentEmployee record = departmentEmployeeDaoImpl.searchByEmployeeId(id);
        if (record == null) {
            System.out.println("No department record found for this employee.");
            return;
        }

        Department department = departmentDaoImpl.searchDepartmentById(record.getDepartmentId());
        if (department == null) {
            System.out.println("Department not found.");
            return;
        }

        table.addCell(String.valueOf(record.getEmployeeId()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employees.getFirstName() + " " + employees.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(employees.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(record.getDepartmentId(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(record.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(record.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        System.out.println(table.render());
    }

    @Override
    public void searchByDepartmentId() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║          \u001B[33m \uD83D\uDCC2 Search Department\u001B[34m       ║
                ╚══════════════════════════════════════╝\u001B[0m""");

        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Emp_ID", "Emp_Name", "Gender", "Dept_ID", "Dept_Name", "from Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 40);
        }

        System.out.print("Enter Department ID: ");
        String departmentId = scanner.nextLine();

        List<DepartmentEmployee> records = departmentEmployeeDaoImpl.searchByDepartmentId(departmentId);

        if (records.isEmpty()) {
            System.out.println("❌ No employees found for this department.");
            return;
        }
        for (DepartmentEmployee record : records) {
            Employees employee = employeeDaoImpl.searchById(record.getEmployeeId());
            Department department = departmentDaoImpl.searchDepartmentById(record.getDepartmentId());

            if (employee == null || department == null) {
                continue; // Skip if employee or department not found
            }

            // Populate the table with data
            table.addCell(String.valueOf(record.getEmployeeId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getFirstName() + " " + employee.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(employee.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(record.getDepartmentId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(record.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(record.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        }

        // Display the table
        System.out.println(table.render());


    }

    @Override
    public void deleteDepartmentEmployee() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║      \u001B[33m\u274C Delete By Employee ID\u001B[34m        ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
                scanner.nextLine();
            }
        }
        Employees employees = employeeDaoImpl.searchById(id);
        if (employees != null) {
            departmentEmployeeDaoImpl.deleteByEmployeeId(id);
        } else {
            System.out.println("Employee not found with ID: " + id);
        }


    }

    @Override
    public void updateDepartmentEmployee() {
        DepartmentEmployee updateDepartmentEmployee = new DepartmentEmployee();
        System.out.println("""
                \u001B[34m╔════════════════════════════════════════╗
                ║   \u001B[33m\uD83D\uDD04 Update Employee For Department\u001B[34m    ║
                ╚════════════════════════════════════════╝\u001B[0m""");

        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Employees employees = employeeDaoImpl.searchById(id);
        if (employees != null) {
            updateDepartmentEmployee.setEmployeeId(id);
        } else {
            System.out.println("Employee not found with ID: " + id);
        }
        scanner.nextLine();
        while (true) {
            System.out.print("Input Department Id: ");
            String departmentId = scanner.nextLine().trim();
            Department department = departmentDaoImpl.searchDepartmentById(departmentId);
            if (department != null) {
                updateDepartmentEmployee.setDepartmentId(departmentId);
                System.out.print("Input From Date (YYYY-MM-DD): ");
                Date fromDate = inputValidDate(scanner); // Use the helper method
                updateDepartmentEmployee.setFromDate(fromDate);
                System.out.print("Input To Date (YYYY-MM-DD): ");
                Date toDate = inputValidDate(scanner);
                updateDepartmentEmployee.setToDate(toDate);
                departmentEmployeeDaoImpl.updateByEmployeeId(id,updateDepartmentEmployee);
                break;
            } else {
                System.out.println("Department not found");
            }

        }
    }
}
