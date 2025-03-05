package co.istad.ems.service;

import co.istad.ems.domain.Title;
import co.istad.ems.util.Pagination;

import java.util.List;

public interface TitleService {
    void addTitle();
//    void getAllTitles();
    Pagination<List<Title>> getTitles(int page, int size);
    void updateTitle();
    void findTitleById();
    void deleteTitle();
    void filterByTitle();
    void updateOnlTitle();
    void updateOnlyFromDate();
    void updateOnlyToDate();
}
