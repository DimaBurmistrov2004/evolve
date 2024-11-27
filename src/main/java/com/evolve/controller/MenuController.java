package com.evolve.controller;

import com.evolve.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/menu")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @RequestMapping("/page1")
    public ResponseEntity<String> getPage1() {
        return ResponseEntity.ok(menuService.getPageContent("page1"));
    }

    @RequestMapping("/page2")
    public ResponseEntity<String > getPage2() {
        return ResponseEntity.ok(menuService.getPageContent("page2"));
    }

    @RequestMapping("/page3")
    public ResponseEntity<String> getPage3() {
        return ResponseEntity.ok(menuService.getPageContent("page3"));
    }
}
