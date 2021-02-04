package com.softpala.supplierpaymentsapp_;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView supplier_name, supplier_phone,supplier_description;
    TextView transaction_code,supplier_product_description,total_invoice,total_payments_made,activity_date,unit_price;



    public MyHolder(View itemView) {
        super(itemView);



        this.supplier_name=(TextView)itemView.findViewById(R.id.supplier_name);
        this.supplier_phone=(TextView)itemView.findViewById(R.id.supplier_phone);
        this.supplier_description=(TextView)itemView.findViewById(R.id.supplier_decription);

        this.transaction_code=(TextView)itemView.findViewById(R.id.transaction_code);
        this.supplier_product_description=(TextView)itemView.findViewById(R.id.supplier_product_description);
        this.total_invoice=(TextView)itemView.findViewById(R.id.total_invoice);
        this.total_payments_made=(TextView)itemView.findViewById(R.id.total_payments_made);
        this.activity_date=(TextView)itemView.findViewById(R.id.activity_date);
        this.unit_price=(TextView)itemView.findViewById(R.id.unit_price);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(itemView.getContext(), "Position:" + Integer.toString(getPosition()), Toast.LENGTH_SHORT).show();
    }
}
