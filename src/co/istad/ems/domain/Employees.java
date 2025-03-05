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
public class Employees {
    private Integer id;
    private Date birthDate;
    private String firstName;
    private String lastName;
    private Character gender;
    private Date hireDate;
}
