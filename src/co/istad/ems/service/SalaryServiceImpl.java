package co.istad.ems.service;

import co.istad.ems.dao.EmployeeDao;
import co.istad.ems.dao.EmployeeDaoImpl;
import co.istad.ems.dao.SalaryDao;
import co.istad.ems.dao.SalaryDaoImpl;
import co.istad.ems.domain.Employees;
import co.istad.ems.domain.Salary;
import co.istad.ems.util.Pagination;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.util.*;

public class SalaryServiceImpl implements SalaryService {
    Scanner scanner = new Scanner(System.in);
    private final EmployeeDao employeeDaoImpl = new EmployeeDaoImpl();
    private final SalaryDao salaryDaoImpl = new SalaryDaoImpl();
    Integer id;


    @Override
    public void addSalary() {
        Salary newSalary = new Salary();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║     \u001B[36mInsert Salary Employee By Id\u001B[34m     ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        System.out.print("Please Input Employee ID: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric Employee ID.");
            scanner.next(); // Consume invalid input
        }
        id = scanner.nextInt();
        scanner.nextLine();
        Employees employeesId = employeeDaoImpl.searchById(id);
        if (employeesId == null) {
            System.out.println("Employee not found with ID: " + id);
            return; // Exit if employee is not found
        }
        newSalary.setEmployeesId(employeesId.getId());

        System.out.print("Input From Date (YYYY-MM-DD): ");
        Date fromDate = inputValidDate(scanner); // Use the helper method
        newSalary.setFromDate(fromDate);
        System.out.print("Input To Date (YYYY-MM-DD): ");
        Date toDate = inputValidDate(scanner);
        newSalary.setToDate(toDate);

        System.out.print("Input Amount Salary: ");
        int amount = scanner.nextInt();
        try {
            newSalary.setAmount(amount);
        } catch (MissingFormatWidthException e) {
            System.out.println("Invalid input. Please enter a valid amount.");
        }

        salaryDaoImpl.insertSalary(newSalary);

    }

    @Override
    public Pagination<List<Salary>> getSalaries(int page, int size) {
        System.out.println("""
            [34m╔══════════════════════════════════════╗
            ║          [36mShow All Salary[34m             ║
            ╚══════════════════════════════════════╝[0m""");

        // Set up the table
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String[] columnNames = {"ID", "Name", "Salary", "From Date", "To Date"};

        for (int i = 0; i < columnNames.length; i++) {
            table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 25, 40);
        }

        // Fetch all salaries
        List<Salary> allSalaries = salaryDaoImpl.queryAllSalaries();
        allSalaries.sort(Comparator.comparing(Salary::getEmployeesId));

        // Ensure valid page and size
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1

        Pagination<List<Salary>> data = new Pagination<>();
        int totalRecords = allSalaries.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Handle cases where page exceeds total pages
        if (page > totalPages) page = totalPages;

        // Calculate pagination indices
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);

        // Get paginated data
        List<Salary> paginatedSalaries = allSalaries.subList(fromIndex, toIndex);

        // Populate table with paginated salary data
        for (Salary salary : paginatedSalaries) {
            Employees employee = employeeDaoImpl.searchById(salary.getEmployeesId());
            table.addCell(String.valueOf(salary.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getFirstName() + " " + employee.getLastName(),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getAmount()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        }

        // Display the table
        System.out.println(table.render());

        // Set Pagination Data
        data.setData(paginatedSalaries);
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
                getSalaries(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getSalaries(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getSalaries(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getSalaries(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getSalaries(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getSalaries(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getSalaries(page, size);
                }
            }
        }

        return data;
    }


//    @Override
//    public void getAllSalaries() {
//
//        System.out.println("""
//                \u001B[34m╔══════════════════════════════════════╗
//                ║          \u001B[36mShow All Salary\u001B[34m             ║
//                ╚══════════════════════════════════════╝\u001B[0m""");
//
//        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
//        String columNames[] = {"ID", "Name", "Salary", "From Date", "To Date"};
//        for (int i = 0; i < columNames.length; i++) {
//            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
//            table.setColumnWidth(i, 25, 40);
//        }
//        salaryDaoImpl.queryAllSalaries().forEach(salary -> {
//            Employees employee = employeeDaoImpl.searchById(salary.getEmployeesId());
//            table.addCell(String.valueOf(salary.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(employee.getFirst_name() + " " + employee.getLast_name(),
//                    new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(salary.getAmount()), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(salary.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(salary.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
//        });
//        System.out.println(table.render());
//    }

    @Override
    public void updateSalary() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║      \u001B[36mUpdate Salary By EmpID\u001B[34m          ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        Salary updateSalary = new Salary();
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Employees employeesId = employeeDaoImpl.searchById(id);
        if (employeesId == null) {
            System.out.println("Employee not found with ID: " + id);
        }
        while (true) {
            scanner.nextLine();
            System.out.print("> Please Input From Date (YYYY-MM-DD): ");
            Date fromDate = inputValidDate(scanner); // Use the helper method
            updateSalary.setFromDate(fromDate);
            System.out.print("Input To Date (YYYY-MM-DD): ");
            Date toDate = inputValidDate(scanner);
            updateSalary.setToDate(toDate);
            System.out.print("Input Amount Salary: ");
            try {
                int amount = scanner.nextInt();
                updateSalary.setAmount(amount);
            } catch (MissingFormatWidthException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
            salaryDaoImpl.updateSalaryById(id, updateSalary);
            break;
        }
    }

    @Override
    public void deleteSalary() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[36mDelete Salary By ID\u001B[34m          ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Employees employeeId = employeeDaoImpl.searchById(id);
        if (employeeId == null) {
            System.out.println("Employee not found with ID: " + id);
        } else {
            salaryDaoImpl.deleteSalaryById(id);
        }
    }

    @Override
    public void filterSalary() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[36mFilter By Title\u001B[34m              ║
                ╚══════════════════════════════════════╝\u001B[0m""");

        Integer amount;
        List<Object[]> salaryWithEmployeeList = new ArrayList<>();
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"ID", "Name", "Salary", "From Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 25, 40);
        }
        while (true){
            System.out.print("Please Input Amount Salary: ");
            try{
                 amount = scanner.nextInt();
                break;
            } catch (MissingFormatWidthException e) {
                System.out.println("Invalid input. Please enter a valid amount.");
            }
        }
         salaryWithEmployeeList= salaryDaoImpl.searchSalary(amount);
        if (salaryWithEmployeeList.isEmpty()) {
            System.out.println("No records found for salary amount: " + amount);
            return;
        }
        salaryWithEmployeeList.forEach(record -> {
            Salary salary = (Salary) record[0];
            String employeeName = (String) record[1];
            table.addCell(String.valueOf(salary.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employeeName, new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getAmount()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(salary.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        });
        System.out.println(table.render());

    }

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
}

