package co.stad.ems.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentEmployee {
    private Long employeeId;
    private String departmentId;
    private Date fromDate;
    private Date toDate;
}
