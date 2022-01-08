package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.scanbarapp.MainActivity;
import com.example.scanbarapp.Modelo.User;
import com.example.scanbarapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private EditText name,email,password;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button BtnRegistrar = findViewById(R.id.btn_registrar);
        BtnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });



        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RegistrationActivity.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void createUser() {

        progressDialog = new ProgressDialog(RegistrationActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        String userName = name.getText().toString();
        String userEmail = email.getText().toString();
        String userPassword = password.getText().toString();


        if (TextUtils.isEmpty(userName)){
            Toast.makeText(this, R.string.campo_nombre, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (TextUtils.isEmpty(userEmail)){
            Toast.makeText(this, R.string.campo_correo, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (TextUtils.isEmpty(userPassword)){
            Toast.makeText(this, R.string.campo_contraseña, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else if (userPassword.length()<6){
            Toast.makeText(this, R.string.contraseña_importante, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else{
            auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        User user = new User(userName,userEmail,userPassword);
                        String UID = task.getResult().getUser().getUid();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        databaseReference.child("Users").child(UID).setValue(user);
                        CleanFields();
                        goToDashBoardMain();
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, R.string.registro_exitoso, Toast.LENGTH_SHORT).show();

                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void CleanFields(){
        name.setText("");
        email.setText("");
        password.setText("");
    }

    private void goToDashBoardMain(){
        Intent i = new Intent(this, DashboardActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}