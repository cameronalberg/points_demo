package com.example.fetch_demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class TransactionManagerTests {
    String payer;
    int points;
    Instant time;
    TransactionManager manager;

    @BeforeEach
    void setup() {
        payer = "TEST1";
        points = 100;
        time = Instant.now();
        manager = new TransactionManager();
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
        Instant t1_time = Instant.parse("2020-11-02T14:00:00Z");
        Transaction t1 = new Transaction("DANNON", 1000, t1_time);
        Instant t2_time = Instant.parse("2020-10-31T11:00:00Z");
        Transaction t2 = new Transaction("UNILEVER", 200, t2_time);
        Instant t3_time = Instant.parse("2020-10-31T15:00:00Z");
        Transaction t3 = new Transaction("DANNON", -200, t3_time);
        Instant t4_time = Instant.parse("2020-11-01T14:00:00Z");
        Transaction t4 = new Transaction("MILLER COORS", 10000, t4_time);
        Instant t5_time = Instant.parse("2020-10-31T10:00:00Z");
        Transaction t5 = new Transaction("DANNON", 300, t5_time);
        manager.addTransaction(t1);
        manager.addTransaction(t2);
        manager.addTransaction(t3);
        manager.addTransaction(t4);
        manager.addTransaction(t5);
        String expected = "{UNILEVER=200, MILLER COORS=10000, DANNON=1100}";
        Assertions.assertEquals(expected, manager.getBalances().toString());
    }

}
