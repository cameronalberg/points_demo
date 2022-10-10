package com.example.fetch_demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@SpringBootTest
public class TransactionTests {
    String payer;
    int points;
    String time;
    String time_older;
    String time_newer;
    Transaction t;
    Transaction t_same;
    Transaction t_older;
    Transaction t_newer;

    @BeforeEach
    void setup() {
        payer = "TEST1";
        points = 100;
        time = Transaction.convertTimeStamp(Instant.now());
        time_older = Transaction.convertTimeStamp(Instant.now().minus(1, ChronoUnit.HOURS));
        time_newer = Transaction.convertTimeStamp(Instant.now().plus(5, ChronoUnit.MINUTES));
        t = new Transaction(payer, points, time);
        t_same = new Transaction(payer, points, time);
        t_older = new Transaction(payer, points, time_older);
        t_newer = new Transaction(payer, points, time_newer);
    }

    @Test
    void canGetTransactionVariables() {
        Assertions.assertEquals(payer, t.getPayer());
        Assertions.assertEquals(points, t.getAvailablePoints());
        Assertions.assertEquals(points, t.getAvailablePoints());
        System.out.println(t.getTimestamp());
    }

    @Test
    void DateTimeCannotBeNullOrZero() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(null, points, time));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(payer, points, null));
    }

    @Test
    void payerMustContainAlphanumericChars() {
        Assertions.assertEquals(payer, t.getPayer());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction("", points, time));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(" ", points, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction("$!@(#$", 0, time));
    }

    @Test
    void payerBecomesCapitalized() {
        Assertions.assertNotEquals("test1", t.getPayer());
        Assertions.assertEquals("TEST1", t.getPayer());
    }

    @Test
    void transactionsCompareCorrectly() {
        Assertions.assertTrue(t.compareTo(t_older) > 0);
        Assertions.assertTrue(t.compareTo(t_newer) < 0);
        Assertions.assertEquals(0, t.compareTo(t_same));
    }

    @Test
    void checkEquality() {
        Transaction tDiffName = new Transaction("test3", points, time);
        Assertions.assertEquals(t, t_same);
        Assertions.assertEquals(t, t);
        Assertions.assertNotEquals(t, t_older);
        Assertions.assertNotEquals(t, tDiffName);
    }

    @Test
    void checkOriginalPointsUnmodified() {
        Transaction t = new Transaction(payer, points, time);
        t.setAvailablePoints(points + 100);
        Assertions.assertEquals(points, t.getPoints());
    }

}
