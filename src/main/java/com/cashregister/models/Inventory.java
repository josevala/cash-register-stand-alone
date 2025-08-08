package com.cashregister.models;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
public class Inventory {
   private int totalItems;
   private List<HashMap<Product,Double>> inventory;
   private Date lastUpdate;

    public Inventory(){}
    public Inventory(List<HashMap<Product,Double>> inventory, Date lastUpdate, int totalItems) {
        this.inventory = inventory;
        this.lastUpdate = lastUpdate;
        this.totalItems = totalItems;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<HashMap<Product,Double>> getInventory() {
        return inventory;
    }

    public void setInventory(List<HashMap<Product,Double>> inventory) {
        this.inventory = inventory;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
