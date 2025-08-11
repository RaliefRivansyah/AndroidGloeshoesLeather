package com.example.javacartproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductsListActivity extends MainActivity {
    private ArrayList<Product> prodArray;
    private SQLiteDatabase db;
    ProductTableDataGateway dataGateway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list);

        TextView categoryTitle = findViewById(R.id.category_title);
        ListView prodList = findViewById(R.id.prod_list);
        prodArray = new ArrayList<>();
        Intent intent = getIntent();
        String selectedCategory = intent.getStringExtra("category");
        String databaseName = intent.getStringExtra("databaseName");

        db = SQLiteDatabase.openDatabase(
                getApplicationContext().getDatabasePath(databaseName).getAbsolutePath(),
                null,
                SQLiteDatabase.OPEN_READWRITE
        );

        categoryTitle.setText(selectedCategory);
        ProductsAdapter adapter = new ProductsAdapter(this, R.layout.dynamic_product_list, prodArray);
        prodList.setAdapter(adapter);
        loadProductsFromDatabase(selectedCategory);
    }

    private void loadProductsFromDatabase(String selectedCategory) {
        String[] selectAll = {
                ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY,
                ProductTableDataGateway.PRODUCT_COLUMN_NAME,
                ProductTableDataGateway.PRODUCT_COLUMN_PRICE,
                ProductTableDataGateway.PRODUCT_COLUMN_IMAGE
        };

        String whereCategory = ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY + " = ?";
        String[] category = {selectedCategory};
        try (Cursor cursor = db.query(ProductTableDataGateway.PRODUCT_TABLE_NAME,selectAll,whereCategory,category,null,null,null)) {
            while (cursor.moveToNext()) {
                String prodCategory = cursor.getString(cursor.getColumnIndexOrThrow(ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY));
                String prodName = cursor.getString(cursor.getColumnIndexOrThrow(ProductTableDataGateway.PRODUCT_COLUMN_NAME));
                double prodPrice = cursor.getDouble(cursor.getColumnIndexOrThrow(ProductTableDataGateway.PRODUCT_COLUMN_PRICE));
                int prodImage = cursor.getInt(cursor.getColumnIndexOrThrow(ProductTableDataGateway.PRODUCT_COLUMN_IMAGE));
                prodArray.add(new Product(prodCategory, prodName, prodPrice, prodImage));
            }
        }
    }
}