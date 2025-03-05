package co.istad.ems.View;

import co.istad.ems.controller.DepartmentEmployeeController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManageDepartmentEmployee {

    Scanner scanner = new Scanner(System.in);
    private final DepartmentEmployeeController departmentEmployeeController = new DepartmentEmployeeController();

    /* */
    public void menuDepartmentEmployee() {
        int menu = -1;
        do {

            System.out.println("""
                    \u001B[34m╔══════════════════════════════════════════════════╗
                    ║         \u001B[36mManage Departments Employee\u001B[34m              ║
                    ╠══════════════════════════════════════════════════╣
                    ║       \u001B[33m1. \u2795 Add New Employee For Department\u001B[34m      ║
                    ║       \u001B[33m2. \uD83D\uDD04 Update Employee For Department\u001B[34m       ║
                    ║       \u001B[33m3. \uD83D\uDD0D Search Employee\u001B[34m                      ║
                    ║       \u001B[33m4. \uD83D\uDCC2 Search Department\u001B[34m                    ║
                    ║       \u001B[33m5. \u274C Delete By Employee ID\u001B[34m                ║
                    ║       \u001B[33m6. \uD83D\uDCCB Show All\u001B[34m                             ║
                    ║       \u001B[31m0. \u25C0 Exit                    \u001B[34m              ║
                    ╚══════════════════════════════════════════════════╝
                    \u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try {
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu) {
                    case 1 -> departmentEmployeeController.addNew();
                    case 2 -> departmentEmployeeController.updateById();
                    case 3 -> departmentEmployeeController.findByEmpId();
                    case 4 -> departmentEmployeeController.findByDeptId();
                    case 5 -> departmentEmployeeController.deleteById();
                    case 6 -> departmentEmployeeController.getAll();
                    case 0 -> System.out.println("Exit...");
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
