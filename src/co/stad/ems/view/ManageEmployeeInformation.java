package co.stad.ems.view;

import co.stad.ems.domain.Employees;
import co.stad.ems.service.employee.EmployeeImpl;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class ManageEmployeeInformation {

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m"; // Reset color

    private final EmployeeImpl emp = new EmployeeImpl();

    public void displayEmployeeInfoManagement(Scanner input) {
        int option = -1;
        do {
            emp.showMenu();
            System.out.print( BLUE + "> Enter your option: " + RESET);
            try {
                option = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println( YELLOW + "⚠ Invalid input! Please enter a number." + RESET);
                input.nextLine();
                continue;
            }

            switch (option) {
                case 1 -> addEmployee(input);
                case 2 -> listEmployees();
                case 3 -> searchEmployee(input);
                case 4 -> updateEmployee(input);
                case 5 -> deleteEmployee(input);
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println( YELLOW + "⚠ Invalid option! Please choose again." + RESET);
            }
        } while (option != 0);
    }

    private void addEmployee(Scanner input) {
        String firstName, lastName;
        int id;
        char gender;
        Date birthDate, hireDate;

        while (true) {
            System.out.print(BLUE + "Enter employee first name: " + RESET);
            firstName = input.nextLine();
            if (!firstName.matches("^[a-zA-Z]+$") && firstName.length() > 14) {
                System.out.println( YELLOW + "⚠️ Invalid name! First name must contain only letters and less than 15 characters." + RESET);
                continue;
            }

            System.out.print( BLUE + "Enter employee last name: " + RESET);
            lastName = input.nextLine();
            if (!lastName.matches("^[a-zA-Z]+$") && lastName.length() > 16) {
                System.out.println( YELLOW + "⚠️ Invalid name! Last name must contain only letters and less than 17." + RESET);
                continue;
            }

            System.out.print(BLUE + "Enter employee ID: " + RESET);
            try {
                id = input.nextInt();
                input.nextLine(); // Consume the leftover newline
            } catch (InputMismatchException e) {
                System.out.println( YELLOW + "⚠️ Invalid ID format! Please enter a number." + RESET);
                input.nextLine(); // Clear invalid input
                continue;
            }

            System.out.print(BLUE + "Enter employee gender (M/F): " + RESET);
            gender = input.next().charAt(0);
            input.nextLine(); // Consume the newline after char input
            gender = Character.toUpperCase(gender);

            if (gender != 'M' && gender != 'F') {
                System.out.println( YELLOW + "⚠️ Invalid gender. Please input M or F." + RESET);
                continue;
            }

            birthDate = getValidDate(input, BLUE + "Enter employee birth date (YYYY-MM-DD): " + RESET);
            hireDate = getValidDate(input, BLUE + "Enter employee hire date (YYYY-MM-DD): " + RESET);
            break;
        }

        Employees newEmployee = Employees.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .birthDate(birthDate)
                .hireDate(hireDate)
                .build();

        emp.addEmployee(newEmployee);
    }

    // Helper method to validate date input
    private Date getValidDate(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = input.nextLine();
                return Date.valueOf(dateStr);
            } catch (IllegalArgumentException e) {
                System.out.println( YELLOW + "⚠️ Invalid date format! Please use YYYY-MM-DD." + RESET);
            }
        }
    }

//        private void listEmployees() {
//            emp.listEmployees().forEach(System.out::println);
//        }
private void listEmployees() {
    Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

    // Add column headers manually
    String[] columnNames = {"ID", "First Name", "Last Name", "Gender", "Birth Date", "Hire Date"};
    for (int i = 0; i < columnNames.length; i++) {
        table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
        table.setColumnWidth(i, 20, 30);
    }
    // Retrieve and display employee data
    emp.listEmployees().forEach(employee -> {
        table.addCell(String.valueOf(employee.getId()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employee.getFirstName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employee.getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(String.valueOf(employee.getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employee.getBirthDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(employee.getHireDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
    });

    System.out.println(table.render());
}


    private void searchEmployee(Scanner input) {
        System.out.print(BLUE + "Enter employee ID to search: " + RESET);
        try {
            int searchId = Integer.parseInt(input.nextLine());
            System.out.println(emp.searchEmployee(searchId));
        } catch (InputMismatchException e) {
            System.out.println(YELLOW + "⚠️ Invalid ID format!" + RESET);
        }
    }

    private void updateEmployee(Scanner input) {
        System.out.print( BLUE + "Enter employee ID to update: " + RESET);
        int id = Integer.parseInt(input.nextLine().trim());

        System.out.print("Enter new first name: ");
        String firstName = input.nextLine().trim();

        System.out.print(BLUE + "Enter new last name: " + RESET);
        String lastName = input.nextLine().trim();

        System.out.print(BLUE + "Enter employee gender (M/F): " + RESET);
        char gender = input.next().charAt(0);
        gender = Character.toUpperCase(gender);
        input.nextLine();

        Date birthDate = getValidDate(input, BLUE + "Enter new birth date (YYYY-MM-DD): " + RESET);
        Date hireDate = getValidDate(input, BLUE + "Enter new hire date (YYYY-MM-DD): " + RESET);

        Employees updatedEmployee = Employees.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .gender(gender)
                .birthDate(birthDate)
                .hireDate(hireDate)
                .build();

        emp.updateEmployee(id, updatedEmployee);
    }

    private void deleteEmployee(Scanner input) {
        System.out.print(BLUE + "Enter employee ID to delete: " + RESET);
        try {
            int id = Integer.parseInt(input.nextLine());
            emp.deleteEmployee(id);
        } catch (InputMismatchException e) {
            System.out.println( YELLOW + "⚠️ Invalid ID format!" + RESET);
        }
    }
}
