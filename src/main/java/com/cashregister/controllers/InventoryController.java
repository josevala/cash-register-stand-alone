package com.cashregister.controllers;

import com.cashregister.models.Product;
import com.cashregister.services.JdbcInventory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
private final JdbcInventory jdbcInventory;
public InventoryController(JdbcInventory jdbcInventory) {
    this.jdbcInventory = jdbcInventory;
}

@GetMapping
   public HashMap<Product, Double > getInventory(){
      return jdbcInventory.getInventory();
    }
@GetMapping("/last-update")
    public Date getLastUpdate(){
       return  jdbcInventory.getLastUpdate();
    }

@GetMapping("/total-items")
    public int getTotalItems(){
    return jdbcInventory.getTotalItems();
    }

}
