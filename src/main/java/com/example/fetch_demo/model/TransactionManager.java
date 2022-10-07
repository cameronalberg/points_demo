package com.example.fetch_demo.model;

import java.util.*;

public class TransactionManager {

    private final TreeSet<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new TreeSet<>();
    }

    public void addTransaction(Transaction t) {
        //TODO - if transaction has negative points, "spend them" on oldest
        // value and add transaction with originalPoints of -x and
        // availablePoints of 0
        transactions.add(t);
    }

    public Transaction getOldest() {
        Transaction oldest = this.transactions.first();
        return new Transaction(oldest.getPayer(), oldest.getAvailablePoints(),
                oldest.getTimestamp());
    }

    public Map<String, Integer> spendPoints(int spendablePoints) {
        Map<String, Integer> usedPoints = new HashMap<>();
        Iterator<Transaction> itr = transactions.iterator();
        while (spendablePoints > 0 || itr.hasNext()) {
            Transaction item = itr.next();
            //TODO - spend possible points
        }
        return usedPoints;
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
            output.append(item).append("\n");
        }
        return output.toString();
    }


}
