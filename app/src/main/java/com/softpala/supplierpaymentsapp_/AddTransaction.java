package com.softpala.supplierpaymentsapp_;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddTransaction extends Fragment {

    Spinner transaction_type,supplier_name;
    DatabaseHelper controllerdb = new DatabaseHelper(getActivity());
    SQLiteDatabase db;
    DatabaseHelper myDabas;
    EditText transaction_code,supplier_product_description,amount,unitprice;
    Button reg;
    String transaction_code1,supplier_product_description1,amount1,transaction_type1,supplier_name1,unitprice1;

    public AddTransaction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_transaction, container, false);

        myDabas = new DatabaseHelper(getActivity());
        transaction_type=(Spinner) view.findViewById(R.id.transaction_type);
        supplier_name=(Spinner) view.findViewById(R.id.supplier_name);

        transaction_code=(EditText) view.findViewById(R.id.transaction_code);
        supplier_product_description=(EditText) view.findViewById(R.id.supplier_product_description);
        amount=(EditText) view.findViewById(R.id.amount);
        unitprice=(EditText) view.findViewById(R.id.unitprice);
        reg=(Button) view.findViewById(R.id.reg);

        String[] a = transaction_type_list1().toArray(new String[transaction_type_list1().size()]);
        ArrayAdapter<String> transaction_type_list = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, a);
        supplier_name.setAdapter(transaction_type_list);

        String[] items = new String[]{"--Select Transaction Type--","Delivery", "Payment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        transaction_type.setAdapter(adapter);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                Date currentDate = cal.getTime();
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                final String strDate = formatter.format(currentDate);

                transaction_code1="ken";
                supplier_product_description1=supplier_product_description.getText().toString();
                amount1=amount.getText().toString();
                transaction_type1=transaction_type.getSelectedItem().toString();
                supplier_name1=supplier_name.getSelectedItem().toString();
                unitprice1=unitprice.getText().toString();
String total_invoice="";
String total_payments_made="";

                if(transaction_type1.equals("Delivery"))
                {
                    total_invoice=amount1;
                }
                else
                {
                    total_payments_made=amount1;
                }

           Boolean b = myDabas.reg_transactions(transaction_code1,supplier_name1,supplier_product_description1,total_invoice,total_payments_made,strDate,0,unitprice1);
                if(b.equals(true)) {
                    Intent i = new Intent(getActivity().getApplicationContext(), MainNav.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    getActivity().finish();
                }
                else
                {
                    Toast.makeText(getActivity(), "failed to record Transaction", Toast.LENGTH_SHORT).show();
                }
            }
        });



    return view;
    }
    public ArrayList<String> transaction_type_list1() {
        db = myDabas.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM  suppliers",null);
        ArrayList<String> array = new ArrayList<String>();
        while (cur.moveToNext()) {

            String first =cur.getString(cur.getColumnIndex("supplier_name"));

            array.add(first);

        }
        return array;
    }
}
