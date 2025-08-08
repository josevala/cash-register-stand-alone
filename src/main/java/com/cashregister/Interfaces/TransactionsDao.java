package com.cashregister.Interfaces;

import com.cashregister.models.Product;
import com.cashregister.models.Transaction;
import com.cashregister.models.TransactionItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.HashMap;

public interface TransactionsDao {
   Transaction getTransactionById(int id);
   List<Transaction> getTransactionsByTotal(BigDecimal total);
   List<Transaction> getTransactionsByDate(Date date);
   Transaction newTransaction(Transaction transaction);
   TransactionItem addTransactionItems();
   Transaction updateTransaction(Transaction transaction);
   int  deleteTransactionById(int id);
}
