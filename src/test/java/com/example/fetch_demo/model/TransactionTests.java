package com.example.fetch_demo.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootTest
public class TransactionTests {

    @Test
    void canGetTransactionVariables() {
        String payer = "TEST1";
        int points = 100;
        Date time = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transaction(payer, points, time);
        Assertions.assertEquals(payer, transaction.getPayer());
        Assertions.assertEquals(points, transaction.getPoints());
        Assertions.assertEquals(points, transaction.getPoints());
    }

    @Test
    void DateTimeCannotBeNullOrZero() {
        String payer = "TEST1";
        int points = 100;
        Date time = new Timestamp(System.currentTimeMillis());
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(null, points, time));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(payer, points, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Transaction(payer, 0, time));
    }

    @Test
    void payerMustContainAlphanumericChars() {

    }

    @Test
    void payerBecomesCapitalized() {
        String payer = "test1";
        int points = 100;
        Date time = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transaction(payer, points, time);
        Assertions.assertNotEquals("test1", transaction.getPayer());
        Assertions.assertEquals("TEST1", transaction.getPayer());
    }

}
