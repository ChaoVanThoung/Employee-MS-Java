package co.istad.ems.View;

import co.istad.ems.controller.DepartmentMangerController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class MangeDepartmentManger {
    Scanner scanner = new Scanner(System.in);
    private final DepartmentMangerController departmentMangerController = new DepartmentMangerController();

    public void menuDepartmentManger() {
        int menu = -1;
        do {

            System.out.println("""
                    \u001B[34m╔═══════════════════════════════════════════════╗
                    ║             \u001B[36mManager Departments \u001B[34m              ║
                    ╠═══════════════════════════════════════════════╣
                    ║       \u001B[33m1. \u2795 Add Department Manager\u001B[34m            ║
                    ║       \u001B[33m2. \u274C Remove Department Manager\u001B[34m         ║
                    ║       \u001B[33m3. \uD83D\uDD0D Search Department Manager\u001B[34m         ║
                    ║       \u001B[33m4. \uD83D\uDD04 Update Department Manager\u001B[34m         ║
                    ║       \u001B[33m5. \uD83D\uDCCB View List Department Manager\u001B[34m      ║                         \s
                    ║       \u001B[31m0. \u25C0 Exit                    \u001B[34m           ║
                    ╚═══════════════════════════════════════════════╝
                    \u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try {
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu) {
                    case 1 -> departmentMangerController.addNewDepartmentManager();
                    case 2 -> departmentMangerController.deleteDepartmentManager();
                    case 3 -> departmentMangerController.searchByDepartmentId();
                    case 4 -> departmentMangerController.updateDepartmentManager();
                    case 5 -> departmentMangerController.getAllDepartmentsManager();
                    case 0 -> System.out.println("Exit");
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

