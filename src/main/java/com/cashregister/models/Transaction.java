package com.cashregister.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Transaction {
    private HashMap< TransactionItem, Double > transactProducts;
    private Date transactionDate;
    private int transactionId;
    private BigDecimal transactionTotal;
      public Transaction() {}

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Transaction(HashMap<TransactionItem,Double> transactProducts, int transactionId, BigDecimal transactionTotal, Date transactionDate) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.transactProducts = transactProducts;
        this.transactionTotal = transactionTotal;
    }


    public void setTransactProducts(HashMap<TransactionItem,Double> transactProducts) {

        this.transactProducts = transactProducts;
    }

    public void setTransactionId(int transactionId) {

        this.transactionId = transactionId;
    }

    public void setTransactionTotal(BigDecimal transactionTotal) {

        this.transactionTotal = transactionTotal;
    }

    public HashMap<TransactionItem,Double> getTransactProducts() {

        return transactProducts;
    }

    public int getTransactionId()
    {
        return transactionId;
    }

    public BigDecimal getTransactionTotal() {

        return transactionTotal;
    }
}
