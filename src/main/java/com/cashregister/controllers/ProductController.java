package com.cashregister.controllers;


import com.cashregister.models.Product;
import com.cashregister.services.JdbcProduct;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {
   private final JdbcProduct jdbcProduct;

    public  ProductController(JdbcProduct jdbcProduct) {
       this.jdbcProduct = jdbcProduct;
    }
  @GetMapping
   public List<Product> getProducts( @RequestParam (required = false)  String name) {
      if (name == null) {
          return jdbcProduct.getAllProducts();
      } else {
          return jdbcProduct.getProductsByname(name);
      }
  }
  @GetMapping("/{sku}")
   public Product getProductBySku(@PathVariable String sku) {

        return jdbcProduct.getProductBySku(sku);

  }
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return jdbcProduct.addProduct(product);
  }

  @ResponseStatus(HttpStatus.OK)
  @PutMapping("/{sku}")
   public Product updateProduct(@PathVariable String sku, @RequestBody Product product) {
         product.setSku(sku);
         return jdbcProduct.updateProduct(product);
  }
  @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{sku}")
    public int deleteProduct(@PathVariable String sku) {
        int rowsAffected = jdbcProduct.deleteProductById(sku);
        return rowsAffected;
  }
}
