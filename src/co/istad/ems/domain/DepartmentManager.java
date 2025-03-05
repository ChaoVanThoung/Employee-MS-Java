package co.istad.ems.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentManager {
    private Integer employeeId;
    private String departmentId;
    private Date fromDate;
    private Date toDate;
}
