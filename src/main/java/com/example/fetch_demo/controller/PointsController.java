package com.example.fetch_demo.controller;

import com.example.fetch_demo.model.TransactionManager;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.web.servlet.error.ErrorController;

@Controller
public class PointsController implements ErrorController{
    private TransactionManager manager;

    @Autowired
    public PointsController() {
        this.manager = new TransactionManager();
    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Object> handleTypeMismatchException() {
        return new ResponseEntity<>("type mismatch in request parameters. " +
                "refer to API documentation for correct syntax.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMissingRequestParamException() {
        return new ResponseEntity<>("required request parameter not found." +
                "refer to API documentation for correct syntax.",
                HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path="/")
    public String root() {
        return "placeholder for API docs.";
    }

    @RequestMapping(path = "/add")
    public ResponseEntity<Object> addTransaction() {
        return new ResponseEntity<>("not yet implemented",
                HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(path = "/spend")
    @ResponseBody
    public ResponseEntity<Object> spendPoints(@RequestParam int points) {
        if (!TransactionManager.validatePoints(points)) {
            return new ResponseEntity<>("points must be >= 0",
                    HttpStatus.BAD_REQUEST);
        }
        manager.spendPoints(points);
        return new ResponseEntity<>("success!",
                HttpStatus.OK);
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
