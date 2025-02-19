package co.stad.ems.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class UI {
    private static Scanner scanner;

    ManageEmployeeInformation manageEmployeeInformation = new ManageEmployeeInformation();

    public UI(){
        scanner = new Scanner(System.in);
    }

    public void ui () {

        System.out.println("""
                  _____                 _                         __  __ ____ \s
                 | ____|_ __ ___  _ __ | | ___  _   _  ___  ___  |  \\/  / ___|\s
                 |  _| | '_ ` _ \\| '_ \\| |/ _ \\| | | |/ _ \\/ _ \\ | |\\/| \\___ \\\s
                 | |___| | | | | | |_) | | (_) | |_| |  __/  __/ | |  | |___) |
                 |_____|_| |_| |_| .__/|_|\\___/ \\__, |\\___|\\___| |_|  |_|____/\s
                                 |_|            |___/                        \s
                """);
        System.out.println("--------------------------------------------------------------");
        menu();
    }
    public void menu() {
        int menu = -1;
        do {
            System.out.println("1. Manage Employee Information");
            System.out.println("2. Manage Departments");
            System.out.println("3. Manage Salary");
            System.out.println("0. Exit");

            System.out.print("> Please Choose Menu: ");

            try {
                menu = scanner.nextInt();
                scanner.nextLine(); // Consume the newline to avoid input issues

                switch (menu) {
                    case 1 -> manageEmployeeInformation.menu();
                    case 2 -> System.out.println("Manage Departments is under development.");
                    case 3 -> System.out.println("Manage Salary is under development.");
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

}
