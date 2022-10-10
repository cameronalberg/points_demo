package com.example.fetch_demo.controller;

import com.example.fetch_demo.model.Transaction;
import com.example.fetch_demo.model.TransactionManager;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class PointsController{
    private final TransactionManager manager;

    @Autowired
    public PointsController() {
        this.manager = new TransactionManager();
    }

    @PostMapping(path = "/add")
    @CrossOrigin
    public ResponseEntity<Object> addTransaction(@RequestBody Transaction t) {
        System.out.println(t.getTimestamp());
        manager.addTransaction(t);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(path = "/spend")
    @CrossOrigin
    public ResponseEntity<Object> spendPoints(@RequestBody int points) {
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
    @CrossOrigin
    public ResponseEntity<Object> pointBalances() {
        return new ResponseEntity<>(manager.getBalances(),
                HttpStatus.OK);
    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatchException() {
        return new ResponseEntity<>("Type mismatch in request parameters. " +
                "refer to API documentation for correct syntax.",
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void handleUnreadableHTTPMessageException(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or invalid request body");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void handleUnsupportedMethodException(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Provided request method not supported");
    }

}
