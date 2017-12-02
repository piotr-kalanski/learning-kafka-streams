package com.datawizards.kafka;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TransactionsGenerator {
    private Random rand = new Random();
    private List<String> consumers = Arrays.asList("Piotrek", "Diana", "Ryszard", "Rafal");

    public BankTransaction generate() {
        return new BankTransaction(
            consumers.get(rand.nextInt(consumers.size())),
            rand.nextInt(100) * 1.0,
            Instant.now().toString()
        );
    }
}
