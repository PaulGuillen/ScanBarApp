package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scanbarapp.Controlador.ScanCodeActivityDel;
import com.example.scanbarapp.MainActivity;
import com.example.scanbarapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DelItemActivity extends AppCompatActivity {

    public static TextInputEditText ScanBarCode;
    FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    DatabaseReference db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_item);

        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        ScanBarCode = findViewById(R.id.product_scanbar);
        ImageButton BtnEscanear = findViewById(R.id.btn_escanear);

        CardView DeleteItem = findViewById(R.id.deleteItems);
        DeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteFromDatabase();
            }
        });


        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DelItemActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        BtnEscanear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivityDel.class));
            }
        });


    }

    private void DeleteFromDatabase() {


        if (ScanBarCode.getText().toString().isEmpty()){
            Toast.makeText(DelItemActivity.this,R.string.falta_codigo_barras,Toast.LENGTH_SHORT).show();
        }else{
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE).setTitleText(R.string.estas_seguro)
                    .setContentText("No podras recuperar esta información!").setConfirmText("Si") //Are you sure ?
                                // Won\'t be able to recover this file!
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            String UIDSCANPRO = ScanBarCode.getText().toString().trim();

                            db.child("BackUp").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("Product")
                                    .child(UIDSCANPRO).removeValue();


                            firestore.collection("ProductsSaved").document(Objects.requireNonNull(firebaseAuth.getCurrentUser())
                                    .getUid()).collection("Products")
                                    .document(UIDSCANPRO).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        new SweetAlertDialog(DelItemActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText(R.string.producto_borrado).show();
                                    }
                                }
                            });


                            sDialog.dismissWithAnimation();
                        }
                    }).setCancelText("No").setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismissWithAnimation();
                }
            }).show();
        }

    }
}