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
public class Employees_Title {
    private Employees employees;
    private String title;
    private Date from_date;
    public Date to_date;
}
