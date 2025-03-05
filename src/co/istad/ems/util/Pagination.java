package co.istad.ems.util;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pagination <T> {
    private T data;
    private Integer totalPages;
    private Integer totalRecord;
    private int currentPage;
}
