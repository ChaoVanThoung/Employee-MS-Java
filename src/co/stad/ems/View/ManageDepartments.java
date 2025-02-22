package co.stad.ems.View;

import co.stad.ems.controller.DepartmentController;
import co.stad.ems.domain.Department;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;


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
    ║         \u001B[36mManage Departments\u001B[34m           ║
    ╠══════════════════════════════════════╣
    ║       \u001B[33m1. Add New Department\u001B[34m          ║
    ║       \u001B[33m2. Update Department\u001B[34m           ║
    ║       \u001B[33m3. Search By ID\u001B[34m                ║
    ║       \u001B[33m4. Delete By ID\u001B[34m                ║
    ║       \u001B[33m5. Show All Departments\u001B[34m        ║
    ║       \u001B[31m0. Exit\u001B[34m                        ║
    ╚══════════════════════════════════════╝
    \u001B[0m""");
            System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
            try{
                menu = scanner.nextInt();
                scanner.nextLine();
                switch (menu){
                    case 1 -> addNewDepartment();
                    case 2 -> updateById();
                    case 3 -> searchByID();
                    case 4 -> deleteById();
                    case 5 -> showAllDepartments();
                    default -> System.out.println("Invalid Option");
                }
            } catch (InputMismatchException e){
                System.out.println("\u001B[31m\u26A0 Please enter a valid number.\u001B[0m");
                scanner.nextLine();
            }
            System.out.println("\n\n\n");

        }while (menu != 0 );
    }

      /* Add new department */
    public void addNewDepartment() {
        Department newDepartment = new Department();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[36mAdd New Department\u001B[34m           ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        while (true){
            System.out.print("> Input department ID: ");
            newDepartment.setId(scanner.nextLine());
            if (newDepartment.getId().length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        while (true){
            System.out.println("Input name department name");
            newDepartment.setDepartmentName(scanner.nextLine());
            if (newDepartment.getDepartmentName().length() <= 40 && newDepartment.getDepartmentName().matches("^[a-zA-Z]+$")){
                break;
            } else {
                System.out.println("Invalid input. Must be alphabet and 40 characters or fewer.");
            }
        }
        departmentController.addNewDepartment(newDepartment);
    }

    /* Show All Department */
    public void showAllDepartments (){

        System.out.println("""
                      \u001B[34m╔══════════════════════════════════════╗
                      ║         \u001B[36mShow All Department\u001B[34m          ║
                      ╚══════════════════════════════════════╝\u001B[0m
                """);

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Department ID","Department Name"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }
        departmentController.getAllDepartments().forEach(department -> {
            table.addCell(department.getId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
        });
        System.out.println(table.render());
    }

    /* Search By id*/
    public void searchByID(){

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Department ID","Department Name"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }

        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mSearch By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);


        while (true){
            System.out.print("> Input department ID: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentController.findById(id);
        if (department != null){
            table.addCell(department.getId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            System.out.println(table.render());
        } else {
            System.out.println("Department not found");
        }
    }

    /* Deleted by id*/
    public void deleteById(){
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mDelete By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);

        while (true){
            System.out.print("> Input department ID: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentController.findById(id);
        if (department != null){
            departmentController.deleteDepartment(id);
        } else {
            System.out.println("Department not found");
        }
    }

    /* Update by id */
    public void updateById(){
        Department updatedDepartment = new Department();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mUpdate By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        while (true){
            System.out.print("> Input department ID For Search: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department oldDepartment = departmentController.findById(id);
        if (oldDepartment != null){
            while (true){
                System.out.println("Input name department name");
                updatedDepartment.setDepartmentName(scanner.nextLine());
         
                if (updatedDepartment.getDepartmentName().length() <= 40 && updatedDepartment.getDepartmentName().matches("^[a-zA-Z\\s]+$")){
                    departmentController.updateDepartment(id, updatedDepartment);
                    break;
                } else {
                    System.out.println("Invalid input. Must be alphabet and 40 characters or fewer.");
                }
            }
        } else {
            System.out.println("Department not found");
        }
    }

}
