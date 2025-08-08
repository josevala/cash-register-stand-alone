package com.cashregister.models;

import java.math.BigDecimal;

public class Product {
  private  String name; private BigDecimal price;  private String sku;
    private double quantity;
    public Product() {}
    public Product(String name, BigDecimal price, String sku, double quantity) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String toString() {
        return  "name: "+ name + ", price: "+ price + ", sku: "+ sku + ", quantity: "+ quantity;
    }
}
