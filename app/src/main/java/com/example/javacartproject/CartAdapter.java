package com.example.javacartproject;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class CartAdapter extends ArrayAdapter<Product> {
    protected Context context;
    protected ArrayList<Product> prod;
    protected int resource;
    protected TextView totalLabel;

    public CartAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects, TextView totalLabel) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        prod = CartStaticArray.cartProducts;
        prod = (ArrayList<Product>)objects;
        this.totalLabel = totalLabel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product tmpProd = this.prod.get(position);
        int quantity = CartStaticArray.quantities.get(tmpProd);

        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        convertView = layoutInflater.inflate(this.resource,parent,false);
        NumberFormat format = NumberFormat.getCurrencyInstance();
        format.setMaximumFractionDigits(0);
        format.setCurrency(Currency.getInstance("IDR"));

        // Initialize UI items
        TextView prod_name = (TextView)convertView.findViewById(R.id.prod_name);
        TextView prod_price = (TextView)convertView.findViewById(R.id.prod_price);
        ImageView prod_img = (ImageView)convertView.findViewById(R.id.prod_img);
        Button del_btn = (Button)convertView.findViewById(R.id.del);
        Button inc_btn = (Button)convertView.findViewById(R.id.increase_btn);
        Button dec_btn = (Button)convertView.findViewById(R.id.decrease_btn);
        TextView quantity_text = (TextView)convertView.findViewById(R.id.quantity_text);

        // Feed UI items
        prod_name.setText(tmpProd.getName());
        prod_img.setImageResource(tmpProd.getImage());
        quantity_text.setText(String.valueOf(quantity));

        double totalPrice = tmpProd.getPrice() * quantity;
        String formattedPrice = format.format(totalPrice);
        prod_price.setText(quantity + " x " + format.format(tmpProd.getPrice()) + " = " + formattedPrice);

        double total = 0;
        for (Product p : prod) {
            total += p.getPrice() * CartStaticArray.quantities.get(p);
        }
        totalLabel.setText("Total : " + format.format(total));

//        del_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                double price = tmpProd.getPrice() * CartStaticArray.quantities.get(tmpProd);
//                CartStaticArray.delete(tmpProd);
//                notifyDataSetChanged();
//                double total = 0;
//                for (Product p : prod) {
//                    total += p.getPrice() * CartStaticArray.quantities.get(p);
//                }
//                totalLabel.setText("Total : " + String.format("%.2f", total));
//            }
//        });

        del_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Confirm Deletion")
                        .setMessage("Apakah anda ingin menghapus item ini ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (CartStaticArray.quantities.containsKey(tmpProd)) {
                                    CartStaticArray.delete(tmpProd); // Ensure delete() also removes from quantities
                                    notifyDataSetChanged();
                                }

                                double total = 0;
                                for (Product p : prod) {
                                    Integer quantity = CartStaticArray.quantities.get(p);
                                    if (quantity != null) {
                                        total += p.getPrice() * quantity;
                                    }
                                }

                                totalLabel.setText("Total : " + String.format("%.2f", total));
                            }
                        })
                        .setNegativeButton("No", null) // Dismisses dialog if "No" is clicked
                        .show();
            }
        });

        inc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuantity = quantity + 1;
                CartStaticArray.quantities.put(tmpProd, newQuantity);
                quantity_text.setText(String.valueOf(newQuantity));
                notifyDataSetChanged();
            }
        });

        dec_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    int newQuantity = quantity - 1;
                    CartStaticArray.quantities.put(tmpProd, newQuantity);
                    quantity_text.setText(String.valueOf(newQuantity));
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Qty tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }
}