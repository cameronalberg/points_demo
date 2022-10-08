package com.example.fetch_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DocumentationController{

    @RequestMapping(path="/")
    public String root() {
        return "index.html";
    }

    @RequestMapping(path="/usage")
    public String usage() {
        return "index.html";
    }

}
