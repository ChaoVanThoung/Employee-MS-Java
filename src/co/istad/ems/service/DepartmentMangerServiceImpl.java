package co.istad.ems.service;

import co.istad.ems.dao.*;
import co.istad.ems.domain.Department;
import co.istad.ems.domain.DepartmentManager;
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

public class DepartmentMangerServiceImpl implements DepartmentManagerService{

    private final DepartmentMangerDao departmentMangerDaoImpl = new DepartmentMangerDaoImpl();
    private final EmployeeDao employeeDaoImpl = new EmployeeDaoImpl();
    private final DepartmentDao departmentDaoImpl = new DepartmentDaoImpl();

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    Scanner scanner = new Scanner(System.in);

    public Date inputValidDate(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            // Allow blank input for a null date (e.g., ongoing period)
            if (input.isEmpty()) {
                System.out.println("can not null");
                System.out.print("Please Inout: ");
                continue;
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
    public Pagination<List<DepartmentManager>> getAllDepartmentsManager(int page, int size)     {

        System.out.println("""
                \u001B[34m╔════════════════════════════════════════════════════╗
                ║             \u001B[36m📋 View Department Manager\u001B[34m             ║
                ╚════════════════════════════════════════════════════╝\u001B[0m""");
        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Emp_ID", "Emp_Name", "Gender", "Dept_ID", "Dept_Name", "from Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 40);
        }

        List<DepartmentManager> allDepartmentManager = departmentMangerDaoImpl.queryDepartmentEmployee();
        allDepartmentManager.sort(Comparator.comparing(DepartmentManager::getEmployeeId));
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1
        Pagination<List<DepartmentManager>> data = new Pagination<>();
        int totalRecords = allDepartmentManager.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        if (page > totalPages) page = totalPages;
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);

        List<DepartmentManager> paginatedDepartmentManager= allDepartmentManager.subList(fromIndex, toIndex);
        for (DepartmentManager departmentManager : paginatedDepartmentManager) {
            Employees employees = employeeDaoImpl.searchById(departmentManager.getEmployeeId());
            Department department = departmentDaoImpl.searchDepartmentById(departmentManager.getDepartmentId());
            table.addCell(String.valueOf(departmentManager.getEmployeeId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employees.getFirstName() + " " + employees.getLastName(),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(employees.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentManager.getDepartmentId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentManager.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(departmentManager.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        }
        System.out.println(table.render());
        data.setData(paginatedDepartmentManager);
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
                getAllDepartmentsManager(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getAllDepartmentsManager(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getAllDepartmentsManager(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getAllDepartmentsManager(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getAllDepartmentsManager(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getAllDepartmentsManager(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getAllDepartmentsManager(page, size);
                }
            }
        }

        return data;
    }

    @Override
    public void addDepartmentManager() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║     \u001B[33m1.➕ Add Department Manager\u001B[34m      ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        DepartmentManager newDepartmentManager = new DepartmentManager();
        while (true) {
            System.out.print("Input Employee Id (or 'e' to quit): ");
            String input = scanner.nextLine().trim(); // Capture input as string
            if (input.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting process..." + RESET);
                return; // Exit the method
            }
            try {
                int employeeId = Integer.parseInt(input); // Parse after checking exit
                Employees employees = employeeDaoImpl.searchById(employeeId);
                if (employees == null) {
                    System.out.println("Employee not found");
                    continue;
                }
                DepartmentManager departmentManager = departmentMangerDaoImpl.searchByEmployeeId(employeeId);
                if (departmentManager != null) {
                    System.out.println("Department Manager already exists");
                    continue;
                }
                newDepartmentManager.setEmployeeId(employeeId);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input must be a number or 'e'");
            }
        }
        scanner.nextLine();
        while (true) {
            System.out.print("Input Department Id: ");
            String departmentId = scanner.nextLine().trim();
            Department department = departmentDaoImpl.searchDepartmentById(departmentId);
            if (department == null) {
                System.out.println("❌ Department not found. Try again.");
                continue;
            }
            if (departmentMangerDaoImpl.searchByDepartmentId(departmentId) != null) {
                System.out.println("❌ This Department already has a Manager.");
                continue;
            }
            newDepartmentManager.setDepartmentId(departmentId);
            break;
        }
        System.out.print("Input From Date (YYYY-MM-DD): ");
        Date fromDate = inputValidDate(scanner);
        newDepartmentManager.setFromDate(fromDate);
        System.out.print("Input To Date (YYYY-MM-DD): ");
        Date toDate = inputValidDate(scanner);
        newDepartmentManager.setToDate(toDate);

        departmentMangerDaoImpl.insertDepartmentManager(newDepartmentManager);
    }

    @Override
    public void searchByDepartmentId() {
        System.out.println("""
                \u001B[34m╔═══════════════════════════════════════════╗
                ║     \u001B[36m🔎 Search Department(ID) Manager \u001B[34m     ║
                ╚═══════════════════════════════════════════╝\u001B[0m""");
        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Emp_ID", "Emp_Name", "Gender", "Dept_ID", "Dept_Name", "from Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 40);
        }
        System.out.print("Enter Department ID: ");
        String departmentId = scanner.nextLine();
        DepartmentManager departmentManager = departmentMangerDaoImpl.searchByDepartmentId(departmentId);
        if (departmentManager == null) {
            System.out.println("Department Manager not found");
            return;
        }
        Employees employee = employeeDaoImpl.searchById(departmentManager.getEmployeeId());
        Department department = departmentDaoImpl.searchDepartmentById(departmentManager.getDepartmentId());
        table.addCell(String.valueOf(departmentManager.getEmployeeId()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employee.getFirstName() + " " + employee.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(employee.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(departmentManager.getDepartmentId(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(departmentManager.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(departmentManager.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));

        System.out.println(table.render());
    }

    @Override
    public void deletedDepartmentManager() {
        int id;
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║     \u001B[36m♻️ Remove Department Manager\u001B[34m     ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID (or 'e' to quit): ");
            String input = scanner.nextLine().trim();
            System.out.println("DEBUG: Input received: '" + input + "'");
            if (input.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting remove process..." + RESET);
                return;
            }
            try {
                id = Integer.parseInt(input);
                System.out.println("DEBUG: Parsed ID: " + id);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input Only Number");
            }
        }
        Employees employees = employeeDaoImpl.searchById(id);
        if (employees != null) {
            departmentMangerDaoImpl.deleteByEmployeeId(id);
        } else {
            System.out.println("Employee not found with ID: " + id);
        }
    }

    @Override
    public void updateDepartmentManager() {
        int id;
        DepartmentManager updatreDepartmentManager = new DepartmentManager();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════════════════════╗
                ║             \u001B[36m🔄 Update Department Manager\u001B[34m             ║
                ╚══════════════════════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID (or 'e' to quit): ");
            String input = scanner.nextLine().trim();
            System.out.println("DEBUG: Employee ID input: '" + input + "'");
            if (input.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting update process..." + RESET);
                return;
            }
            try {
                id = Integer.parseInt(input);
                System.out.println("DEBUG: Parsed ID: " + id);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input Only Number");
            }
        }
        DepartmentManager departmentManager = departmentMangerDaoImpl.searchByEmployeeId(id);
        if (departmentManager == null) {
            System.out.println("Employee not found with ID: " + id);
            return;
        }
        scanner.nextLine();
        while (true) {
            System.out.print("Input Department Id: ");
            String departmentId = scanner.nextLine().trim();
            Department department = departmentDaoImpl.searchDepartmentById(departmentId);
            if (department != null) {
                updatreDepartmentManager.setDepartmentId(departmentId);
                System.out.print("Input From Date (YYYY-MM-DD): ");
                Date fromDate = inputValidDate(scanner); // Use the helper method
                updatreDepartmentManager.setFromDate(fromDate);
                System.out.print("Input To Date (YYYY-MM-DD): ");
                Date toDate = inputValidDate(scanner);
                updatreDepartmentManager.setToDate(toDate);
                departmentMangerDaoImpl.updateByEmployeeId(id, updatreDepartmentManager);
                break;
            } else {
                System.out.println("Department not found with ID: " + departmentId);
            }
        }
    }

}

