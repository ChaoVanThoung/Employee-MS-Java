package co.istad.ems.View;


import co.istad.ems.controller.TitleController;
import co.istad.ems.dao.EmployeeDao;
import co.istad.ems.dao.EmployeeDaoImpl;


import java.util.InputMismatchException;
import java.util.Scanner;


public class ManageTitle {
    Scanner scanner = new Scanner(System.in);
    private final TitleController titleController = new TitleController();
    private final EmployeeDao employeeDao = new EmployeeDaoImpl();
    int menu = -1;

    // menu
    public void menuTitle() {

            do{

                System.out.println("""
            \u001B[34m╔══════════════════════════════════════╗
            ║         \u001B[36m📋 Manage Job Position\u001B[34m       ║
            ╠══════════════════════════════════════╣
            ║   \u001B[33m1. ➕ Add Position\u001B[34m                 ║
            ║   \u001B[33m2. ✏️ Update Position\u001B[34m              ║
            ║   \u001B[33m3. 🔍 Search Position\u001B[34m              ║
            ║   \u001B[33m4. 🗑️ Delete Position\u001B[34m              ║
            ║   \u001B[33m5. 📄 Show All Position\u001B[34m            ║
            ║   \u001B[33m6. 🏷️ Filter By Position\u001B[34m           ║
            ║   \u001B[31m0.  ⏻ Exit\u001B[34m                         ║
            ╚══════════════════════════════════════╝
            \u001B[0m
            """);
                System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
                try{
                    menu = scanner.nextInt();
                    scanner.nextLine();
                    switch (menu){
                        case 1 -> titleController.addTitle();
                        case 2 -> updateTitle();
                        case 3 -> titleController.findById();
                        case 4 -> titleController.deleteTitle();
                        case 5 -> titleController.getAllTitles();
                        case 6 -> titleController.findTitleByTitle();
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



    /*Update */
    public  void updateTitle(){

       do {
           System.out.println("""
            \u001B[34m╔══════════════════════════════════════╗
            ║            \u001B[36m📝 Update Title\u001B[34m           ║
            ╠══════════════════════════════════════╣
            ║   \u001B[33m1. 🔄 Update All\u001B[34m                   ║
            ║   \u001B[33m2. 🏷️ Update Only Position\u001B[34m         ║
            ║   \u001B[33m3. 📅 Update Only From Date\u001B[34m        ║
            ║   \u001B[33m4. 📆 Update Only To Date\u001B[34m          ║
            ║   \u001B[31m0. ⏻ Exit\u001B[34m                          ║
            ╚══════════════════════════════════════╝
            \u001B[0m
            """);
           System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
           try{
               menu = scanner.nextInt();
               scanner.nextLine();
               switch (menu){
                   case 1 -> titleController.updateTitle();
                   case 2 -> titleController.updateOnlyTitle();
                   case 3 -> titleController.updateOnlyFromDate();
                   case 4 -> titleController.updateOnlyToDate();
                   case 0 -> {
                       System.out.println("Exit...");
                       menuTitle();
                       return;
                   }
                   default -> System.out.println("Invalid Option");
               }
           } catch (InputMismatchException e){
               System.out.println("\u001B[31m\u26A0 Please enter a valid number.\u001B[0m");
               scanner.nextLine();
           }
           System.out.println("\n\n\n");
       }while (menu != 0 );
    }

    /*filter By title*/
}