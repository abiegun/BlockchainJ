package com.ajlopez.blockchain.processors;

import com.ajlopez.blockchain.core.Transaction;

import java.util.*;

/**
 * Created by ajlopez on 21/01/2018.
 */
public class TransactionPool {
    private Set<Transaction> transactions = new HashSet<>();

    public void addTransaction(Transaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("Null transaction");

        this.transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        if (transaction == null)
            throw new IllegalArgumentException("Null transaction");

        this.transactions.remove(transaction);
    }

    public boolean containsTransaction(Transaction transaction) {
        return this.transactions.contains(transaction);
    }

    public List<Transaction> getTransactions() {
        List<Transaction> list = new ArrayList<Transaction>();

        list.addAll(this.transactions);

        return list;
    }
}