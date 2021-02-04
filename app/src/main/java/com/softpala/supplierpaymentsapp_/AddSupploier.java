package com.softpala.supplierpaymentsapp_;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddSupploier extends Fragment {

EditText supplier_name;
EditText supplier_phone;
EditText supplier_description;
Button reg;
DatabaseHelper db;


    public AddSupploier() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_supploier, container, false);
        supplier_name=(EditText) view.findViewById(R.id.supplier_name);
        supplier_phone=(EditText) view.findViewById(R.id.supplier_phone);
        supplier_description=(EditText) view.findViewById(R.id.supplier_description);
        reg=(Button) view.findViewById(R.id.reg);
        db=new DatabaseHelper(getActivity());
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Boolean b= db.reg_suppliers(supplier_name.getText().toString(),supplier_phone.getText().toString(),supplier_description.getText().toString(),0);
              if(b.equals(true)) {
                  Intent i = new Intent(getActivity().getApplicationContext(), MainNav.class);
                  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(i);
                  getActivity().finish();
              }
              else
              {
                  Toast.makeText(getActivity(), "failed to register supplier", Toast.LENGTH_SHORT).show();
              }

            }
        });

        return view;
    }

}
