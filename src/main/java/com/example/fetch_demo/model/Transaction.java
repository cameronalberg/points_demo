package com.example.fetch_demo.model;

import java.util.Date;
import java.util.Locale;

public class Transaction {
    private String payer;
    private int points;
    private Date timestamp;

    public Transaction(String payer, int points, Date timestamp) throws IllegalArgumentException {
        this.setPayer(payer);
        this.setPoints(points);
        this.setTimestamp(timestamp);
    }

    public String getPayer() {
        return this.payer;
    }

    public void setPayer(String input) {
        if (input == null) {
            throw new IllegalArgumentException("payer cannot be null");
        }
        else {
            this.payer = input.toUpperCase(Locale.ROOT);
        }
    }

    public int getPoints() {
        return this.points;
    }

    public void setPoints(int points) {
        if (points == 0) {
            throw new IllegalArgumentException("points cannot be 0");
        }
        else {
            this.points = points;
        }
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date input) {
        if (input == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        else {
            this.timestamp = input;
        }
    }

}
