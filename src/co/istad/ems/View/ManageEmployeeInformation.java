package co.istad.ems.View;

import co.istad.ems.controller.EmployeeController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManageEmployeeInformation {

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    private final EmployeeController employeeController = new EmployeeController();

    public void menuEmployeeInformation() {
        Scanner scanner = new Scanner(System.in);
        int menu = -1;
        do {

            System.out.println("""
                    \u001B[34m╔══════════════════════════════════════╗
                    ║           \u001B[36mManage Employees\u001B[34m           ║
                    ╠══════════════════════════════════════╣
                    ║       \u001B[33m1. \u2795 Add Employee       \u001B[34m      ║
                    ║       \u001B[33m2. \uD83D\uDCCB View Employee List \u001B[34m      ║
                    ║       \u001B[33m3. \uD83D\uDD0E Search Employee    \u001B[34m      ║
                    ║       \u001B[33m4. \uD83D\uDCC8 Update Employee    \u001B[34m      ║
                    ║       \u001B[33m5. \u267B\uFE0F Delete Employee    \u001B[34m      ║
                    ║       \u001B[31m0. \u25C0 Exit               \u001B[34m       ║
                    ╚══════════════════════════════════════╝
                    \u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try {
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu) {
                    case 1 -> employeeController.addNewEmployee();
                    case 2 -> employeeController.getAllEmployees();
                    case 3 -> employeeController.FindEmployeeById();
                    case 4 -> employeeController.updateEmployee();
                    case 5 -> employeeController.deleteEmployee();
                    case 0 -> System.out.println("Exit.....");
                    default -> System.out.println("Invalid Option");
                }
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m\u26A0 Please enter a valid number.\u001B[0m");
                scanner.nextLine();
            }
            System.out.println("\n\n\n");

        } while (menu != 0);
    }
}

