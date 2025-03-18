package co.istad.ems.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {
    private static Scanner scanner;

    ManageEmployeeInformation manageEmployeeInformation = new ManageEmployeeInformation();
    ManageTitle manageTitle = new ManageTitle();
    ManageSalary manageSalary = new ManageSalary();
    ManageDepartmentEmployee manageDepartmentEmployee = new ManageDepartmentEmployee();
    ManageDepartments manageDepartments = new ManageDepartments();
    MangeDepartmentManger manageDepartmentManger = new MangeDepartmentManger();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public UI() {
        scanner = new Scanner(System.in);
    }

    public void ui() throws InterruptedException {
        displayWelcomeMessage();
        System.out.println("""
                  _____                 _                         __  __ ____ \s
                 | ____|_ __ ___  _ __ | | ___  _   _  ___  ___  |  \\/  / ___|\s
                 |  _| | '_ ` _ \\| '_ \\| |/ _ \\| | | |/ _ \\/ _ \\ | |\\/| \\___ \\\s
                 | |___| | | | | | |_) | | (_) | |_| |  __/  __/ | |  | |___) |
                 |_____|_| |_| |_| .__/|_|\\___/ \\__, |\\___|\\___| |_|  |_|____/\s
                                 |_|            |___/                        \s
                """);
        System.out.println("--------------------------------------------------------------");
//        displayLoadingAnimation();
        progressBarLoading();
        menu();
    }

    public void menu() throws InterruptedException {
        int menu = -1;
        do {
            System.out.println("""
                        \u001B[34m╔════════════════════════════════════════════════╗
                        ║         \u001B[36m🧑‍💼 Employee Management System\u001B[34m          ║
                        ╠════════════════════════════════════════════════╣
                        ║   \u001B[33m1. 🧑‍💻 Manage Employee Information\u001B[34m            ║
                        ║   \u001B[33m2. 🏢 Manage Departments\u001B[34m                     ║
                        ║   \u001B[33m3. 💸 Manage Salary\u001B[34m                          ║
                        ║   \u001B[33m4. 🎯 Manage Position\u001B[34m                        ║
                        ║   \u001B[33m5. 👥 Manage Department's Employees\u001B[34m          ║
                        ║   \u001B[33m6. 👔 Manage Department's Manager\u001B[34m            ║
                        ║   \u001B[31m0. ⏻ Exit\u001B[34m                                    ║
                        ╚════════════════════════════════════════════════╝
                        \u001B[0m
                    """);

            System.out.print("> Please Choose Menu: ");


            try {
                menu = scanner.nextInt();
                scanner.nextLine(); // Consume the newline to avoid input issues
                switch (menu) {
                    case 1 -> manageEmployeeInformation.menuEmployeeInformation();
                    case 2 -> manageDepartments.menuDepartments();
                    case 3 -> manageSalary.menuSalary();
                    case 4 -> manageTitle.menuTitle();
                    case 5 -> manageDepartmentEmployee.menuDepartmentEmployee();
                    case 6 -> manageDepartmentManger.menuDepartmentManger();
                    case 0 -> System.out.println("Exiting... Thank you!");
                    default -> System.out.println("Invalid option! Please enter a number between 0 and 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\u001B[31m\u26A0 Input Only Numbers!!\u001B[0m"); // ⚠️ Warning sign in red
                System.out.println("\u001B[33m🔄 Please Try Again!\u001B[0m");
                scanner.nextLine(); // Clear invalid input to prevent infinite loop
            }
            System.out.println();
            System.out.println();
        } while (menu != 0);
    }

    private void displayWelcomeMessage() throws InterruptedException {
        String welcome = "Welcome to";
        for (char c : welcome.toCharArray()) {
            System.out.print(ANSI_YELLOW + c + ANSI_RESET);
            Thread.sleep(100); // Delay for effect
        }
        System.out.println();
    }



    public void progressBarLoading() {
        System.out.print("\u001B[33mLoading: \u001B[0m");
        int total = 20; // Total length of the progress bar
        for (int i = 0; i <= total; i++) {
            System.out.print("\u001B[32m["); // Start of the progress bar
            for (int j = 0; j < i; j++) {
                System.out.print("="); // Filled part
            }
            for (int j = i; j < total; j++) {
                System.out.print(" "); // Empty part
            }
            System.out.print("] " + (i * 100 / total) + "%\r"); // Percentage and carriage return
            try {
                Thread.sleep(200); // Adjust speed
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("\u001B[32m Done!\u001B[0m");
    }
}
