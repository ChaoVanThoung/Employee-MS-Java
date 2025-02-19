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
public class Employees {
    private int id;
    private Date birth_date;
    private String first_name;
    private String last_name;
    private Character gender;
    private Date hire_date;
}
