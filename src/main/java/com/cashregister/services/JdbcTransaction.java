package com.cashregister.services;

import com.cashregister.Interfaces.TransactionsDao;
import com.cashregister.models.DaoException;
import com.cashregister.models.Transaction;
import com.cashregister.models.TransactionItem;
import org.springframework.beans.factory.annotation.Autowired;
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
                " JOIN transactions t ON ti.transaction_id = t.transaction_id WHERE t.transaction_id = ? ;";
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
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT transaction_id FROM transactions ORDER BY transaction_date;";
         try{
             SqlRowSet transactionResults = jdbcTemplate.queryForRowSet(sql);
             while(transactionResults.next()){
                 Transaction transaction = getTransactionById(transactionResults.getInt("transaction_id"));
                 transactions.add(transaction);
             }
         }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }
         return transactions;
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
    public List<Transaction> getTransactionsByTotalAndDate(BigDecimal total, Date date){
        List<Transaction> transactions = new ArrayList<>();
         String sql = "SELECT transaction_id FROM transactions WHERE transaction_total = ? AND transaction_date = ?;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,total, date);
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
     String sqlTransactions = "INSERT INTO transactions( transaction_total, transaction_date ) VALUES (?, ?) RETURNING transaction_id;";
        try{
            int newTransactionId = jdbcTemplate.queryForObject(sqlTransactions, int.class, transaction.getTransactionTotal(), transaction.getTransactionDate());
            HashMap<TransactionItem, Double > map = transaction.getTransactProducts();
            for (TransactionItem transactionItem : map.keySet()) {
                transactionItem.setTransactionId(newTransactionId);
                addTransactionItem(transactionItem);
            }
            transaction1 = getTransactionById(newTransactionId);


        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }
     return transaction1;
    }
    public void  addTransactionItem(TransactionItem transactionItem){
        String sqlTransactionItems = "INSERT INTO transaction_item ( transaction_id, product_sku, quantity ) VALUES (?, ?, ?);";
         try{
             jdbcTemplate.update(sqlTransactionItems, transactionItem.getTransactionId(), transactionItem.getSku(), transactionItem.getQuantity());
         }catch (DataIntegrityViolationException e){
             throw new DaoException("Data Integrity Violation");
         }catch (CannotGetJdbcConnectionException e){
             throw new DaoException("Unable to connect to server");
         }
    }

    @Override
    public int deleteTransactionById(int id) {
        int rowsAffected = 0;
        String sqlTransaction = "DELETE FROM transactions WHERE transaction_id = ?;";
        String  sqlTransactionItems = "DELETE FROM transaction_item WHERE transaction_id = ?;";
        try{
            jdbcTemplate.update(sqlTransactionItems, id);
            rowsAffected = jdbcTemplate.update(sqlTransaction, id);
            if(rowsAffected == 0){
                throw new DaoException("Transaction id not found");
            }
        }catch (DataIntegrityViolationException e){
            throw new DaoException("Data Integrity Violation");
        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server");
        }
        return rowsAffected;
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