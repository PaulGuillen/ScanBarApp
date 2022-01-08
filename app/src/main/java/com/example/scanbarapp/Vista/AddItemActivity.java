package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanbarapp.Controlador.ScanCodeActivity;
import com.example.scanbarapp.Modelo.Product;
import com.example.scanbarapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddItemActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    @SuppressLint("StaticFieldLeak")
    public static EditText ScanPro;
    TextInputEditText NamePro , DescripPro, TypePro, PurchPro, SellPro, QuantPro, PackPro ;
    private FirebaseAuth auth;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        firestore = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        NamePro = findViewById(R.id.product_name);
        DescripPro = findViewById(R.id.product_description);
        TypePro = findViewById(R.id.product_type);
        PurchPro = findViewById(R.id.product_purchase);
        SellPro = findViewById(R.id.product_sell);
        QuantPro = findViewById(R.id.product_quantity);
        ScanPro = findViewById(R.id.product_scanbar);
        PackPro = findViewById(R.id.product_price_box);


        ImageButton ScanBarCode = findViewById(R.id.btn_escanear);
        ScanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        CardView AddItem = findViewById(R.id.addItem);
        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NamePro.getText().toString().isEmpty() || DescripPro.getText().toString().isEmpty() || TypePro.getText().toString().isEmpty()
                        || PurchPro.getText().toString().isEmpty() || SellPro.getText().toString().isEmpty() || QuantPro.getText().toString().isEmpty()
                        || ScanPro.getText().toString().isEmpty()) {
                    Snackbar.make(v, R.string.campos_faltantes, Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    AddToDatabase();
                }

            }
        });

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddItemActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void AddToDatabase() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        String scannumberpro = ScanPro.getText().toString().trim();
        String descripro = DescripPro.getText().toString();
        String namepro = NamePro.getText().toString();
        String purchasepro =PurchPro.getText().toString() ;
        String quantitypro = QuantPro.getText().toString();
        String sellpro = SellPro.getText().toString();
        String typepro = TypePro.getText().toString();
        float packpro = Float.parseFloat(PackPro.getText().toString());

        Product product = new Product(scannumberpro,descripro,namepro,purchasepro,quantitypro,sellpro,typepro,saveCurrentDate,saveCurrentTime,
                packpro);

        firestore.collection("ProductsSaved").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products")
                .document(scannumberpro)
                .set(product)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new SweetAlertDialog(AddItemActivity.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText(R.string.agregar_producto).show();
                        CleanInputs();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItemActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                    }
                });

        //BackUpInformationTOUSE
        db.child("BackUp").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("Product").child(scannumberpro)
                .setValue(product);


    }

    private void CleanInputs(){
        NamePro.setText("");
        DescripPro.setText("");
        TypePro.setText("");
        PurchPro.setText("");
        SellPro.setText("");
        QuantPro.setText("");
        ScanPro.setText("");
        PackPro.setText("");
    }

}
