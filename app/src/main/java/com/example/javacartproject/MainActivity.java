package com.example.javacartproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    ProductTableDataGateway dataGateway;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dataGateway = new ProductTableDataGateway(this);
        db = dataGateway.getWritableDatabase();
        db.delete(ProductTableDataGateway.PRODUCT_TABLE_NAME, null, null);
        SeedDatabase();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        Cursor cursor = db.query(true, ProductTableDataGateway.PRODUCT_TABLE_NAME, new String[]{ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY},
                null, null, null, null, null, null);
        int index = 0;
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(ProductTableDataGateway.PRODUCT_COLUMN_CATEGORY));
            menu.add(Menu.NONE, Menu.FIRST + index, index, category);
            index++;
        }
        cursor.close();
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.cart_button:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            default:
                intent = new Intent(this, ProductsListActivity.class);
                intent.putExtra("category", item.getTitle());
                intent.putExtra("databaseName", dataGateway.getDatabaseName());
                startActivity(intent);
                break;
        }
        return true;
    }
    public void SeedDatabase() {
        Product[] products = {
                new Product("Product", "Dark Chocolate Boots", 1000000, R.drawable.prod1),
                new Product("Product", "Light Brown Boots", 2000000, R.drawable.prod2),
                new Product("Product", "White n Grey Boots", 1500000, R.drawable.prod3),
                new Product("Product", "Dark Brown Boots", 2000000, R.drawable.prod4),
                new Product("Product", "White Low Shoes", 1500000, R.drawable.prod5),
                new Product("Product", "Brown Medium Boots", 2500000, R.drawable.prod6),
                new Product("Product", "Black Heels", 1000000, R.drawable.prod7),
                new Product("Product", "Dark Red High Boots", 1500000, R.drawable.prod8)
        };

        for (Product product : products) {
            ContentValues values = product.getContentValues();
            db.insert(ProductTableDataGateway.PRODUCT_TABLE_NAME, null, values);
        }
    }

    public void to_cart(View view) {
        Intent intent;
        intent = new Intent (this, CartActivity.class);
        startActivity(intent);
    }
}