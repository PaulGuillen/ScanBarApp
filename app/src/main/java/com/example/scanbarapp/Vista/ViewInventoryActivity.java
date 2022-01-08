package com.example.scanbarapp.Vista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scanbarapp.Controlador.ViewHolderInventory;
import com.example.scanbarapp.Controlador.ViewHolderProduct;
import com.example.scanbarapp.MainActivity;
import com.example.scanbarapp.Modelo.Product;
import com.example.scanbarapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewInventoryActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    RecyclerView mrecyclerview;
    private FirebaseFirestore firestore;
    private FirestoreRecyclerAdapter adapter;
    private TextView totalproducts;
    private TextView totalpurchase;

    Button searchbtn;
    EditText edt_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_inventory);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        mrecyclerview = findViewById(R.id.recyclerViews);
        totalproducts = findViewById(R.id.total_products);
        totalpurchase = findViewById(R.id.total_purchase);
        searchbtn = findViewById(R.id.search);
        edt_search = findViewById(R.id.edt_search);

        ImageButton BackToHome = findViewById(R.id.back_to_home);
        BackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewInventoryActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtext = edt_search.getText().toString();

            }
        });


        AllProcess();
    }



    private void AllProcess() {


        Query query = firestore.collection("ProductsSaved")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products");
        FirestoreRecyclerOptions<Product> options = new FirestoreRecyclerOptions.Builder<Product>().setQuery(query, Product.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Product, ViewHolderInventory>(options) {

            @NonNull
            @Override
            public ViewHolderInventory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_product, parent, false);
                return new ViewHolderInventory(view);


            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolderInventory holder, @SuppressLint("RecyclerView") int position, @NonNull Product model) {

                SweetAlertDialog pDialog = new SweetAlertDialog(ViewInventoryActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Loading ...");
                pDialog.setCancelable(true);
                pDialog.show();

                holder.setDetails(getApplicationContext(), model.getProductScanNumber(), model.getProductName()
                        , model.getProductPurchase()
                        , model.getProductType(), model.getProductQuantity());


                pDialog.dismiss();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mScanBarCode = getItem(position).getProductScanNumber();
                        String mNamePro = getItem(position).getProductName();
                        String mSellPro = getItem(position).getProductPurchase();
                        String mTypePro = getItem(position).getProductType();
                        String mQuantityPro = getItem(position).getProductQuantity();

                        Intent intent = new Intent(v.getContext(), ScanItemStateActivity.class);
                        intent.putExtra("barcode", mScanBarCode);
                        intent.putExtra("namepro", mNamePro);
                        intent.putExtra("purchasepro", mSellPro);
                        intent.putExtra("typepro", mTypePro);
                        intent.putExtra("quantitypro", mQuantityPro);


                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return false;
                    }
                });
            }

        };


        mrecyclerview.setLayoutManager(new GridLayoutManager(ViewInventoryActivity.this.getApplicationContext(),2));
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setAdapter(adapter);

        //SumTotalPurchase*Quantity

        CollectionReference docRef = firestore.collection("ProductsSaved")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products");

        docRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ViewInventoryActivity.this, "Error" + e, Toast.LENGTH_SHORT).show();
                    return;
                }

                double sum = 0;
                for (QueryDocumentSnapshot doc : value) {
                    Map<String, Object> map = (Map<String, Object>) doc.getData();
                    Object price = map.get("productPurchase");
                    Object quantity = map.get("productQuantity");

                    double pValue = Double.parseDouble(String.valueOf(price));
                    int pQuantity = Integer.parseInt(String.valueOf(quantity));
                    sum += pValue*pQuantity;
                    totalpurchase.setText(String.valueOf(sum));

                }
            }
        });

        //CountDocuments
        firestore.collection("ProductsSaved")
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("Products")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int counttotalnoofitem = task.getResult().size();
                    totalproducts.setText(Integer.toString(counttotalnoofitem));
                }else{
                    totalproducts.setText("0");
                }
            }
        });

    }

    
    @Override
    protected void onStart() {
        super.onStart();

       adapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();
      adapter.stopListening();
    }
}