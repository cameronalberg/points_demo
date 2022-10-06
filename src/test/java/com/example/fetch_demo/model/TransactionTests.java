package com.example.fetch_demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@SpringBootTest
public class TransactionTests {
    String payer;
    int points;
    Instant time;

    @BeforeEach
    void setup() {
        payer = "TEST1";
        points = 100;
        time = Instant.now();
    }

    @Test
    void canGetTransactionVariables() {
        Transaction transaction = new Transaction(payer, points, time);
        Assertions.assertEquals(payer, transaction.getPayer());
        Assertions.assertEquals(points, transaction.getPoints());
        Assertions.assertEquals(points, transaction.getPoints());
        System.out.println(transaction.getTimestamp());
    }

    @Test
    void DateTimeCannotBeNullOrZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(null, points, time));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(payer, points, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(payer, 0, time));
    }

    @Test
    void payerMustContainAlphanumericChars() {
        Transaction transaction = new Transaction(payer, points, time);
        Assertions.assertEquals(payer, transaction.getPayer());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction("", points, time));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(" ", points, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction("$!@(#$", 0, time));
    }

    @Test
    void payerBecomesCapitalized() {
        Transaction transaction = new Transaction(payer, points, time);
        Assertions.assertNotEquals("test1", transaction.getPayer());
        Assertions.assertEquals("TEST1", transaction.getPayer());
    }

    @Test
    void transactionsCompareCorrectly() {
        Instant time_older = time.minus(1, ChronoUnit.HOURS);
        Instant time_newer = time.plus(5, ChronoUnit.MINUTES);
        Transaction t = new Transaction(payer, points, time);
        Transaction t_same = new Transaction(payer, points, time);
        Transaction t_older = new Transaction(payer, points, time_older);
        Transaction t_newer = new Transaction(payer, points, time_newer);
        Assertions.assertTrue(t.compareTo(t_older) > 0);
        Assertions.assertTrue(t.compareTo(t_newer) < 0);
        Assertions.assertEquals(0, t.compareTo(t_same));
    }

    @Test
    void checkEquality() {
        Instant time_older = time.minus(1, ChronoUnit.HOURS);
        Transaction t = new Transaction(payer, points, time);
        Transaction t_same = new Transaction(payer, points, time);
        Transaction t_older = new Transaction(payer, points, time_older);
        Assertions.assertEquals(t, t_same);
        Assertions.assertEquals(t, t);
        Assertions.assertNotEquals(t, t_older);
    }

}
