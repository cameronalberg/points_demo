package com.example.fetch_demo.controller;

import com.example.fetch_demo.model.Transaction;
import com.example.fetch_demo.model.TransactionManager;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.boot.web.servlet.error.ErrorController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Object> addTransaction(@RequestParam String payer,
                                                 @RequestParam int points,
                                                 @RequestParam String timestamp) {
        Transaction t;
        try {
            t = new Transaction(payer, points, Instant.parse(timestamp));
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("timestamp could not be parsed.",
                    HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        manager.addTransaction(t);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "/spend")
    @ResponseBody
    public ResponseEntity<Object> spendPoints(@RequestParam int points) {
        if (!TransactionManager.validatePoints(points)) {
            return new ResponseEntity<>("points must be >= 0",
                    HttpStatus.BAD_REQUEST);
        }
        Map<String, Integer> usedPoints = manager.spendPoints(points);
        List<TransactionManager.usedPointResponse> response = new ArrayList<>();
        for (String payer : usedPoints.keySet()) {
            response.add(new TransactionManager.usedPointResponse(payer,
                    usedPoints.get(payer)));
        }
        return new ResponseEntity<>(response,
                HttpStatus.OK);
    }

    @RequestMapping(path = "/balance")
    public ResponseEntity<Object> pointBalances() {
        return new ResponseEntity<>(manager.getBalances(),
                HttpStatus.OK);
    }

    @RequestMapping(path="/error")
    public ResponseEntity<Object> returnError() {
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }

}
