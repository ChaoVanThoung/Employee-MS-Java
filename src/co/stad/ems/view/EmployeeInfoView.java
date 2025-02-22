package co.stad.ems.view;

import co.stad.ems.domain.Employees;
import co.stad.ems.service.employee.EmployeeImpl;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.util.Date.*;

public class EmployeeInfoView {
    private final EmployeeImpl emp = new EmployeeImpl();

    public void displayEmployeeInfoManagement(Scanner input) {
        int option = -1;
        do {
            emp.showMenu();
            System.out.print("\u001B[33m> Enter your option: \u001B[0m");
            try {
                option = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m⚠ Invalid input! Please enter a number.\u001B[0m");
                continue;
            }

            switch (option) {
                case 1 -> addEmployee(input);
                case 2 -> listEmployees();
                case 3 -> searchEmployee(input);
                case 4 -> updateEmployee(input);
                case 5 -> deleteEmployee(input);
                case 0 -> System.out.println("Exiting...");
                default -> System.out.println("\u001B[31m⚠ Invalid option! Please choose again.\u001B[0m");
            }
        } while (option != 0);
    }

    private void addEmployee(Scanner input) {
        String firstName, lastName;
        int id;
        char gender;
        Date birthDate, hireDate;

        while (true) {
            System.out.print("Enter employee first name: ");
            firstName = input.nextLine();
            if (!firstName.matches("^[a-zA-Z]+$")) {
                System.out.println("Invalid name! First name must contain only letters.");
                continue;
            }

            System.out.print("Enter employee last name: ");
            lastName = input.nextLine();
            if (!lastName.matches("^[a-zA-Z]+$")) {
                System.out.println("Invalid name! Last name must contain only letters.");
                continue;
            }

            System.out.print("Enter employee ID: ");
            try {
                id = input.nextInt();
                input.nextLine(); // Consume the leftover newline
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31mInvalid ID format! Please enter a number.\u001B[0m");
                input.nextLine(); // Clear invalid input
                continue;
            }

            System.out.print("Enter employee gender (M/F): ");
            gender = input.next().charAt(0);
            input.nextLine(); // Consume the newline after char input
            gender = Character.toUpperCase(gender);

            if (gender != 'M' && gender != 'F') {
                System.out.println("Invalid gender. Please input M or F.");
                continue;
            }

            birthDate = getValidDate(input, "Enter employee birth date (YYYY-MM-DD): ");
            hireDate = getValidDate(input, "Enter employee hire date (YYYY-MM-DD): ");

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
                return Date.valueOf(dateStr); // Converts valid "YYYY-MM-DD" format to java.sql.Date
            } catch (IllegalArgumentException e) {
                System.out.println("\u001B[31mInvalid date format! Please use YYYY-MM-DD.\u001B[0m");
            }
        }
    }



    private void listEmployees() {
        emp.listEmployees().forEach(System.out::println);
    }

    private void searchEmployee(Scanner input) {
        System.out.print("Enter employee ID to search: ");
        try {
            int searchId = Integer.parseInt(input.nextLine());
            System.out.println(emp.searchEmployee(searchId));
        } catch (InputMismatchException e) {
            System.out.println("\u001B[31mInvalid ID format!\u001B[0m");
        }
    }

    private void updateEmployee(Scanner input) {
        System.out.print("Enter employee ID to update: ");
        int id = Integer.parseInt(input.nextLine().trim());

        System.out.print("Enter new first name: ");
        String firstName = input.nextLine().trim();

        System.out.print("Enter new last name: ");
        String lastName = input.nextLine().trim();

        Employees updatedEmployee = Employees.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        emp.updateEmployee(id, updatedEmployee);
    }

    private void deleteEmployee(Scanner input) {
        System.out.print("Enter employee ID to delete: ");
        try {
            int id = Integer.parseInt(input.nextLine());
            emp.deleteEmployee(id);
        } catch (InputMismatchException e) {
            System.out.println("\u001B[31mInvalid ID format!\u001B[0m");
        }
    }
}
