package co.istad.ems.View;

import co.istad.ems.controller.DepartmentController;


import java.util.InputMismatchException;
import java.util.Scanner;

public class ManageDepartments {
    String id;
    Scanner scanner = new Scanner(System.in);
    DepartmentController departmentController = new DepartmentController();


    /* Menu Departments */
    public void menuDepartments() {
        int menu = -1;
        do{

            System.out.println("""
    \u001B[34m╔══════════════════════════════════════╗
    ║         \u001B[36m🏢 Manage Departments\u001B[34m        ║
    ╠══════════════════════════════════════╣
    ║       \u001B[33m1. ➕ Add New Department\u001B[34m       ║
    ║       \u001B[33m2. ✏️ Update Department\u001B[34m        ║
    ║       \u001B[33m3. 🔍 Search By ID\u001B[34m             ║
    ║       \u001B[33m4. 🗑️ Delete By ID\u001B[34m             ║
    ║       \u001B[33m5. 📄 Show All Departments\u001B[34m     ║
    ║       \u001B[31m0.  ⏻ Exit\u001B[34m                     ║
    ╚══════════════════════════════════════╝\u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try{
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu){
                    case 1 -> departmentController.addNewDepartment();
                    case 2 -> departmentController.updateDepartment();
                    case 3 -> departmentController.findById();
                    case 4 -> departmentController.deleteDepartment();
                    case 5 -> departmentController.getAllDepartments();
                    case 0 -> System.out.println("Exit..");
                    default -> System.out.println("Invalid Option");
                }
            } catch (InputMismatchException e){
                System.out.println("\u001B[31m\u26A0 Please enter a valid number.\u001B[0m");
                scanner.nextLine();
            }
            System.out.println("\n\n\n");

        }while (menu != 0 );
    }

}
