package com.cashregister.controllers;

import com.cashregister.models.Transaction;
import com.cashregister.services.JdbcTransaction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final JdbcTransaction jdbcTransaction;
    public TransactionController(JdbcTransaction jdbcTransaction) {
        this.jdbcTransaction = jdbcTransaction;
    }
    @GetMapping("/{transaction_id}")
    public Transaction getTransactionById(@PathVariable int transaction_id){
        return jdbcTransaction.getTransactionById(transaction_id);
    }
    @GetMapping
    public List<Transaction> getTransactions(@RequestParam (required = false) BigDecimal total,
                                             @RequestParam (required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                             Date transaction_date){
        if(total != null && transaction_date != null){
            return jdbcTransaction.getTransactionsByTotalAndDate(total, transaction_date);
        }else if(total != null && transaction_date == null){
            return jdbcTransaction.getTransactionsByTotal(total);
        }else if(transaction_date != null){
            return jdbcTransaction.getTransactionsByDate(transaction_date);
        }else{
            return jdbcTransaction.getTransactions();
        }
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Transaction createTransaction( @RequestBody Transaction transaction){
         Transaction newTransaction = jdbcTransaction.newTransaction(transaction);
         return newTransaction;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{transaction_id}")
    public void deleteTransaction(@PathVariable int transaction_id){
           jdbcTransaction.deleteTransactionById(transaction_id);
    }
}
