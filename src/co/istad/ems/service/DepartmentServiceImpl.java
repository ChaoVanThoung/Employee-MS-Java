package co.istad.ems.service;

import co.istad.ems.dao.DepartmentDaoImpl;
import co.istad.ems.domain.Department;
import co.istad.ems.util.Pagination;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.util.List;
import java.util.Scanner;

public class DepartmentServiceImpl implements DepartmentService {

    Scanner scanner = new Scanner(System.in);
    private final DepartmentDaoImpl departmentDaoImpl = new DepartmentDaoImpl();
    String id;

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    @Override
    public void addDepartment() {
        Department newDepartment = new Department();
        System.out.println("""
             \u001B[34m╔══════════════════════════════════════╗
             ║         \u001B[36m➕ Add New Department\u001B[34m        ║
             ╚══════════════════════════════════════╝\u001B[0m""");

        while (true) {
            System.out.print("> Input department ID (or 'e' to quit): ");
            String idInput = scanner.nextLine().trim();
            if (idInput.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting add department process..." + RESET);
                return;
            }
            newDepartment.setId(idInput);
            if (newDepartment.getId().length() == 4) {
                Department department = departmentDaoImpl.searchDepartmentById(newDepartment.getId());
                if (department != null) {
                    System.out.println("Department already exists!");
                    continue;
                }
                System.out.println("DEBUG: Valid ID accepted"); // Debug
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }

        while (true) {
            System.out.print("Input department name (or 'exit' to quit): ");
            String nameInput = scanner.nextLine().trim();
            System.out.println("DEBUG: Name input: '" + nameInput + "'"); // Debug
            if (nameInput.equalsIgnoreCase("exit")) {
                System.out.println(YELLOW + "Exiting add department process..." + RESET);
                return;
            }
            newDepartment.setDepartmentName(nameInput);
            if (newDepartment.getDepartmentName().length() <= 40 && newDepartment.getDepartmentName().matches("^[a-zA-Z]+$")) {
                System.out.println("DEBUG: Valid name accepted"); // Debug
                break;
            } else {
                System.out.println("Invalid input. Must be alphabet and 40 characters or fewer.");
            }
        }

        departmentDaoImpl.insertNewDepartment(newDepartment);
    }

    @Override
    public Pagination<List<Department>> getAllDepartments(int page, int size) {
        System.out.println("""
         \u001B[34m╔═══════════════════════════════════════════════════╗
         ║                \u001B[36m📋 Show All Departments\u001B[34m            ║
         ╚═══════════════════════════════════════════════════╝\u001B[0m""");

        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columNames[] = {"Department ID","Department Name"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }
        List<Department> allDepartment = departmentDaoImpl.queryAllDepartments();

        // Ensure valid page and size
        if (size <= 0) size = 10; // Default page size if invalid
        if (page < 1) page = 1;   // Ensure page is at least 1

        Pagination<List<Department>> data = new Pagination<>();
        int totalRecords = allDepartment.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);

        // Handle cases where page exceeds total pages
        if (page > totalPages) page = totalPages;

        // Calculate pagination indices
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalRecords);

        List<Department> paginatedDepartments= allDepartment.subList(fromIndex, toIndex);
        for (Department department : paginatedDepartments) {
            table.addCell(department.getId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
        }

        System.out.println(table.render());

        // Set Pagination Data
        data.setData(paginatedDepartments);
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
                getAllDepartments(requestedPage, size);
            } else {
                System.out.println("Invalid page number. Try again.");
                getAllDepartments(page, size);
            }
        } else {
            switch (option.toUpperCase()) {
                case "P" -> {
                    if (page > 1) {
                        getAllDepartments(page - 1, size);
                    } else {
                        System.out.println("You are already on the first page.");
                        getAllDepartments(page, size);
                    }
                }
                case "N" -> {
                    if (page < totalPages) {
                        getAllDepartments(page + 1, size);
                    } else {
                        System.out.println("You are already on the last page.");
                        getAllDepartments(page, size);
                    }
                }
                case "E" -> System.out.println("Exiting pagination...");
                default -> {
                    System.out.println("Invalid option. Try again.");
                    getAllDepartments(page, size);
                }
            }
        }

        return data;
    }

    @Override
    public Department findDepartmentById() {
        Table table = new Table(2, BorderStyle.UNICODE_BOX_DOUBLE_BORDER, ShownBorders.ALL);
        String columnNames[] = {"Department ID", "Department Name"};
        for (int i = 0; i < columnNames.length; i++) {
            table.addCell(columnNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i, 25, 40);
        }

        System.out.println("""
             \u001B[34m╔══════════════════════════════════════╗
             ║             \u001B[36m🔍 Search By ID\u001B[34m          ║
             ╚══════════════════════════════════════╝\u001B[0m """);

        String id; // Assuming id is a String based on your code
        while (true) {
            System.out.print("> Input department ID (or 'e' to quit): ");
            id = scanner.nextLine().trim(); // Trim to remove extra spaces
            if (id.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting search process..." + RESET);
                return null; // Return null to exit
            }
            if (id.length() == 4) {
                break; // Break if input is 4 characters
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentDaoImpl.searchDepartmentById(id);
        if (department != null) {
            table.addCell(department.getId(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(department.getDepartmentName(), new CellStyle(CellStyle.HorizontalAlign.center));
            System.out.println(table.render());
        } else {
            System.out.println("Department not found");
        }
        return department;
    }

    @Override
    public void deleteDepartmentById() {
        System.out.println("""
             \u001B[34m╔══════════════════════════════════════╗
             ║             \u001B[36m🗑️ Delete By ID\u001B[34m          ║
             ╚══════════════════════════════════════╝\u001B[0m""");

        String id;
        while (true) {
            System.out.print("> Input department ID (or 'e' to quit): ");
            id = scanner.nextLine().trim(); // Capture input and trim spaces
            if (id.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting delete process..." + RESET);
                return; // Exit the method
            }
            if (id.length() == 4) {
                break; // Break if input is 4 characters
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentDaoImpl.searchDepartmentById(id);
        if (department != null) {
            departmentDaoImpl.deleteDepartmentById(id);
        } else {
            System.out.println("Department not found");
        }
    }

    @Override
    public void updateDepartmentById() {
        Department updatedDepartment = new Department();
        System.out.println("""
          \u001B[34m╔══════════════════════════════════════╗
          ║            \u001B[36m⚙️ Update By ID\u001B[34m           ║
          ╚══════════════════════════════════════╝\u001B[0m""");

        String id; // Assuming id is a String
        while (true) {
            System.out.print("> Input department ID For Search (or 'e' to quit): ");
            id = scanner.nextLine().trim();
            if (id.equalsIgnoreCase("e")) {
                System.out.println(YELLOW + "Exiting update process..." + RESET);
                return; // Exit the method
            }
            if (id.length() == 4) {
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department oldDepartment = departmentDaoImpl.searchDepartmentById(id);
        if (oldDepartment != null) {
            while (true) {
                System.out.print("Input new department name (or 'e' to quit): ");
                String nameInput = scanner.nextLine().trim();
                if (nameInput.equalsIgnoreCase("e")) {
                    System.out.println(YELLOW + "Exiting update process..." + RESET);
                    return; // Exit the method
                }
                updatedDepartment.setDepartmentName(nameInput);
                if (updatedDepartment.getDepartmentName().length() <= 40 && updatedDepartment.getDepartmentName().matches("^[a-zA-Z\\s]+$")) {
                    departmentDaoImpl.updateDepartmentById(id, updatedDepartment);
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
