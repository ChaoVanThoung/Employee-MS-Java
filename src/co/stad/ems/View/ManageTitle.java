package co.stad.ems.View;


import co.stad.ems.controller.TitleController;
import co.stad.ems.dao.EmployeeDao;
import co.stad.ems.dao.EmployeeDaoImpl;
import co.stad.ems.domain.Employees;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;


import java.util.InputMismatchException;
import java.util.Scanner;


public class ManageTitle {
    Scanner scanner = new Scanner(System.in);
    private final TitleController titleController = new TitleController();
    private final EmployeeDao employeeDao = new EmployeeDaoImpl();

    // menu
    public void menuTitle() {
            int menu = -1;
            do{

                System.out.println("""
    \u001B[34m╔══════════════════════════════════════╗
    ║         \u001B[36mManage Job Position\u001B[34m          ║
    ╠══════════════════════════════════════╣
    ║       \u001B[33m1. Add Title\u001B[34m                   ║
    ║       \u001B[33m2. Update Title\u001B[34m                ║
    ║       \u001B[33m3. Search Title\u001B[34m                ║
    ║       \u001B[33m4. Delete Title\u001B[34m                ║
    ║       \u001B[33m5. Show All Title\u001B[34m              ║
    ║       \u001B[31m0. Exit\u001B[34m                        ║
    ╚══════════════════════════════════════╝
    \u001B[0m""");
                System.out.print("\u001B[33m> Please Choose Your Option: \u001B[0m");
                try{
                    menu = scanner.nextInt();
                    scanner.nextLine();
                    switch (menu){
                        case 1 -> addTitle();
                        case 2 -> updateTitle();
                        case 3 -> findById();
                        case 4 -> deleteById();
                        case 5 -> showAllTitles();
                        default -> System.out.println("Invalid Option");
                    }
                } catch (InputMismatchException e){
                    System.out.println("\u001B[31m\u26A0 Please enter a valid number.\u001B[0m");
                    scanner.nextLine();
                }
                System.out.println("\n\n\n");

            }while (menu != 0 );
    }

    /* add title*/
    private void addTitle(){
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mAdd Position\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        titleController.addTitle();
    }

    /* Show all title */
    private void showAllTitles() {

        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║          \u001B[36mShow All Titile\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");

        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"ID","Name","Position","From Date","To Date"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }

        titleController.getAllTitles().forEach(title ->{
            Employees employee = employeeDao.searchById(title.getEmployeesId());
            table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getFirst_name() + " " + employee.getLast_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(title.getTitle(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        });
        System.out.println(table.render());
    }

    /*Search By employee id*/
    public void findById(){
        titleController.findById();
    }

    /*Delete by employee id*/
    public void deleteById(){
        titleController.deleteTitle();
    }

    /*Update By id*/
    public  void updateTitle(){
        titleController.updateTitle();
    }
}