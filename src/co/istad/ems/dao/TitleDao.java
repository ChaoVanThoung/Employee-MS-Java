package co.istad.ems.dao;

import co.istad.ems.domain.Title;

import java.sql.Date;
import java.util.List;

public interface TitleDao {
    List<Title> queryAllTitle();
    void insertTitle(Title newTitle);
    void updateTitleById(Integer id, Title updateTitle);
    void deleteTitleById(Integer id);
    Title searchTitleById(Integer id);
    List<Object[]> filterTitleByName(String name);
    void updateOnlyTitleById(Integer id,String newTitle);
    void updateOnlyFromDateById(Integer id, Date newFromDate);
    void updateOnlyToDateById(Integer id, Date newToDate);
}
