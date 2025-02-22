package co.stad.ems.service;

import co.stad.ems.dao.EmployeeDao;
import co.stad.ems.dao.EmployeeDaoImpl;
import co.stad.ems.dao.TitleDaoImpl;
import co.stad.ems.domain.Employees;
import co.stad.ems.domain.Title;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TitleServiceImpl implements TitleService {
    private final TitleDaoImpl titleDaoImpl = new TitleDaoImpl();
    Scanner scanner = new Scanner(System.in);
    private final EmployeeDao employeeDaoImpl = new EmployeeDaoImpl();

    Integer id;

    @Override
    public void addTitle() {
//        titleDaoImpl.insertTitle(title);
        Title newTitle = new Title();
        System.out.print("Please Input Employee ID: ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter a valid numeric Employee ID.");
            scanner.next(); // Consume invalid input
        }
        id = scanner.nextInt();
        scanner.nextLine();
        Employees employeesId = employeeDaoImpl.searchById(id);
        if (employeesId == null) {
            System.out.println("Employee not found with ID: " + id);
            return; // Exit if employee is not found
        }
        newTitle.setEmployeesId(employeesId.getId());

        //       Validate title
        System.out.print("Input Title Or Position: ");
        String titleInput = scanner.nextLine().trim();
        if (titleInput.isEmpty() || titleInput.length() > 50) {
            System.out.println("Invalid title. Must not be empty and under 50 characters.");
            return;
        }
        newTitle.setTitle(titleInput);

        //        Validation date
        System.out.print("Input From Date (YYYY-MM-DD): ");
        Date fromDate = inputValidDate(scanner); // Use the helper method
        newTitle.setFromDate(fromDate);
        System.out.print("Input To Date (YYYY-MM-DD): ");
        Date toDate = inputValidDate(scanner);
        newTitle.setToDate(toDate);

        titleDaoImpl.insertTitle(newTitle);
    }

    public Date inputValidDate(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();
            // Allow blank input for a null date (e.g., ongoing period)
            if (input.isEmpty()) {
                return null;
            }
            try {
                // Parse and return a valid date
                return Date.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("❌ Invalid date format. Please use YYYY-MM-DD.");
                System.out.print("Please Input Again: ");
            }
        }
    }

    @Override
    public List<Title> getAllTitles() {
        return titleDaoImpl.queryAllTitle();
    }

    @Override
    public void updateTitle() {
        Title updateTitle = new Title();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mUpdate By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        while (true) {
            System.out.print("> Please Input: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Title oldTitle = titleDaoImpl.searchTitleById(id);
        if (oldTitle != null) {
            while (true){
                System.out.print("Enter Title Or Position: ");
                String titleInput = scanner.nextLine().trim();
                scanner.nextLine();


                if (titleInput.length() <= 50 && titleInput.matches("^[a-zA-Z\\s]+$")) {
                    updateTitle.setTitle(titleInput);
                    System.out.print("Input From Date (YYYY-MM-DD): ");
                    Date fromDate = inputValidDate(scanner); // Use the helper method
                    updateTitle.setFromDate(fromDate);

                    System.out.print("Input To Date (YYYY-MM-DD): ");
                    Date toDate = inputValidDate(scanner);
                    updateTitle.setToDate(toDate);
                    titleDaoImpl.updateTitleById(id, updateTitle);
                    break;
                } else {
                    System.out.println("Invalid input. Must be al   phabet and 40 characters or fewer.");
                }
            }
        } else {
            System.out.println("not found");
        }
    }

    @Override
    public void findTitleById() {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"ID", "Name", "Position", "From Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 25, 40);
        }
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mSearch By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Title title = titleDaoImpl.searchTitleById(id);
        if (title != null) {
            Employees employee = employeeDaoImpl.searchById(title.getEmployeesId());

            if (employee != null) {
                table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(employee.getFirst_name() + " " + employee.getLast_name(),
                        new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(title.getTitle(), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            }
        }
        System.out.println(table.render());
    }

    @Override
    public void deleteTitle() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mDelete By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m
                """);
        while (true) {
            System.out.print("> Please Input: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Title title = titleDaoImpl.searchTitleById(id);
        if (title != null) {
            titleDaoImpl.deleteTitleById(id);
        } else {
            System.out.println("Title not found with ID: " + id);
        }

    }
}
