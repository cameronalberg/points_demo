package com.example.fetch_demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class PointsController implements ErrorController{

    @RequestMapping(path="/")
    public String root() {
        return "root path";
    }

    @RequestMapping(path = "/add")
    public ResponseEntity<Object> addTransaction() {
        return new ResponseEntity<>("not yet implemented",
                HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/spend")
    public ResponseEntity<Object> spendPoints() {
        return new ResponseEntity<>("not yet implemented",
                HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/balance")
    public ResponseEntity<Object> pointBalances() {
        return new ResponseEntity<>("not yet implemented",
                HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path="/error")
    public ResponseEntity<Object> returnError() {
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }
}
