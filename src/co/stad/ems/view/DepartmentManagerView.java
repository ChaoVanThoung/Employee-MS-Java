package co.stad.ems.view;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

import co.stad.ems.domain.DepartmentManager;
import co.stad.ems.domain.Employees;
import co.stad.ems.service.employee.DepartmentManagerImpl;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

public class DepartmentManagerView {
    private final DepartmentManagerImpl departmentManager = new DepartmentManagerImpl();

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[36m";
    public static final String RESET = "\u001B[0m"; // Reset color

    private Date getValidDate(Scanner input, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String dateStr = input.nextLine();
                return Date.valueOf(dateStr);
            } catch (IllegalArgumentException e) {
                System.out.println("\u001B[31mInvalid date format! Please use YYYY-MM-DD.\u001B[0m");
            }
        }
    }

    public void displayDepartmentManagerMenu(Scanner input) {
        int option = -1;

        do {
            System.out.println( BLUE + """
                    ╔════════════════════════════════════════════════════════════════════════╗
                    ║                            Department Manager Menu                     ║
                    ╠════════════════════════════════════════════════════════════════════════╣
                    ║       1. ➕ Add Department Manager                                     ║
                    ║       2.  🗑 Remove Department Manager                                  ║
                    ║       3. 🔎 Search Department Manager                                  ║
                    ║       4. 📋 View Department Manager                                    ║
                    ║       0. ◀️ Exit                                                       ║
                    ╚════════════════════════════════════════════════════════════════════════╝
                    """ + RESET);
            System.out.print( BLUE + "> Enter your option: " + RESET);
            try {
                option = input.nextInt();
                input.nextLine();
            } catch (InputMismatchException e) {
                System.out.println( RED + "Invalid input! Please enter a valid number." + RESET);
                input.nextLine();
                continue;
            }
            switch (option) {
                case 1 -> addDepartmentManager(input);
                case 2 -> removeDepartmentManager(input);
                case 3 -> searchDepartmentManager(input);
                case 4 -> viewDepartmentManagers();
                case 0 -> System.out.println( YELLOW + "Exiting..." + RESET);
                default -> System.out.println( RED + "Invalid option! Please enter a valid number." + RESET);
            }

        } while (option != 0);
    }

    public void addDepartmentManager(Scanner input) {
        int managerId;
        String departmentId;
        Date fromDate;
        Date toDate;

        while (true) {
            try {
                System.out.print( BLUE + "Enter manager ID: " + RESET);
                managerId = input.nextInt();
                input.nextLine();

                System.out.print( BLUE + "Enter department ID: " + RESET);
                departmentId = input.nextLine();

                fromDate = getValidDate(input, BLUE +"Enter from date (YYYY-MM-DD): " + RESET);
                toDate = getValidDate(input, BLUE + "Enter to date (YYYY-MM-DD): " + RESET);

                if (toDate.before(fromDate)) {
                    System.out.println( RED + "Error: To Date must be after From Date!" + RESET);
                    continue;
                }

                Employees employees = new Employees(managerId);

                DepartmentManager manager = DepartmentManager.builder()
                        .employees(employees)
                        .departmentId(departmentId)
                        .fromDate(fromDate)
                        .toDate(toDate)
                        .build();

                departmentManager.insertDepartmentManager(manager);
                return;

            } catch (InputMismatchException e) {
                System.out.println( RED + "Invalid input! Please enter a valid number." + RESET);
                input.nextLine();
            } catch (Exception e) {
                System.out.println(RED + e.getMessage() + RESET);
            }
        }
    }

    public void removeDepartmentManager(Scanner input) {
        int managerId;
        while (true) {
            try {
                System.out.print( BLUE + "Enter manager ID: " + RESET);
                managerId = input.nextInt();
                input.nextLine();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }
        departmentManager.deleteDepartmentManager(managerId);
    }

    void searchDepartmentManager(Scanner input) {
        int managerId;
        while (true) {
            try {
                System.out.print(BLUE + "Enter manager ID: " + RESET);
                managerId = input.nextInt();
                input.nextLine();
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input.nextLine(); // Clear buffer
            }
        }

        DepartmentManager manager = departmentManager.getDepartmentManagerById(managerId);

        if (manager != null) {
            // Create table with 7 columns
            Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

            // Define column headers
            final String[] columnNames = {"ID", "Department ID", "Department", "Full Name", "Gender", "From Date", "To Date"};

            // Add headers to the table
            for (int i = 0; i < columnNames.length; i++) {
                table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
                table.setColumnWidth(i, 20, 30); // Set column width for better formatting
            }

            // Add manager's data to the table
            table.addCell(String.valueOf(manager.getEmployees().getId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(manager.getDepartmentId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(manager.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(manager.getEmployees().getFirstName() + " " + manager.getEmployees().getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(manager.getEmployees().getGender()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(manager.getFromDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(manager.getToDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));

            // Render the table
            System.out.println(table.render());
        } else {
            System.out.println("Department Manager not found.");
        }
    }


    public void viewDepartmentManagers() {
        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);

        final String[] columnNames = {"ID", "Department ID", "Department","Full Name", "Gender", "From Date", "To Date"};

        for (int i = 0; i < columnNames.length; i++) {
            table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 20, 30);
        }

        // Retrieve and display employee data
        departmentManager.getAllDepartmentManagers().forEach(departmentManager -> {
            table.addCell(String.valueOf(departmentManager.getEmployees().getId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(departmentManager.getDepartmentId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(departmentManager.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(departmentManager.getEmployees().getFirstName() + " " + departmentManager.getEmployees().getLastName(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf((departmentManager.getEmployees().getGender())), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(departmentManager.getFromDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(departmentManager.getToDate().toString(), new CellStyle(CellStyle.HorizontalAlign.center));
        });

        System.out.println(table.render());
    }
}
