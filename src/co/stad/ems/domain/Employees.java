package co.stad.ems.domain;

import java.sql.Date;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Employees {
    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private char gender;
    private Date hireDate;
}
