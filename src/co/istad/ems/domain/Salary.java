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
public class Salary {
    private Integer employeesId;
    private Date fromDate;
    private Integer amount;
    private Date toDate;
}
