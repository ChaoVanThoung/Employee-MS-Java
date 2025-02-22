package co.stad.ems.dao;

import co.stad.ems.domain.Title;

import java.util.List;

public interface TitleDao {
    List<Title> queryAllTitle();
    void insertTitle(Title newTitle);
    void updateTitleById(Integer id, Title updateTitle);
    void deleteTitleById(Integer id);
    Title searchTitleById(Integer id);
}
