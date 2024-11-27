package com.evolve.service;

import org.springframework.stereotype.Service;

@Service
public class MenuService {

    public String getPageContent(String page) {
        switch (page) {
            case "page1":
                return "This is page 1";
            case "page2":
                return "This is page 2";
            case "page3":
                return "This is page 3";
            default:
                return "Page not found";
        }
    }
}
