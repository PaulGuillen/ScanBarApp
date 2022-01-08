package com.example.scanbarapp;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scanbarapp.Vista.DashboardActivity;
import com.example.scanbarapp.Vista.RegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText Email, Password;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        Button SignUp = findViewById(R.id.registrar_usuario);
        Button SignIn = findViewById(R.id.iniciar_sesion);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUp();
            }
        });

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignIn();
            }
        });

        if (auth.getCurrentUser() != null) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            startActivity(new Intent(MainActivity.this, DashboardActivity.class));
            finish();
        }

    }


    private void SignIn(){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        String userEmail = Email.getText().toString();
        String userPassword = Password.getText().toString();


        if (TextUtils.isEmpty(userEmail)) {
            Toast.makeText(this, R.string.campo_correo, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else if (TextUtils.isEmpty(userPassword)) {
            Toast.makeText(this, R.string.campo_contraseña, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }else{
            //Logearse
            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        Intent intent = new Intent (MainActivity.this,DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this, R.string.inicio_sesion_exitoso, Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Error" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void SignUp(){
        Intent intent = new Intent (MainActivity.this,RegistrationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
    }

}