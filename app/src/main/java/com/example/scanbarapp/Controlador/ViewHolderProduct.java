package com.example.scanbarapp.Controlador;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanbarapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;
import java.util.Objects;

public class ViewHolderProduct extends RecyclerView.ViewHolder{

    View mView;

    public ViewHolderProduct(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    public void setDetails(Context ctx, String itembarcodeinventory, String itemnameinventory, String itempurchaseinventory
            , String itempriceinventory, String itemdateinventory, String itempresentation , String itemquantity,String itempackaging, String itemtime,
                           String itemdescription){
        TextView item_barcode = (TextView) mView.findViewById(R.id.inventory_barcode);
        TextView item_name = (TextView) mView.findViewById(R.id.iventory_barname);
        TextView item_purchase = (TextView) mView.findViewById(R.id.inventory_barpurchase);
        TextView item_price = (TextView) mView.findViewById(R.id.inventory_barprice);
        TextView item_date = (TextView) mView.findViewById(R.id.inventory_bardate);
        TextView item_category = (TextView) mView.findViewById(R.id.inventory_barpresentation);
        TextView item_quantity = (TextView) mView.findViewById(R.id.inventory_bartotal);
        TextView item_packaging = (TextView) mView.findViewById(R.id.inventory_packaging);
        TextView item_time = (TextView) mView.findViewById(R.id.inventory_time);
        TextView item_description = (TextView) mView.findViewById(R.id.inventory_description);

        item_barcode.setText(itembarcodeinventory);
        item_name.setText(itemnameinventory);
        item_purchase.setText(itempurchaseinventory);
        item_price.setText( itempriceinventory);
        item_date.setText(itemdateinventory);
        item_category.setText(itempresentation);
        item_quantity.setText(itemquantity);
        item_packaging.setText(itempackaging);
        item_time.setText(itemtime);
        item_description.setText(itemdescription);

    }

    private  ViewHolderProduct.ClickListener mClickListener;

    public  interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolderProduct.ClickListener clickListener){
        mClickListener = clickListener;
    }

}
