package com.cashregister.Interfaces;

import com.cashregister.models.Product;

import java.util.List;

public interface ProductDao {
    List<Product> getAllProducts();
    List<Product> getProductsByname(String name);
    Product getProductBySku(String sku);
    Product addProduct(Product product);
    Product updateProduct(Product product);
    int  deleteProductById(String sku);
}
