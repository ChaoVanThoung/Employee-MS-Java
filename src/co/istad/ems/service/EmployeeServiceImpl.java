package co.istad.ems.service;

import co.istad.ems.dao.EmployeeDaoImpl;
import co.istad.ems.domain.Employees;
import co.istad.ems.domain.Salary;
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


public class EmployeeServiceImpl implements EmployeeService {

    Scanner scanner = new Scanner(System.in);

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private final EmployeeDaoImpl employeeDaoImpl = new EmployeeDaoImpl();


    // Helper method to validate date input
    private Date getValidDate(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = scanner.nextLine();
                return Date.valueOf(dateStr);
            } catch (IllegalArgumentException e) {
                System.out.println(YELLOW + "⚠️ Invalid date format! Please use YYYY-MM-DD." + RESET);
            }
        }
    }

    @Override
    public void addEmployee() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[33m\u2795 Add Employee\u001B[34m              ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        String firstName, lastName;
        String idInput;
        String genderInput;
        Date birthDate, hireDate;

        Employees newEmployee = new Employees();
        while (true) {
            System.out.print(BLUE + "Enter employee first name (or '-' to go back): " + RESET);
            firstName = scanner.nextLine();
            if (firstName.equals("-")) {
                System.out.println(YELLOW + "Returning to the main menu..." + RESET);
                return; // Exit method and return to the menu
            }
            if (firstName.isEmpty() || !firstName.matches("^[a-zA-Z]+$") || firstName.length() > 14) {
                System.out.println(YELLOW + "⚠️ Invalid name! First name must contain only letters and be ≤ 14 characters." + RESET);
                continue;
            }
            newEmployee.setFirstName(firstName);
            break;
        }
        while (true) {
            System.out.print(BLUE + "Enter employee last name: " + RESET);
            lastName = scanner.nextLine();
            if (lastName.isEmpty() || !lastName.matches("^[a-zA-Z]+$") || lastName.length() > 16) {
                System.out.println(YELLOW + "⚠️ Invalid name! Last name must contain only letters and be less than or equal to 16 characters." + RESET);
                continue;
            }
            newEmployee.setLastName(lastName);
            break;
        }

        while (true) {
            System.out.print(BLUE + "Enter employee ID: " + RESET);
            try {
                idInput = scanner.nextLine();
                if (idInput.isEmpty()) { // Check if the input is empty
                    System.out.println(YELLOW + "⚠️ ID cannot be empty. Please enter a valid ID." + RESET);
                    continue;
                }
                Integer id = Integer.parseInt(idInput);
                if (id <= 0) {
                    System.out.println(YELLOW + "⚠️ Invalid ID! ID must be greater than 0." + RESET);
                    continue;
                }
                Employees employees = employeeDaoImpl.searchById(id);
                if (employees != null) {
                    System.out.println(RED + "This id already exists!" + RESET);
                    continue;
                }
                newEmployee.setId(id);
                break;
            } catch (InputMismatchException e) {
                System.out.println(YELLOW + "⚠️ Invalid ID format! Please enter a number." + RESET);
                scanner.nextLine(); // Clear invalid input
            }
        }

        while (true) {
            System.out.print(BLUE + "Enter employee gender (M/F): " + RESET);
            genderInput = scanner.nextLine();
//            scanner.nextLine();// Consume the newline after char input
            if (genderInput.isEmpty()) {
                System.out.println(YELLOW + "⚠️ ID cannot be empty. Please enter a valid Gender." + RESET);
                continue;
            }
            Character gender = genderInput.charAt(0);
            gender = Character.toUpperCase(gender);
            if (gender != 'M' && gender != 'F') {
                System.out.println(YELLOW + "⚠️ Invalid gender. Please input M or F." + RESET);
                continue;
            }
            newEmployee.setGender(gender);
            break;
        }

        birthDate = getValidDate(scanner, BLUE + "Enter employee birth date (YYYY-MM-DD): " + RESET);
        hireDate = getValidDate(scanner, BLUE + "Enter employee hire date (YYYY-MM-DD): " + RESET);

        newEmployee.setBirthDate(birthDate);
        newEmployee.setHireDate(hireDate);

        employeeDaoImpl.addEmployee(newEmployee);
    }

    @Override
    public void updateEmployee() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║        \u001B[33m \uD83D\uDCC8 Update Employee    \u001B[34m       ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        Integer id;
        String firstName, lastName;
//        String idInput;
        String genderInput;
        Date birthDate, hireDate;

        Employees updatedEmployee = new Employees();
        while (true) {
            System.out.print(BLUE + "Enter employee ID: " + RESET);
            try {
                id = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Input Only Number!!");
            }
        }
        Employees employees = employeeDaoImpl.searchById(id);
        if (employees == null) {
            System.out.println(YELLOW + "Employee not found!" + RESET);
            return;
        }
        System.out.println(GREEN + "[+] Employee Information" + RESET);
        System.out.println(BLUE + "ID: " + RESET + employees.getId());
        System.out.println(BLUE + "First Name: " + RESET + employees.getFirstName());
        System.out.println(BLUE + "Last Name: " + RESET + employees.getLastName());
        System.out.println(BLUE + "Gender: " + RESET + employees.getGender());
        System.out.println(BLUE + "Birth Date: " + RESET + employees.getBirthDate());
        System.out.println(BLUE + "Hire Date: " + RESET + employees.getHireDate());
        System.out.println();

        System.out.println(GREEN + "[+] Update Information" + RESET);

        while (true) {
            System.out.print(BLUE + "Enter for update employee first name: " + RESET);
            firstName = scanner.nextLine();
            if (firstName.isEmpty() || !firstName.matches("^[a-zA-Z]+$") || firstName.length() > 14) {
                System.out.println(YELLOW + "⚠️ Invalid name! First name must contain only letters and be less than or equal to 14 characters." + RESET);
                continue;
            }
            updatedEmployee.setFirstName(firstName);
            break;
        }

        while (true) {
            System.out.print(BLUE + "Enter for update employee last name: " + RESET);
            lastName = scanner.nextLine();
            if (lastName.isEmpty() || !lastName.matches("^[a-zA-Z]+$") || lastName.length() > 16) {
                System.out.println(YELLOW + "⚠️ Invalid name! Last name must contain only letters and be less than or equal to 16 characters." + RESET);
                continue;
            }
            updatedEmployee.setLastName(lastName);
            break;
        }
        while (true) {
            System.out.print(BLUE + "Enter for update employee gender (M/F): " + RESET);
            genderInput = scanner.nextLine();
//            scanner.nextLine();// Consume the newline after char input
            if (genderInput.isEmpty()) {
                System.out.println(YELLOW + "⚠️ ID cannot be empty. Please enter a valid Gender." + RESET);
                continue;
            }
            Character gender = genderInput.charAt(0);
            gender = Character.toUpperCase(gender);
            if (gender != 'M' && gender != 'F') {
                System.out.println(YELLOW + "⚠️ Invalid gender. Please input M or F." + RESET);
                continue;
            }
            updatedEmployee.setGender(gender);
            break;
        }
        birthDate = getValidDate(scanner, BLUE + "Enter for update employee birth date (YYYY-MM-DD): " + RESET);
        hireDate = getValidDate(scanner, BLUE + "Enter for update employee hire date (YYYY-MM-DD): " + RESET);

        updatedEmployee.setBirthDate(birthDate);
        updatedEmployee.setHireDate(hireDate);

        employeeDaoImpl.updateEmployee(id, updatedEmployee);
    }

    @Override
    public void deleteEmployee() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║        \u001B[33m \u267B\uFE0F Delete Employee    \u001B[34m        ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        Integer id;
        while (true) {
            System.out.print(BLUE + "Enter employee ID: " + RESET);
            try {
                id = Integer.parseInt(scanner.nextLine());
                employeeDaoImpl.deleteEmployee(id);
                break;
            } catch (NumberFormatException e) {
                System.out.println(YELLOW + "⚠️ Invalid ID format!" + RESET);
            }
        }

    }

    @Override
    public Pagination<List<Employees>> getAllEmployees(int page, int size) {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║       \u001B[33m \uD83D\uDCCB View Employee List \u001B[34m        ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String[] columnNames = {"ID", "First Name", "Last Name", "Gender", "Birth Date", "Hire Date"};
        for (int i = 0; i < columnNames.length; i++) {
            table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 30);
        }
        List<Employees> allEmployees = employeeDaoImpl.getAllEmployees();
        allEmployees.sort(Comparator.comparing(Employees::getId));
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1
        Pagination<List<Employees>> data = new Pagination<>();
        int totalRecords = allEmployees.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        if (page > totalPages) page = totalPages;
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);
        List<Employees> paginatedEmployee = allEmployees.subList(fromIndex, toIndex);
        for (Employees employee : paginatedEmployee) {
            table.addCell(String.valueOf(employee.getId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getFirstName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(employee.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getBirthDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getHireDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
        }
        System.out.println(table.render());
        data.setData(paginatedEmployee);
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
                getAllEmployees(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getAllEmployees(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getAllEmployees(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getAllEmployees(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getAllEmployees(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getAllEmployees(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getAllEmployees(page, size);
                }
            }
        }
        return data;
    }


    @Override
    public Employees findById() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║       \u001B[33m \uD83D\uDD0E Search Employee    \u001B[34m        ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        Integer id;
        Employees employees = new Employees();
        while (true) {
            System.out.print(BLUE + "Enter employee ID: " + RESET);
            try {
                id = Integer.parseInt(scanner.nextLine());
                employees = employeeDaoImpl.searchById(id);
                if (employees == null) {
                    System.out.println(YELLOW + "Employee with ID " + id + " not found!" + RESET);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println(YELLOW + "⚠️ Invalid ID format!" + RESET);
            }
        }
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String[] columnNames = {"ID", "First Name", "Last Name", "Gender", "Birth Date", "Hire Date"};
        for (int i = 0; i < columnNames.length; i++) {
            table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 30);
        }
        table.addCell(String.valueOf(employees.getId()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employees.getFirstName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employees.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(employees.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employees.getBirthDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employees.getHireDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
        System.out.println(table.render());
        return employees;
    }
}
