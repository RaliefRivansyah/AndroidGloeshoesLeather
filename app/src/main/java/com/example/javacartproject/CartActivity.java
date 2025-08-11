package com.example.javacartproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class CartActivity extends MainActivity {
    private CartAdapter adapter;
    private TextView totalLabel;
    private Button btnPerbes;
    private Object View;
    private Object v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_list);
        ListView cartList = findViewById(R.id.cart_list);
        totalLabel = findViewById(R.id.total_label);
        btnPerbes = findViewById(R.id.firebase_btn);
        adapter = new CartAdapter(this, R.layout.dynamic_cart_list, CartStaticArray.cartProducts, totalLabel);
        cartList.setAdapter(adapter);

        btnPerbes.setOnClickListener(view -> {
            if (CartStaticArray.quantities.isEmpty()) { // Check if the cart is empty
                Toast.makeText(view.getContext(), "Your cart is empty!", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirm Checkout")
                        .setMessage("Are you sure you want to proceed to the invoice?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            String totalHarga = totalLabel.getText().toString();
                            Intent intent = new Intent(CartActivity.this, InvoiceActivity.class);
                            intent.putExtra("key", totalHarga);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", null) // Simply dismisses the dialog if "No" is clicked
                        .show();
            }
        });
    }
}