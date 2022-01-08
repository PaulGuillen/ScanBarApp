package com.example.scanbarapp.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.scanbarapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetailedProductActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    DatabaseReference db;
    Button Enviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_product);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();


        TextView Pcodbar = findViewById(R.id.Scanbarcode);
        EditText Pnamepro = findViewById(R.id.NameProduct);
        EditText Ppurchasepro = findViewById(R.id.Purchaseproduct);
        EditText Psellpro = findViewById(R.id.Sellproduct);
        TextView Pdatepro = findViewById(R.id.Dateproduct);
        EditText Ptypepro = findViewById(R.id.Typeproduct);
        EditText Pquantitypro = findViewById(R.id.Quantityproduct);
        EditText Ppackaging = findViewById(R.id.PackagingProduct);

        String barcode = getIntent().getStringExtra("barcode");
        String namepro = getIntent().getStringExtra("namepro");
        String purchasepro = getIntent().getStringExtra("purchasepro");
        String sellpro = getIntent().getStringExtra("sellpro");
        String datepro = getIntent().getStringExtra("datepro");
        String typepro = getIntent().getStringExtra("typepro");
        String quantitypro = getIntent().getStringExtra("quantitypro");
        String packagingpro = getIntent().getStringExtra("productPackaging");
        String productTime = getIntent().getStringExtra("productTime");
        String productDescription = getIntent().getStringExtra("productDescription");

        Pcodbar.setText(barcode);
        Pnamepro.setText(namepro);
        Ppurchasepro.setText(purchasepro);
        Psellpro.setText(sellpro);
        Pdatepro.setText( "Fecha de Registro: "+ datepro);
        Ptypepro.setText(typepro);
        Pquantitypro.setText(quantitypro);
        Ppackaging.setText(packagingpro);


        Enviar = findViewById(R.id.btn_enviar);
        Enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcodetext = Pcodbar.getText().toString();
                String nameprotext = Pnamepro.getText().toString();
                String purchasetext = Ppurchasepro.getText().toString();
                String sellprotext = Psellpro.getText().toString();
                String typeprotext = Ptypepro.getText().toString();
                String quantityprotext = Pquantitypro.getText().toString();
                double packagingprotext = Double.parseDouble(Ppackaging.getText().toString());

                DatabaseReference customerRef = db.child("BackUp").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("Product").child(barcodetext);
                Map<String, Object> customerMap = new HashMap<>();
                customerMap.put("productName", nameprotext);
                customerMap.put("productPurchase", purchasetext);
                customerMap.put("productSell", sellprotext);
                customerMap.put("productType", typeprotext);
                customerMap.put("productQuantity", quantityprotext);
                customerMap.put("productPackaging", packagingprotext);
                customerMap.put("productScanNumber", barcode);
                customerMap.put("currentTime", productTime);
                customerMap.put("currentDate", datepro);
                customerMap.put("productDescription", productDescription);

                customerRef.setValue(customerMap);


                firestore.collection("ProductsSaved")
                        .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products").document(barcode).update(customerMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>(){

                            @Override
                            public void onSuccess(Void unused) {
                                new SweetAlertDialog(DetailedProductActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(R.string.producto_update).show();
                            }
                        });
            }
        });

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailedProductActivity.this, ScanItemActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });



    }



    @Override
    protected void onStart() {
        super.onStart();
    }
}