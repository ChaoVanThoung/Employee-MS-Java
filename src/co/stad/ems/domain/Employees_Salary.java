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
public class Employees_Salary {
    private Employees employees;
    private Date from_date;
    private Integer amount;
    private Date to_date;
}
