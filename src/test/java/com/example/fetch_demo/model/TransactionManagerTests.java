package com.example.fetch_demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class TransactionManagerTests {
    String payer;
    int points;
    Instant time;
    TransactionManager manager;
    Instant t1_time;
    Transaction t1;
    Instant t2_time;
    Transaction t2;
    Instant t3_time;
    Transaction t3;
    Instant t4_time;
    Transaction t4;
    Instant t5_time;
    Transaction t5;

    @BeforeEach
    void setup() {
        payer = "TEST1";
        points = 100;
        time = Instant.now();
        manager = new TransactionManager();
        t1_time = Instant.parse("2020-11-02T14:00:00Z");
        t1 = new Transaction("DANNON", 1000, t1_time);
        t2_time = Instant.parse("2020-10-31T11:00:00Z");
        t2 = new Transaction("UNILEVER", 200, t2_time);
        t3_time = Instant.parse("2020-10-31T15:00:00Z");
        t3 = new Transaction("DANNON", -200, t3_time);
        t4_time = Instant.parse("2020-11-01T14:00:00Z");
        t4 = new Transaction("MILLER COORS", 10000, t4_time);
        t5_time = Instant.parse("2020-10-31T10:00:00Z");
        t5 = new Transaction("DANNON", 300, t5_time);
    }

    @Test
    void printTransactions() {
        Transaction transaction = new Transaction(payer, points, time);
        Transaction transaction2 = new Transaction(payer, points, time);
        Transaction transaction3 = new Transaction(payer, points,
                time.plus(5, ChronoUnit.MINUTES));
        manager.addTransaction(transaction);
        manager.addTransaction(transaction2);
        manager.addTransaction(transaction3);
        Assertions.assertEquals(2, manager.transactionCount());
    }

    @Test
    void orderIsCorrect() {
        Transaction transaction = new Transaction("DEFAULT", points, time);
        Transaction transaction_newer = new Transaction("NEWER", points,
                time.plus(5, ChronoUnit.MINUTES));

        manager.addTransaction(transaction);
        manager.addTransaction(transaction_newer);
        Assertions.assertEquals(transaction, manager.getOldest());
        Transaction transaction_older = new Transaction("OLDER", points,
                time.minus(5, ChronoUnit.MINUTES));

        manager.addTransaction(transaction_older);
        Assertions.assertEquals(transaction_older, manager.getOldest());
    }

    @Test
    void checkBalances() {
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t4);
        manager.addTransaction(t5);
        String expected = "{UNILEVER=200, MILLER COORS=10000, DANNON=1100}";
        Assertions.assertEquals(expected, manager.getBalances().toString());
    }

    @Test
    void checkTransactions() {
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t4);
        manager.addTransaction(t5);
        String[] expected = {"payer: DANNON, points: 300, timestamp: " +
                "2020-10-31T10:00:00Z", "payer: UNILEVER, points: 200, " +
                "timestamp: 2020-10-31T11:00:00Z", "payer: DANNON, points: " +
                "-200, timestamp: 2020-10-31T15:00:00Z", "payer: MILLER " +
                "COORS, points: 10000, timestamp: 2020-11-01T14:00:00Z",
                "payer: DANNON, points: 1000, timestamp: 2020-11-02T14:00:00Z"};
        List<Transaction> results = manager.getTransactions();
        int i = 0;
        for (Transaction item : results) {
            Assertions.assertEquals(expected[i], item.toString());
            i++;
        }
    }

    @Test
    void checkValidPointSpend() {
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t4);
        manager.addTransaction(t5);
        manager.spendPoints(5000);
        String[] expected = {"payer: DANNON, points: 0, timestamp: " +
                "2020-10-31T10:00:00Z", "payer: UNILEVER, points: 0, " +
                "timestamp: 2020-10-31T11:00:00Z", "payer: DANNON, points: " +
                "0, timestamp: 2020-10-31T15:00:00Z", "payer: MILLER " +
                "COORS, points: 5300, timestamp: 2020-11-01T14:00:00Z",
                "payer: DANNON, points: 1000, timestamp: 2020-11-02T14:00:00Z"};
        List<Transaction> results = manager.getTransactions();
        int i = 0;
        for (Transaction item : results) {
            Assertions.assertEquals(expected[i], item.toString());
            i++;
        }
    }

    @Test
    void checkMultiplePointSpend() {
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t4);
        manager.addTransaction(t5);
        manager.spendPoints(5000);
        manager.spendPoints(5500);
        String[] expected = {"payer: DANNON, points: 0, timestamp: " +
                "2020-10-31T10:00:00Z", "payer: UNILEVER, points: 0, " +
                "timestamp: 2020-10-31T11:00:00Z", "payer: DANNON, points: " +
                "0, timestamp: 2020-10-31T15:00:00Z", "payer: MILLER " +
                "COORS, points: 0, timestamp: 2020-11-01T14:00:00Z",
                "payer: DANNON, points: 800, timestamp: 2020-11-02T14:00:00Z"};
        List<Transaction> results = manager.getTransactions();
        int i = 0;
        for (Transaction item : results) {
            Assertions.assertEquals(expected[i], item.toString());
            i++;
        }
    }

    @Test
    void checkImpossiblePointSpend() {
        manager.addTransaction(t5);
        manager.spendPoints(5000);
        String[] expected = {"payer: DANNON, points: 0, timestamp: " +
                "2020-10-31T10:00:00Z"};
        List<Transaction> results = manager.getTransactions();
        int i = 0;
        for (Transaction item : results) {
            Assertions.assertEquals(expected[i], item.toString());
            i++;
        }
    }

    @Test
    void checkPayerBalanceNotNegative() {
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t5);
        manager.spendPoints(500);
        manager.spendPoints(10000);
        Map<String, Integer> results = manager.getBalances();
        for (int balance : results.values()) {
            Assertions.assertTrue(balance >= 0);
        }
    }

    @Test
    void checkOpsWithoutTransactions() {
        manager.spendPoints(500);
        manager.spendPoints(10000);
        Assertions.assertEquals(0, manager.getBalances().size());
        Assertions.assertEquals(0, manager.getTransactions().size());
    }

}
