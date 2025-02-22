package co.stad.ems.domain;

import lombok.*;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Title {
    private Integer employeesId;
    private String title;
    private Date fromDate;
    private Date toDate;
}
