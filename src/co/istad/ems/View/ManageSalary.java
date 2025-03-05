package co.istad.ems.View;

import co.istad.ems.controller.SalaryController;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ManageSalary {

    Scanner scanner = new Scanner(System.in);
    private final SalaryController salaryController = new SalaryController();

    public void menuSalary() {
        int menu = -1;
        do{

            System.out.println("""
    \u001B[34m╔══════════════════════════════════════╗
    ║             \u001B[36mManage Salary\u001B[34m            ║
    ╠══════════════════════════════════════╣
    ║         \u001B[33m1. ➕ Add New Salary\u001B[34m         ║
    ║         \u001B[33m2. 🔄 Update Salary\u001B[34m          ║
    ║         \u001B[33m3. 🔍 Filter Salary\u001B[34m          ║
    ║         \u001B[33m4. ❌ Delete Salary\u001B[34m          ║
    ║         \u001B[33m5. 📊 Show All Salary\u001B[34m        ║
    ║         \u001B[31m0.  \u25C0 Exit              \u001B[34m     ║
    ╚══════════════════════════════════════╝
    \u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try{
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu){
                    case 1 -> salaryController.addNewSalary();
                    case 2 -> salaryController.updateSalary();
                    case 3 -> salaryController.filterSalary();
                    case 4 -> salaryController.deleteSalary();
                    case 5 -> salaryController.getAllSalary();
                    case 0 -> System.out.println("Exit...");
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
