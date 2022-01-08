package com.example.scanbarapp.Controlador;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanbarapp.R;

public class ViewHolderInventory extends RecyclerView.ViewHolder {


    View mView;

    public ViewHolderInventory(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


    }

    public void setDetails(Context ctx, String itembarcode, String itemname,String itemprice, String itemcategory , String itemquantity){
        TextView item_barcode = (TextView) mView.findViewById(R.id.product_barcode);
        TextView item_name = (TextView) mView.findViewById(R.id.product_barname);
        TextView item_price = (TextView) mView.findViewById(R.id.product_barprice);
        TextView item_category = (TextView) mView.findViewById(R.id.product_barpresentation);
        TextView item_quantity = (TextView) mView.findViewById(R.id.product_bartotal);

        item_barcode.setText(itembarcode);
        item_name.setText(itemname);
        item_price.setText("S/. "+ itemprice);
        item_category.setText(itemcategory);
        item_quantity.setText(itemquantity);

    }


}
