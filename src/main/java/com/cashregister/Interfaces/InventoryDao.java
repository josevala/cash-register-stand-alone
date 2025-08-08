package com.cashregister.Interfaces;

import com.cashregister.models.Product;

import java.util.Date;
import java.util.HashMap;

public interface InventoryDao {
    HashMap<Product, Double> getInventory();
    Date getLastUpdate();
    int getTotalItems();
}
