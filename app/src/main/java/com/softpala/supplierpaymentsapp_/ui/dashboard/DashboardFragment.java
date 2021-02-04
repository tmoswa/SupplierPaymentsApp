package com.softpala.supplierpaymentsapp_.ui.dashboard;

import android.app.SearchManager;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softpala.supplierpaymentsapp_.AddSupploier;
import com.softpala.supplierpaymentsapp_.AddTransaction;
import com.softpala.supplierpaymentsapp_.DatabaseHelper;
import com.softpala.supplierpaymentsapp_.MyAdapter;
import com.softpala.supplierpaymentsapp_.MyAdapter_Trans;
import com.softpala.supplierpaymentsapp_.R;
import com.softpala.supplierpaymentsapp_.SiftThroughStores;
import com.softpala.supplierpaymentsapp_.UserSessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DashboardFragment extends Fragment {

    RecyclerView rv1;
    SearchView sv;
    ArrayList<SiftThroughStores> stores=new ArrayList<>();
    MyAdapter_Trans adapter;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;

    private List<ClipData.Item> itemList = new ArrayList<>();

    public LinearLayout linearlayout;
    DatabaseHelper myDabas;
    UserSessionManager session;
    SQLiteDatabase db;

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        linearlayout= (LinearLayout) root.findViewById(R.id.Layout1);

        rv1= (RecyclerView) root.findViewById(R.id.rv1);
        rv1.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter=new MyAdapter_Trans(getActivity(),stores);

        myDabas=new DatabaseHelper(getActivity());
        // db = controllerdb.getReadableDatabase();
        db = myDabas.getWritableDatabase();


        return root;
    }


    private void getStores(String searchTerm)
    {
        stores.clear();

        DatabaseHelper db=new DatabaseHelper(getActivity());
        myDabas=new DatabaseHelper(getActivity());
        session = new UserSessionManager(getActivity().getApplicationContext());
        if(session.checkLogin())
            getActivity().finish();
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // get name
        String username = user.get(UserSessionManager.KEY_User);
        SiftThroughStores p=null;
        Cursor c=db.retrieve_trans(searchTerm);

        while (c.moveToNext())
        {
            String id=c.getString(0);
            String transaction_code=c.getString(1);
            String supplier_name=c.getString(2);
            String supplier_product_description=c.getString(3);
            String total_invoice=c.getString(4);
            String total_payments_made=c.getString(5);
            String activity_date=c.getString(6);
            String unitprice=c.getString(7);

            p=new SiftThroughStores();
            p.setIdd(id);
            p.setTransaction_code(transaction_code);
            p.setSupplier_name(supplier_name);
            p.setSupplier_product_description(supplier_product_description);
            p.setTotal_invoice(total_payments_made);
            p.setTotal_payments_made(total_invoice);
            p.setActivity_date(activity_date);
            p.setUnitprice(unitprice);

            stores.add(p);

         /*   linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(),"You clicked"+account_num,Toast.LENGTH_LONG).show();
                }
            });*/

        }

        rv1.setAdapter(adapter);


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.seearch, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    getStores(newText);
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.close) {
            getActivity().finish();
        }
        if (id == R.id.add) {
            AddTransaction nextFrag= new AddTransaction();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, nextFrag, "Add Transaction")
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }
}



