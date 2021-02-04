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

public class MyAdapter_Trans extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<SiftThroughStores> stores;
    Dialog mydialog;
    Button Delete, Cancel;

    DatabaseHelper myDabas;
    UserSessionManager session;

    DatabaseHelper controllerdb = new DatabaseHelper(c);
    SQLiteDatabase db;

    public MyAdapter_Trans(Context c, ArrayList<SiftThroughStores> stores) {
        this.c = c;
        this.stores = stores;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_trans, parent, false);
        final MyHolder holder = new MyHolder(v);
        myDabas = new DatabaseHelper(c);
        mydialog = new Dialog(c);
        mydialog.setContentView(R.layout.options_menu_trans);
        mydialog.setTitle("Choose Action for Transaction");

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final TextView s_name = (TextView) mydialog.findViewById(R.id.supplier_name1);
                final TextView t_code = (TextView) mydialog.findViewById(R.id.transaction_code1);

                s_name.setText(stores.get(holder.getAdapterPosition()).getIdd());




                Cancel = (Button) mydialog.findViewById(R.id.cancel);
                Delete = mydialog.findViewById(R.id.delete);
                mydialog.show();




                ((Button) mydialog.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        String supplier_nam = s_name.getText().toString();
                        String transaction_cod = t_code.getText().toString();

                        db = myDabas.getReadableDatabase();

                        db.execSQL("DELETE  FROM  transactions WHERE _id= '" + supplier_nam + "'");

                        db.close();
                        Toast.makeText(c, "Successfully deleted Transaction " + supplier_nam, Toast.LENGTH_LONG).show();
                        mydialog.dismiss();

                    }
                });

                ((Button) mydialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                         mydialog.dismiss();

                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.transaction_code.setText(stores.get(position).getTransaction_code());
        holder.supplier_name.setText(stores.get(position).getSupplier_name());
        holder.supplier_product_description.setText(stores.get(position).getSupplier_product_description());
        holder.total_invoice.setText(stores.get(position).getTotal_invoice());
        holder.total_payments_made.setText(stores.get(position).getTotal_payments_made());
        holder.activity_date.setText(stores.get(position).getActivity_date());
        holder.unit_price.setText(stores.get(position).getUnitprice());


    }

    @Override
    public int getItemCount() {
        return stores.size();
    }

}