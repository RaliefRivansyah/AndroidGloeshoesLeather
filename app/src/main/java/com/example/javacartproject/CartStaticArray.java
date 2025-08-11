package com.example.javacartproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartStaticArray {
    public static ArrayList<Product> cartProducts = new ArrayList<Product>();
    public static void add(Product product) {
        if (cartProducts.contains(product)) {
            int quantity = quantities.get(product);
            quantities.put(product, quantity + 1);
        } else {
            cartProducts.add(product);
            quantities.put(product, 1);
        }
    }

    public static void delete(Product tmpProd) {
        cartProducts.remove(tmpProd);
        quantities.remove(tmpProd);
    }

    public static HashMap<Product, Integer> quantities;
    static {quantities = new HashMap<>();}
}
