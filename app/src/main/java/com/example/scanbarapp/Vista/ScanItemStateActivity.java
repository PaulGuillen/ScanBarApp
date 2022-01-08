package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.scanbarapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ScanItemStateActivity extends AppCompatActivity {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    RadioButton Available, NoAvailable;
    FirebaseDatabase database;
    DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item_state);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        db = database.getReference();


        Available = findViewById(R.id.rb_available);
        NoAvailable =  findViewById(R.id.rb_no_available);

        Button SaveStatus = (Button) findViewById(R.id.guardar_estado);
        Button CancelView = (Button) findViewById(R.id.cancelar);

        TextView Pcodbar = findViewById(R.id.scan_item_state_code);


        String barcode = getIntent().getStringExtra("barcode");

        Pcodbar.setText(barcode);

        SaveStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Available.isChecked()){
                    String estado = Available.getText().toString();
                    DocumentReference senderRef = firestore.collection("ProductsSaved")
                            .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products")
                            .document(barcode);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("status", estado);
                    senderRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            new SweetAlertDialog(ScanItemStateActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Estado Actualizado").show();
                        }
                    });

                }else if(NoAvailable.isChecked()){
                    String estado = NoAvailable.getText().toString();
                    DocumentReference senderRef = firestore.collection("ProductsSaved")
                            .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products")
                            .document(barcode);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("status", estado);
                    senderRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            new SweetAlertDialog(ScanItemStateActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Estado Actualizado").show();
                        }
                    });

                }


            }
        });

    }
}