package com.cashregister.models;


import java.math.BigDecimal;

public class TransactionItem {

    private BigDecimal price;
    private double quantity;
    private String sku;
    private int transactionId;
    public TransactionItem() {}
    public TransactionItem( BigDecimal price, double quantity, String sku, int transactionId) {
        this.transactionId = transactionId;
        this.price = price;
        this.quantity = quantity;
        this.sku = sku;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
