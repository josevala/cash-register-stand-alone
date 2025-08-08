package com.cashregister.models;

import java.util.List;

public class StoredTransactions {
    private List<Transaction> transactions;
    StoredTransactions() {
    }
    StoredTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    public List<Transaction> getStoredTransactions() {
        return transactions;
    }
    public void setStoredTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
