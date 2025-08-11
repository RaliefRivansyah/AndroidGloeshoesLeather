




package com.example.javacartproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class InvoiceActivity extends AppCompatActivity {
    private CartAdapter adapter;
    private TextView totalLabel;
    private TextView nomorInvoice;
    private TextView tanggalInvoice;
    private TextView totalHarga;
    private FirebaseDatabase firebaseDatabase;
    private String dateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invoice_list);
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Inisialisasi Views
        totalHarga = findViewById(R.id.total);
        nomorInvoice = findViewById(R.id.nomorInvoice);
        tanggalInvoice = findViewById(R.id.tanggal);
        totalLabel = findViewById(R.id.total);
        ListView cartList = findViewById(R.id.prod_list);

        // total harga dari Intent
        Intent intent = getIntent();
        String str = intent.getStringExtra("key");
        totalHarga.setText(str);

        // nomor invoice acak
        Random rand = new Random();
        int randInt = rand.nextInt(1000);
        nomorInvoice.setText("Nomor Invoice : GL-" + randInt);

        // Mengatur tanggal
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd-MMM-yyyy");
        dateTime = simpleDateFormat.format(calendar.getTime());
        tanggalInvoice.setText("Tanggal : " + dateTime);

        // Inisialisasi adapter dan set ke ListView
        adapter = new CartAdapter(this, R.layout.invoice_dynamic_cart_list, CartStaticArray.cartProducts, totalLabel);
        cartList.setAdapter(adapter);

        // Menyimpan data ke Firebase
        saveInvoiceToFirebase();
    }

    private void saveInvoiceToFirebase() {
        String invoiceNumber = nomorInvoice.getText().toString();
        String date = tanggalInvoice.getText().toString();
        String total = totalHarga.getText().toString();

        // data untuk invoice
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("invoice", invoiceNumber);
        invoice.put("tanggal", date);
        invoice.put("totalHarga", total);

        // Referensi ke lokasi baru di Firebase untuk invoice
        firebaseDatabase.getReference().child("invoices").push().setValue(invoice)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(InvoiceActivity.this, "Silahkan Screenshot Invoice", Toast.LENGTH_SHORT).show();

                        // Toast notifikasi
                        Toast.makeText(InvoiceActivity.this, "Keranjang belanja akan otomatis terhapus", Toast.LENGTH_SHORT).show();

                        //  Detik
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                deleteCartAndNavigateBack();
                            }
                        }, 20000);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InvoiceActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void deleteCartAndNavigateBack() {
        // Hapus item setelah disimpan
        CartStaticArray.cartProducts.clear();
        adapter.notifyDataSetChanged();

        // Navigasi kembali ke CartActivity (opsional)
        Intent intent = new Intent(InvoiceActivity.this, CartActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveProductsToFirebase(String invoiceNumber) {
        // Loop melalui produk dalam CartStaticArray.cartProducts
        for (Product product : CartStaticArray.cartProducts) {
            Map<String, Object> productData = new HashMap<>();

            String modifiedProductName = product.getName();
            double modifiedHargaName = product.getPrice();


            productData.put("Invoice", invoiceNumber);
            productData.put("NamaProduk", modifiedProductName);
            productData.put("Harga", modifiedHargaName);

            firebaseDatabase.getReference().child("invoice_products").push().setValue(productData)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            // Opsional: Tambahkan tindakan sukses jika diperlukan
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(InvoiceActivity.this, "Error menyimpan produk: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
}

