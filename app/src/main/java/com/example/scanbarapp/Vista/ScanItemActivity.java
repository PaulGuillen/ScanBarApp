package com.example.scanbarapp.Vista;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.scanbarapp.Controlador.ScanCodeActivitysearch;
import com.example.scanbarapp.Controlador.ViewHolderProduct;
import com.example.scanbarapp.Modelo.Product;
import com.example.scanbarapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ScanItemActivity extends AppCompatActivity {

    public static EditText resultsearcheview;
    ImageButton scantosearch;
    Button searchbtn;
    RecyclerView mrecyclerview;
    private FirebaseAuth auth;
    private DatabaseReference db;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_item);

        firestore = FirebaseFirestore.getInstance();
        resultsearcheview = findViewById(R.id.searchfield);
        scantosearch = findViewById(R.id.imageButtonsearch);
        searchbtn = findViewById(R.id.searchbtnn);
        mrecyclerview = findViewById(R.id.recyclerViews);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mrecyclerview.setLayoutManager(manager);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ScanItemActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        scantosearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ScanCodeActivitysearch.class));
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = resultsearcheview.getText().toString();
                firebasesearch(searchtext);
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onStop() {
        super.onStop();

    }



    private void firebasesearch(String searchtext) {

        String query = searchtext.toLowerCase();

        Query firebaseSearchQuery = db.child("BackUp").child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).child("Product")
                .orderByChild("productScanNumber").startAt(query).endAt(query + "\uf8ff");

        SweetAlertDialog pDialog = new SweetAlertDialog(ScanItemActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading ...");
        pDialog.setCancelable(true);
        pDialog.show();

        FirebaseRecyclerAdapter<Product, ViewHolderProduct> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, ViewHolderProduct>
                (Product.class,
                        R.layout.list_product_inventory,
                        ViewHolderProduct.class,
                        firebaseSearchQuery) {
            @Override
            protected void populateViewHolder(ViewHolderProduct viewHolder, Product model, int position) {

                viewHolder.setDetails(getApplicationContext(), model.getProductScanNumber(), model.getProductName(), model.getProductPurchase()
                        , model.getProductSell(), model.getCurrentDate(), model.getProductType(), model.getProductQuantity(),
                        String.valueOf(model.getProductPackaging()), model.getCurrentTime(), model.getProductDescription());
                        pDialog.dismiss();
            }

            @Override
            public ViewHolderProduct onCreateViewHolder(ViewGroup parent, int viewType) {
                ViewHolderProduct viewHolderProduct =  super.onCreateViewHolder(parent, viewType);
                viewHolderProduct.setOnClickListener(new ViewHolderProduct.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String mScanBarCode = getItem(position).getProductScanNumber();
                        String mNamePro = getItem(position).getProductName();
                        String mPurchasePro = getItem(position).getProductPurchase();
                        String mSellPro = getItem(position).getProductSell();
                        String mDatePro = getItem(position).getCurrentDate();
                        String mTypePro = getItem(position).getProductType();
                        String mQuantityPro = getItem(position).getProductQuantity();
                        String mPackagingPro = String.valueOf(getItem(position).getProductPackaging());
                        String mProductTime = String.valueOf(getItem(position).getCurrentTime());
                        String mDescription = String.valueOf(getItem(position).getProductDescription());

                        Intent intent = new Intent(view.getContext(), DetailedProductActivity.class);
                        intent.putExtra("barcode", mScanBarCode);
                        intent.putExtra("namepro", mNamePro);
                        intent.putExtra("purchasepro", mPurchasePro);
                        intent.putExtra("sellpro", mSellPro);
                        intent.putExtra("datepro", mDatePro);
                        intent.putExtra("typepro", mTypePro);
                        intent.putExtra("quantitypro", mQuantityPro);
                        intent.putExtra("productPackaging", mPackagingPro);
                        intent.putExtra("productTime", mProductTime);
                        intent.putExtra("productDescription", mDescription);

                        startActivity(intent);


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

                return viewHolderProduct;

            }

        };


        mrecyclerview.setAdapter(firebaseRecyclerAdapter);
    }




}

