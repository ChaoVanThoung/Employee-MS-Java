package co.istad.ems.service;

import co.istad.ems.dao.EmployeeDao;
import co.istad.ems.dao.EmployeeDaoImpl;
import co.istad.ems.dao.TitleDaoImpl;
import co.istad.ems.domain.Employees;
import co.istad.ems.domain.Title;
import co.istad.ems.util.Pagination;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TitleServiceImpl implements TitleService {
    private final TitleDaoImpl titleDaoImpl = new TitleDaoImpl();
    Scanner scanner = new Scanner(System.in);
    private final EmployeeDao employeeDaoImpl = new EmployeeDaoImpl();

    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RESET = "\u001B[0m";

    Integer id;

    @Override
    public void addTitle() {
//        titleDaoImpl.insertTitle(title);
        Title newTitle = new Title();

        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║             \u001B[36mAdd Position\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");

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
        Title existingTitle = titleDaoImpl.searchTitleById(id);
        if (existingTitle != null) {
            System.out.println("Employee already has a title: " + existingTitle.getTitle());
            return; // Exit if the employee already has a title
        }
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

    @Override
    public Pagination<List<Title>> getTitles(int page, int size) {
        System.out.println("""
            \u001B[34m╔══════════════════════════════════════╗
            ║          \u001B[36m📄 Show All Position\u001B[34m        ║
            ╚══════════════════════════════════════╝\u001B[0m""");
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"ID","Name","Position","From Date","To Date"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }
        List<Title> allTitle = titleDaoImpl.queryAllTitle();
        allTitle.sort(Comparator.comparing(Title::getEmployeesId));
        // Ensure valid page and size
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1

        Pagination<List<Title>> data = new Pagination<>();
        int totalRecords = allTitle.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Handle cases where page exceeds total pages
        if (page > totalPages) page = totalPages;

        // Calculate pagination indices
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);
        List<Title> paginatedTitle = allTitle.subList(fromIndex, toIndex);
        for (Title title : paginatedTitle) {
            Employees employee = employeeDaoImpl.searchById(title.getEmployeesId());
            table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employee.getFirstName() + " " + employee.getLastName(),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(title.getTitle(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        }
        System.out.println(table.render());
        data.setData(paginatedTitle);
        data.setTotalRecord(totalRecords);
        data.setTotalPages(totalPages);
        data.setCurrentPage(page);

        System.out.println("-----------------------------------------------------------------------------------------------------------------");
        System.out.println("Page " + page + " of " + totalPages);
        System.out.println("(N) Next Page");
        System.out.println("(P) Previous Page");
        System.out.println("(E) Exit");
        System.out.print("> Paginate (Enter page number or option): ");

        String option = scanner.nextLine();

        if (option.matches("\\d+")) {
            int requestedPage = Integer.parseInt(option);
            if (requestedPage >= 1 && requestedPage <= totalPages) {
                getTitles(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getTitles(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getTitles(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getTitles(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getTitles(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getTitles(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getTitles(page, size);
                }
            }
        }
        return data;
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

//    @Override
//    public void getAllTitles() {
//
//        System.out.println("""
//                \u001B[34m╔══════════════════════════════════════╗
//                ║          \u001B[36mShow All Titile\u001B[34m             ║
//                ╚══════════════════════════════════════╝\u001B[0m""");
//
//        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
//        String columNames[] = {"ID","Name","Position","From Date","To Date"};
//        for(int i=0;i<columNames.length;i++){
//            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
//            table.setColumnWidth(i,25,40);
//        }
//
//        titleDaoImpl.queryAllTitle().forEach(title ->{
//            Employees employee = employeeDaoImpl.searchById(title.getEmployeesId());
//            table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(employee.getFirst_name() + " " + employee.getLast_name(),
//                    new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(title.getTitle(), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
//        });
//        System.out.println(table.render());
//    }

    @Override
    public void updateTitle() {
        Title updateTitle = new Title();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║            \u001B[36m🛠️ Update All\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Input Only Number");
            }
        }
        Title oldTitle = titleDaoImpl.searchTitleById(id);
        if (oldTitle != null) {
            while (true) {
                System.out.print("Enter Title Or Position: ");
                scanner.nextLine();
                String titleInput = scanner.nextLine().trim();

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
                    System.out.println("Invalid input. Must be alphabet and 40 characters or fewer.");
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
                ║        \u001B[36m🔍 Search By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println(YELLOW + "Input Only Number" + RESET);
            }
        }
        Title title = titleDaoImpl.searchTitleById(id);
        if (title != null) {
            Employees employee = employeeDaoImpl.searchById(title.getEmployeesId());

            if (employee != null) {
                table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(employee.getFirstName() + " " + employee.getLastName(),
                        new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(title.getTitle(), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
                table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            }

            System.out.println(table.render());
        } else {
            System.out.println(RED + "not found" + RESET);
        }
    }

    @Override
    public void deleteTitle() {
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║          \u001B[36m🗑️ Delete By ID\u001B[34m             ║
                ╚══════════════════════════════════════╝ \u001B[0m""");
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

    @Override
    public void filterByTitle() {
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"ID", "Name", "Position", "From Date", "To Date"};
        for (int i = 0; i < columNames.length; i++) {
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 25, 40);
        }
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║         \u001B[36m🏷️ Filter By Title\u001B[34m           ║
                ╚══════════════════════════════════════╝\u001B[0m """);;
        List<Object[]> titleWithEmployeeList = new ArrayList<>();
        while (true) {
            System.out.print("Please Input Title For Filter: ");
            String inputFilter = scanner.nextLine().trim();
            if (inputFilter.length() <= 50 && inputFilter.matches("^[a-zA-Z\\s]+$") && inputFilter != null) {
                titleWithEmployeeList = titleDaoImpl.filterTitleByName(inputFilter);
                break;
            } else {
                System.out.println(YELLOW + "Invalid input. Must be alphabet and 50 characters or fewer and must be not null" + RESET);
            }
        }
        titleWithEmployeeList.forEach(record -> {
            Title title = (Title) record[0];
            String employeeName = (String) record[1];
            table.addCell(String.valueOf(title.getEmployeesId()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(employeeName, new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getTitle()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getFromDate()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(title.getToDate()), new CellStyle(CellStyle.HorizontalAlign.center));
        });
        System.out.println(table.render());


    }

    @Override
    public void updateOnlTitle() {
        Title updateTitle = new Title();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║        \u001B[36m🛠️ Update Only Position\u001B[34m       ║
                ╚══════════════════════════════════════╝\u001B[0m""");
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println(YELLOW + "Input Only Number" + RESET);
            }
        }
        Title oldTitle = titleDaoImpl.searchTitleById(id);
        if (oldTitle != null) {
            while (true){
                System.out.print("> Please Input New Title: ");
                scanner.nextLine();
                String inputNewTitle = scanner.nextLine().trim();
                if (inputNewTitle.length() <= 50 && inputNewTitle.matches("^[a-zA-Z\\s]+$") && inputNewTitle != null){
                    updateTitle.setTitle(inputNewTitle);
                    titleDaoImpl.updateOnlyTitleById(id,inputNewTitle);
                    break;
                } else {
                    System.out.println(YELLOW + "Invalid input. Must be alphabet and 40 characters or fewer." + RESET);
                }
            }
        } else {
            System.out.println(RED + "Title not found with ID: " + id + RESET);
        }
    }

    @Override
    public void updateOnlyFromDate() {
        Title updateTitle = new Title();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║        \u001B[36m📅 Update Only From Date\u001B[34m      ║
                ╚══════════════════════════════════════╝\u001B[0m """);
        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println(YELLOW + "Input Only Number" + RESET);
            }
        }
        Title oldTitle = titleDaoImpl.searchTitleById(id);
        if (oldTitle != null) {
            while (true){
                System.out.print("> Please Input New From Date: ");
                scanner.nextLine();
                String inputFromDate = scanner.nextLine().trim();

                    try {
                        LocalDate localDate = LocalDate.parse(inputFromDate);
                        Date sqlDate = Date.valueOf(localDate); // Convert to java.sql.Date

                        // Update the title with new from_date
                        updateTitle.setFromDate(sqlDate);
                        titleDaoImpl.updateOnlyFromDateById(id, sqlDate);
                        break;
                    } catch (DateTimeParseException e) {
                        System.out.println(YELLOW + "❌ Invalid date format. Please use YYYY-MM-DD." + RESET);
                    }
            }
        } else {
            System.out.println(RED + "Title not found with ID: " + id + RESET);
        }
    }

    @Override
    public void updateOnlyToDate() {
        Title updateTitle = new Title();
        System.out.println("""
                \u001B[34m╔══════════════════════════════════════╗
                ║   \u001B[36m📆 Update Only To Date\u001B[34m             ║
                ╚══════════════════════════════════════╝\u001B[0m  """);

        while (true) {
            System.out.print("> Please Input Employee ID: ");
            try {
                id = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println(YELLOW + "Input Only Number" + RESET);
            }
        }
        Title oldTitle = titleDaoImpl.searchTitleById(id);
        if (oldTitle != null) {
            while (true){
                System.out.print("> Please Input New To Date: ");
                scanner.nextLine();
                String inputFromDate = scanner.nextLine().trim();

                try {
                    LocalDate localDate = LocalDate.parse(inputFromDate);
                    Date sqlDate = Date.valueOf(localDate); // Convert to java.sql.Date

                    // Update the title with new from_date
                    updateTitle.setToDate(sqlDate);
                    titleDaoImpl.updateOnlyToDateById(id, sqlDate);
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println(YELLOW + "❌ Invalid date format. Please use YYYY-MM-DD." + RESET);
                }
            }
        } else {
            System.out.println(RED + "Title not found with ID: " + id + RESET);

        }
    }
}
