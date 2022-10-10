package com.example.fetch_demo.model;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TransactionManager {

    private final TreeSet<Transaction> transactions;

    public TransactionManager() {
        this.transactions = new TreeSet<>();
    }


    public static class usedPointResponse {
        private final String payer;
        private final int points;

        public usedPointResponse(String payer, int usedPoints) {
            this.payer = payer;
            this.points = -1 * usedPoints;
        }

        public String getPayer() {
            return this.payer;
        }

        public int getPoints() {
            return this.points;
        }
    }
    public int addTransaction(Transaction t) {
        int id = -1;
        for (Transaction item : this.transactions) {
            if (item.equals(t)) {
                id = item.getTransactionID();
            }
        }
        if (id < 0) {
            id = this.transactions.size();
            t.setTransactionID(id);
            transactions.add(t);
        }
        return id;
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(this.transactions);
    }

    public Transaction getTransaction(int id) {
        Transaction t = null;
        for (Transaction item : this.transactions) {
            if (item.getTransactionID() == id) {
                t = item;
            }
        }
        return t;
    }

    public Transaction getOldest() {
        Transaction oldest = this.transactions.first();
        return new Transaction(oldest.getPayer(), oldest.getAvailablePoints(),
                oldest.getTimestamp().toString());
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

    public static boolean validatePoints(int points) {
        return points >= 0;
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
