package com.example.fetch_demo.model;

import java.util.*;

public class TransactionManager {

    private final TreeSet<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new TreeSet<>();
    }

    // Transaction is added to set - if transaction has negative points, the
    // points are deducted from the oldest
    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(this.transactions);
    }

    public Transaction getOldest() {
        Transaction oldest = this.transactions.first();
        return new Transaction(oldest.getPayer(), oldest.getAvailablePoints(),
                oldest.getTimestamp());
    }

    public Map<String, Integer> spendPoints(int spendablePoints) {
        Map<String, Integer> usedPoints = new HashMap<>();
        Iterator<Transaction> itr = transactions.iterator();
        while (spendablePoints != 0 && itr.hasNext()) {
            Transaction item = itr.next();
            int availablePoints = item.getAvailablePoints();
            if (availablePoints != 0) {
                int remainingPoints = availablePoints - spendablePoints;
                int spentPoints = spendablePoints;
                if (remainingPoints >= 0) {
                    item.setAvailablePoints(remainingPoints);
                    spendablePoints = 0;
                } else {
                    item.setAvailablePoints(0);
                    spendablePoints = -1 * remainingPoints;
                    spentPoints = availablePoints;
                }
                int points = usedPoints.getOrDefault(item.getPayer(), 0);
                points += spentPoints;
                usedPoints.put(item.getPayer(), points);
            }
        }
        return usedPoints;
    }

    private boolean filterByPayer(Transaction t, String payer) {
        if (payer == null || payer.isBlank()) {
            return true;
        }
        return t.getPayer().equals(payer);
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
