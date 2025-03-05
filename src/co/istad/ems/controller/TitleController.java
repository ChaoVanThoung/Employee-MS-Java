package co.istad.ems.controller;

import co.istad.ems.domain.Title;
import co.istad.ems.service.TitleService;
import co.istad.ems.service.TitleServiceImpl;
import co.istad.ems.util.Pagination;

import java.util.List;

public class TitleController {
    private final TitleService titleService = new TitleServiceImpl();

    public void addTitle() {
        titleService.addTitle();
    }
    public Pagination<List<Title>> getAllTitles() {
        return titleService.getTitles(1,10);
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
    public void findTitleByTitle() {
         titleService.filterByTitle();
    }
    public void updateOnlyTitle(){
        titleService.updateOnlTitle();
    }
    public void updateOnlyFromDate(){
        titleService.updateOnlyFromDate();
    }
    public void updateOnlyToDate(){
        titleService.updateOnlyToDate();
    }
}
