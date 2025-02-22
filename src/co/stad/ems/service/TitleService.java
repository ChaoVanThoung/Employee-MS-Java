package co.stad.ems.service;

import co.stad.ems.domain.Title;

import java.util.List;

public interface TitleService {
    void addTitle();
    List<Title> getAllTitles();
    void updateTitle();
    void findTitleById();
    void deleteTitle();
}
