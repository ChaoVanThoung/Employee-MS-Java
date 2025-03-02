package co.stad.ems.Main;

import co.stad.ems.view.DepartmentManagerView;
import co.stad.ems.view.ManageEmployeeInformation;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
//        ManageEmployeeInformation employee = new ManageEmployeeInformation();
//        employee.displayEmployeeInfoManagement(input);
        DepartmentManagerView departmentManager = new DepartmentManagerView();
        departmentManager.displayDepartmentManagerMenu(input);
    }
}