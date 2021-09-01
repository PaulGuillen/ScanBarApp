package com.example.scanbarapp.Vista;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.scanbarapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class DelItemActivity extends AppCompatActivity {

    public static EditText ScanBarCode;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_item);

        ScanBarCode = findViewById(R.id.product_scanbar);
        FirebaseAuth.getInstance();

    }
}