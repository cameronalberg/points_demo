package com.example.fetch_demo.model;

import java.util.*;

public class TransactionManager {

    private final TreeSet<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new TreeSet<>();
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public Transaction getOldest() {
        Transaction oldest = this.transactions.first();
        return new Transaction(oldest.getPayer(), oldest.getAvailablePoints(),
                oldest.getTimestamp());
    }

    public Map<String, Integer> getBalances() {
        Map<String, Integer> payers = new HashMap<>();
        for (Transaction item : transactions) {
            int points = payers.getOrDefault(item.getPayer(), 0);
            points += item.getAvailablePoints();
            payers.put(item.getPayer(), points);
        }
        return payers;
    }

    public int transactionCount() {
        return this.transactions.size();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Transaction item : transactions) {
            output.append(item + "\n");
        }
        return output.toString();
    }


}
