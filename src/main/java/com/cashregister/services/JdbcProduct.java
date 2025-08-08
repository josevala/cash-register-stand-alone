package com.cashregister.services;

import com.cashregister.Interfaces.ProductDao;
import com.cashregister.models.DaoException;
import com.cashregister.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcProduct implements ProductDao {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcProduct(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public static Product mapRowToProduct(SqlRowSet results)  {
        Product product = new Product();
        product.setName(results.getString("name"));
        product.setSku(results.getString("sku"));
        product.setPrice(results.getBigDecimal("price"));
        product.setQuantity(results.getDouble("quantity"));
        return product;
    }
    
    @Override
    public List<Product> getProductsByname(String name) {
        String sql = "SELECT * FROM product WHERE name LIKE ?;";
        List<Product> products = new ArrayList<>();
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, "%"+ name +"%");
            while (results.next()) {
                Product product = mapRowToProduct(results);
                products.add(product);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
        return products;
    }

    @Override
    public Product getProductBySku(String sku) {
        Product product = null;
        String sql = "SELECT * FROM product WHERE sku = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, sku);
            if (results.next()) {
                 product = mapRowToProduct(results);
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
        return product;
    }

    @Override
    public Product addProduct(Product product) {

        Product newProduct = null;
        String sql = "Insert into product(name, price, quantity, sku, last_updated) values(?,?,?,?, ?);";
        try {
            jdbcTemplate.update(sql, product.getName(), product.getPrice(), product.getQuantity(),
                    product.getSku(),    new Timestamp(System.currentTimeMillis()));
            String selectSql = "SELECT * FROM product WHERE sku = ?";
             SqlRowSet results = jdbcTemplate.queryForRowSet(selectSql, product.getSku());
             if (results.next()) {
                newProduct = mapRowToProduct(results);
             }
        }catch (CannotGetJdbcConnectionException e){
               throw new DaoException("Unable to connect to server");
           }
        return newProduct;
    }

    @Override
    public Product updateProduct(Product product) {
        Product updatedProduct = null;
         String sql ="Update product SET name = ?, price = ?, quantity = ?" +
                 ", sku = ?, last_updated = ? WHERE sku = ?;";
        try{
             int numberOfRowsAffected = jdbcTemplate.update(
              sql,product.getName(),product.getPrice(),product.getQuantity(),
                     product.getSku(),new Timestamp(System.currentTimeMillis()), product.getSku());
            if(numberOfRowsAffected == 0 ){
                throw new DaoException("Product was not updated");
            }else{
                updatedProduct = getProductBySku(product.getSku());
            }
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
        return updatedProduct;
    }

    @Override
    public int deleteProductById(int id) {
        String deleteFromTransaction = "DELETE FROM transaction_item WHERE product_sku = ?;";
        String deleteFromInventory = "DELETE FROM product WHERE sku  = ?;";
        int rowsAffected = 0;
         try {
             jdbcTemplate.update(deleteFromTransaction, id);
             rowsAffected = jdbcTemplate.update(deleteFromInventory, id);

         }catch (CannotGetJdbcConnectionException e){
             throw new DaoException("Unable to connect to server");

         }catch(DataIntegrityViolationException e){
             throw new DaoException("Data Integrity Violation");
         }
        return rowsAffected;
    }
}
