package com.cashregister.services;

import com.cashregister.Interfaces.InventoryDao;
import com.cashregister.models.DaoException;
import com.cashregister.models.Inventory;
import com.cashregister.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

@Component
public class JdbcInventory implements InventoryDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcInventory(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HashMap<Product, Double> getInventory() {
    HashMap<Product, Double> inventory = new HashMap<>();
    String sql = "SELECT * FROM product;";
    try{
      SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
      while(results.next()){
          Product product = JdbcProduct.mapRowToProduct(results);
          inventory.put(product,product.getQuantity());
      }
    }catch (CannotGetJdbcConnectionException e){
        throw new DaoException("Unable to connect to server");
    }catch (DataIntegrityViolationException e){
        throw new DaoException("Data Integrity Violation");
    }
    return inventory;
}
    @Override
    public Date getLastUpdate() {
        String sql = "SELECT MAX(last_updated) FROM product;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            if(results.next()){
            Timestamp timestamp = results.getTimestamp("last_updated");
            return timestamp;
            }
        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
        return null;
    }

    @Override
    public int getTotalItems() {
        String sql = "SELECT COUNT(*) FROM product;";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server");
        }
    }

}
