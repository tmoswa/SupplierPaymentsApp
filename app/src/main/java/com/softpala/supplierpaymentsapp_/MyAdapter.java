package com.softpala.supplierpaymentsapp_;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<SiftThroughStores> stores;
    Dialog mydialog;
    Button ViewStatement, Delete, Cancel;

    DatabaseHelper myDabas;
    UserSessionManager session;

    DatabaseHelper controllerdb = new DatabaseHelper(c);
    SQLiteDatabase db;

    public MyAdapter(Context c, ArrayList<SiftThroughStores> stores) {
        this.c = c;
        this.stores = stores;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model, parent, false);
        final MyHolder holder = new MyHolder(v);
        myDabas = new DatabaseHelper(c);
        mydialog = new Dialog(c);
        mydialog.setContentView(R.layout.options_menu);
        mydialog.setTitle("Choose Action for Supplier");

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final TextView s_name = (TextView) mydialog.findViewById(R.id.supplier_name1);
                final TextView s_phone = (TextView) mydialog.findViewById(R.id.supplier_phone1);
                final TextView s_desc = (TextView) mydialog.findViewById(R.id.supplier_decription1);


                ViewStatement = (Button) mydialog.findViewById(R.id.ViewStatement);
                Cancel = (Button) mydialog.findViewById(R.id.cancel);
                Delete = mydialog.findViewById(R.id.delete);
                mydialog.show();



                s_name.setText(stores.get(holder.getAdapterPosition()).getSupplier_name());
                s_phone.setText(stores.get(holder.getAdapterPosition()).getSupplier_phone());
                s_desc.setText(stores.get(holder.getAdapterPosition()).getSupplier_description());

                //      _______________________________________________## Shop Statement
                ((Button) mydialog.findViewById(R.id.ViewStatement)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        Intent i = new Intent(c, ShopStatement.class);
                        String supplier_nam = s_name.getText().toString();
                        String supplier_phon = s_phone.getText().toString();
                        String supplier_desc = s_desc.getText().toString();

                        i.putExtra("supplier_name", (supplier_nam));
                        i.putExtra("supplier_phone", (supplier_phon));
                        i.putExtra("supplier_decription", (supplier_desc));

                        c.startActivity(i);

                        mydialog.dismiss();
                    }
                });

                ((Button) mydialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mydialog.dismiss();

                    }
                });
                ((Button) mydialog.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        String supplier_nam = s_name.getText().toString();
                        String supplier_phon = s_phone.getText().toString();
                        String supplier_desc = s_desc.getText().toString();

                        db = myDabas.getReadableDatabase();

                        db.execSQL("DELETE  FROM  stores WHERE supplier_name= '" + supplier_nam + "' and phone_number = '" + s_phone + "' and supplier_description = '" + s_desc + "'");

                        db.close();
                        Toast.makeText(c, "Successfully deleted store " + supplier_nam, Toast.LENGTH_LONG).show();
                        mydialog.dismiss();

                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        holder.supplier_name.setText(stores.get(position).getSupplier_name());
        holder.supplier_phone.setText(stores.get(position).getSupplier_phone());
        holder.supplier_description.setText(stores.get(position).getSupplier_description());


       // Toast.makeText(this, stores.get(position).getSupplier_name() ,Toast.LENGTH_LONG).show();


    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

}