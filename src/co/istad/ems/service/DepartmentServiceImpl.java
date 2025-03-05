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



    @Override
    public void addDepartment() {
        Department newDepartment = new Department();
        System.out.println("""
             \u001B[34m╔══════════════════════════════════════╗
             ║         \u001B[36m➕ Add New Department\u001B[34m        ║
             ╚══════════════════════════════════════╝\u001B[0m""");
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
        String columNames[] = {"Department ID","Department Name"};
        for(int i=0;i<columNames.length;i++){
            table.addCell(columNames[i], new CellStyle(CellStyle.HorizontalAlign.center));
            table.setColumnWidth(i,25,40);
        }


        System.out.println("""
             \u001B[34m╔══════════════════════════════════════╗
             ║             \u001B[36m🔍 Search By ID\u001B[34m          ║
             ╚══════════════════════════════════════╝\u001B[0m """);



        while (true){
            System.out.print("> Input department ID: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentDaoImpl.searchDepartmentById(id);
        if (department != null){
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

        while (true){
            System.out.print("> Input department ID: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department department = departmentDaoImpl.searchDepartmentById(id);
        if (department != null){
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

        while (true){
            System.out.print("> Input department ID For Search: ");
            id = scanner.nextLine();
            if (id.length() == 4){
                break;
            } else {
                System.out.println("Input must be 4 characters");
            }
        }
        Department oldDepartment = departmentDaoImpl.searchDepartmentById(id);
        if (oldDepartment != null){
            while (true){
                System.out.println("Input name department name");
                updatedDepartment.setDepartmentName(scanner.nextLine());

                if (updatedDepartment.getDepartmentName().length() <= 40 && updatedDepartment.getDepartmentName().matches("^[a-zA-Z\\s]+$")){
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
