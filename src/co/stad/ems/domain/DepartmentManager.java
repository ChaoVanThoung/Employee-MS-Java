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
public class DepartmentManager {
    private Employees employees;
    private Integer department_id;
    private Date from_date;
    private Date to_date;
}
