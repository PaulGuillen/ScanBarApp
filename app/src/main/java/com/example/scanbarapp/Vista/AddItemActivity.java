package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanbarapp.Controlador.ScanCodeActivity;
import com.example.scanbarapp.MainActivity;
import com.example.scanbarapp.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddItemActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    @SuppressLint("StaticFieldLeak")
    public static EditText ScanPro;
    EditText NamePro,DescripPro,TypePro,PurchPro,SellPro,QuantPro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        firestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();

        NamePro = findViewById(R.id.product_name);
        DescripPro = findViewById(R.id.product_description);
        TypePro = findViewById(R.id.product_type);
        PurchPro = findViewById(R.id.product_purchase);
        SellPro = findViewById(R.id.product_sell);
        QuantPro = findViewById(R.id.product_quantity);
        ScanPro = findViewById(R.id.product_scanbar);

        Button ScanBarCode = findViewById(R.id.btn_escanear);
        ScanBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivity.class));
            }
        });

        String NameProduct = NamePro.getText().toString();
        String DesProduct = DescripPro.getText().toString();
        String TypProduct = TypePro.getText().toString();
        String PurchProduct = PurchPro.getText().toString();
        String SellProduct = SellPro.getText().toString();
        String QuanProduct = QuantPro.getText().toString();
        String ScanProduct = ScanPro.getText().toString();

        CardView AddItem = findViewById(R.id.addItem);
        AddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(NameProduct) || TextUtils.isEmpty(DesProduct) || TextUtils.isEmpty(TypProduct) || TextUtils.isEmpty(PurchProduct) ||
                        TextUtils.isEmpty(SellProduct) || TextUtils.isEmpty(QuanProduct) || TextUtils.isEmpty(ScanProduct)){
                    Snackbar.make(v, R.string.campos_faltantes, Snackbar.LENGTH_LONG)
                            .show();
                }else{
                    AddToDatabase();
                }


            }
        });

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AddItemActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });


    }

    private void AddToDatabase(){
        String saveCurrentDate , saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();
        cartMap.put("productName",NamePro.getText().toString());
        cartMap.put("productDescription",DescripPro.getText().toString());
        cartMap.put("productType",TypePro.getText().toString());
        cartMap.put("productPurchase",PurchPro.getText().toString());
        cartMap.put("productSell",SellPro.getText().toString());
        cartMap.put("productQuantity",QuantPro.getText().toString());
        cartMap.put("ProductScanNumber",ScanPro.getText().toString());
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);


        firestore.collection("ProductsSaved")
                .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(AddItemActivity.this, R.string.agregar_producto, Toast.LENGTH_SHORT).show();
            }
        });
    }


}