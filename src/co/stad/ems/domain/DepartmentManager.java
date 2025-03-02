package co.stad.ems.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
@Builder
public class DepartmentManager {
    private Employees employees;
    private String departmentId;
    private String departmentName;
    private Date fromDate;
    private Date toDate;
}
