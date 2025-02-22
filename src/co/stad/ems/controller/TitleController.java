package co.stad.ems.controller;

import co.stad.ems.domain.Title;
import co.stad.ems.service.TitleService;
import co.stad.ems.service.TitleServiceImpl;

import java.util.List;

public class TitleController {
    private final TitleService titleService = new TitleServiceImpl();

    public void addTitle() {
        titleService.addTitle();
    }
    public List<Title> getAllTitles() {
        return titleService.getAllTitles();
    }
    public void findById(){
        titleService.findTitleById();
    }
    public void deleteTitle(){
        titleService.deleteTitle();
    }
    public void updateTitle(){
        titleService.updateTitle();
    }
}
