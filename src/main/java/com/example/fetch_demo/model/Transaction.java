package com.example.fetch_demo.model;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class Transaction implements Comparable<Transaction>{
    private String payer;
    private int availablePoints;
    private final int originalPoints;
    private Instant timestamp;

    public Transaction(String payer, int points, String timestamp)
            throws IllegalArgumentException {
        this.setPayer(payer);
        this.setAvailablePoints(points);
        this.setTimestamp(timestamp);
        this.originalPoints = points;
    }

    public String getPayer() {
        return this.payer;
    }

    public void setPayer(String input) {
        if (input == null) {
            throw new IllegalArgumentException("payer cannot be null");
        }
        if (!input.matches(".*[a-zA-Z0-9]+.*")) {
            throw new IllegalArgumentException("payer must contain at least " +
                    "one alphanumeric character");
        }
        else {
            this.payer = input.toUpperCase(Locale.ROOT);
        }
    }

    public int getOriginalPoints() {
        return this.originalPoints;
    }

    public int getAvailablePoints() {
        return this.availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String input) {
        if (input == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
        if (input.length() != 20) {
            throw new IllegalArgumentException("timestamp must be exactly 20 characters");
        }
        else {
            Instant converted = Instant.parse(input);
            this.timestamp = converted.truncatedTo(ChronoUnit.SECONDS);
        }
    }

    public static String convertTimeStamp(Instant timestamp) {
        return timestamp.truncatedTo(ChronoUnit.SECONDS).toString();
    }

    @Override
    public String toString() {
        return "payer: " + this.getPayer() + ", points: " + this.getAvailablePoints() +
                ", timestamp: " + this.getTimestamp();
    }

    @Override
    public int compareTo(Transaction o) {
        return this.getTimestamp().compareTo(o.getTimestamp());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Transaction)) return false;
        Transaction compare = (Transaction) o;
        if (!compare.getPayer().equals(this.getPayer())) return false;
        if (!(compare.getAvailablePoints() == this.getAvailablePoints())) return false;
        return compare.getTimestamp().equals(this.getTimestamp());
    }
}
