package com.cashregister.services;

import com.cashregister.Interfaces.TransactionsDao;
import com.cashregister.models.DaoException;
import com.cashregister.models.Product;
import com.cashregister.models.Transaction;
import com.cashregister.models.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.cashregister.services.JdbcProduct.mapRowToProduct;

@Component
public class JdbcTransaction implements TransactionsDao {
 private final JdbcTemplate jdbcTemplate;
 @Autowired
  public JdbcTransaction(JdbcTemplate jdbcTemplate) {
     this.jdbcTemplate = jdbcTemplate;
  }

    @Override
    public Transaction getTransactionById(int id) {
        Transaction transaction = new Transaction();
        HashMap<TransactionItem, Double> transactionProducts = new HashMap<>();
        String sqlTransaction = "SELECT * FROM transactions WHERE transactions.transaction_id = ?";
        String sqlProducts = "SELECT p.sku, p.name,p.price, t.transaction_id, ti.quantity FROM product p JOIN transaction_item ti ON p.sku = ti.product_sku" +
                " JOIN transactions t ON ti.id = t.transaction_id WHERE t.transaction_id = ? ;";
        BigDecimal total =  new BigDecimal(0);
            try{
                SqlRowSet productResults =  jdbcTemplate.queryForRowSet(sqlProducts, id);
                while(productResults.next()){
                    TransactionItem product = mapTransItem(productResults);
                    transactionProducts.put(product, productResults.getDouble("quantity"));
                    total = total.add(product.getPrice().multiply(BigDecimal.valueOf(productResults.getDouble("quantity"))));
                }
                SqlRowSet transactionResults = jdbcTemplate.queryForRowSet(sqlTransaction, id);
                while(transactionResults.next()){
                    transaction.setTransactionId(id);
                    transaction.setTransactionTotal(total);
                    transaction.setTransactProducts(transactionProducts);
                    transaction.setTransactionDate(transactionResults.getDate("transaction_date"));
                }
            } catch (CannotGetJdbcConnectionException e){
                throw new DaoException("Unable to connect to server");
            }catch (DataIntegrityViolationException e){
                throw new DaoException("Data Integrity Violation");
            }
     return  transaction;
    }
    @Override
    public List<Transaction> getTransactionsByTotal(BigDecimal total) {
       List<Transaction> transactions = new ArrayList<>();
       String  sqlTransaction = "SELECT transaction_id FROM transactions WHERE transaction_total = ?;";
       try{
           SqlRowSet results = jdbcTemplate.queryForRowSet(sqlTransaction, total);
           while(results.next()){
               Transaction transaction = new Transaction();
               transaction = getTransactionById(results.getInt("transaction_id"));
               transactions.add(transaction);
           }

       } catch (CannotGetJdbcConnectionException e){
           throw new DaoException("Unable to connect to server");
       }catch (DataIntegrityViolationException e){
           throw new DaoException("Data Integrity Violation");
       }
        return transactions;
    }


    @Override
    public List<Transaction> getTransactionsByDate(Date date) {
        List<Transaction> transactions = new ArrayList<>();
        String  sqlTransaction = "SELECT transaction_id FROM transactions WHERE transaction_date = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlTransaction, date);
            while(results.next()){
                Transaction transaction = new Transaction();
                transaction = getTransactionById(results.getInt("transaction_id"));
                transactions.add(transaction);
            }

        } catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
        return  transactions;
    }

    @Override
    public Transaction newTransaction(Transaction transaction){
     Transaction transaction1 = new Transaction();
     String sqlTransactions = "INSERT INTO transactions VALUES (?, ?, ?);";
     String sqlTransItem = "INSERT INTO transaction_items VALUES (?, ?, ?);";

     return transaction1;
    }

    @Override
    public Transaction updateTransaction(Transaction transaction) {
        return null;
    }

    @Override
    public int deleteTransactionById(int id) {
        return 0;
    }

    public static TransactionItem mapTransItem (SqlRowSet results){
     TransactionItem transactionItem = new TransactionItem();
     transactionItem.setPrice(results.getBigDecimal("price"));
     transactionItem.setQuantity(results.getDouble("quantity"));
     transactionItem.setSku(results.getString("sku"));
     transactionItem.setTransactionId(results.getInt("transaction_id"));
     return transactionItem;
    }


}