package com.softpala.supplierpaymentsapp_;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by tmoswa on 1/07/2018.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String URL_MAIN= "http://www.haltradedistribution.com/HalTradeDistributions/ken";
    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "http://www.haltradedistribution.com/HalTradeDistributions/ken";
    //1 means data is synced and 0 means data is not synced

    public static final String URL_SAVE_DELIVERY= URL_MAIN+"remuneration.php";
    public static final String URL_SAVE_USERS= URL_MAIN+"registration_sync.php";

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final int NAME_NOT_SYNCED_WITH_SERVER = 0;


    public static final String DATABASE_NAME = "sp.db";
    public static final String Table_Users = "users";
    public static final String Table_suppliers = "suppliers";
    public static final String Table_transactions = "transactions";

    public static final String COLUMN_STATUS = "status";

    public static final String main_id = "_id";
    public static final String userscol2 = "name";
    public static final String userscol4 = "uusername";
    public static final String userscol5 = "upassword";


    public static final String supplier_name = "supplier_name";
    public static final String supplier_phone_number = "phone_number";
    public static final String supplier_description = "supplier_description";

    public static final String transaction_code = "transaction_code";
    public static final String supplier_product_description = "supplier_product_description";
    public static final String total_invoice = "total_invoice";
    public static final String total_payments_made = "total_payments_made";
    public static final String activity_date = "activity_date";
    public static final String transaction_type = "transaction_type";
    public static final String unitprice="unitprice";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users ( _id INTEGER PRIMARY KEY AUTOINCREMENT,full_name VARCHAR NOT NULL,uusername VARCHAR NOT NULL,upassword VARCHAR NOT NULL,status INTEGER NOT NULL DEFAULT '0')");
        db.execSQL("CREATE TABLE suppliers ( _id INTEGER PRIMARY KEY AUTOINCREMENT,supplier_name VARCHAR NOT NULL,phone_number VARCHAR NOT NULL,supplier_description VARCHAR NOT NULL, status INTEGER NOT NULL DEFAULT '0')");
        db.execSQL("CREATE TABLE transactions ( _id INTEGER PRIMARY KEY AUTOINCREMENT,supplier_name VARCHAR NOT NULL,transaction_code VARCHAR NOT NULL,supplier_product_description VARCHAR NOT NULL,total_invoice VARCHAR NOT NULL DEFAULT '0',total_payments_made VARCHAR NOT NULL DEFAULT '0',activity_date DATE NOT NULL DEFAULT CURRENT_DATE ,status INTEGER NOT NULL DEFAULT '0',unitprice VARCHAR NOT NULL DEFAULT '0')");


    }

    private static final String ALTER_Stores="ALTER TABLE stores ADD StoreVATNumber VARCHAR DEFAULT 'N/A'";
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ALTER_Stores);
        onCreate(db);
    }

    public boolean reg_users(String full_name, String uusername, String upassword, int Stat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(userscol2, full_name);
        contentValues1.put(userscol4, uusername);
        contentValues1.put(userscol5, upassword);
        contentValues1.put(COLUMN_STATUS, Stat);
        long resultb = db.insert(Table_Users, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;

    }
  public boolean reg_suppliers(String supplier_name1, String phone_number1,String supplier_description1,int Stat) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(supplier_name, supplier_name1);
        contentValues1.put(supplier_phone_number, phone_number1);
      contentValues1.put(supplier_description, supplier_description1);
        contentValues1.put(COLUMN_STATUS, Stat);
        long resultb = db.insert(Table_suppliers, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;

    }
    public boolean reg_transactions(String transaction_code1,String supplier_name1, String supplier_product_description1, String total_invoice1,String total_payments_made1, String activity_date1, int Stat, String unitprice1) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(transaction_code, transaction_code1);
        contentValues1.put(supplier_name, supplier_name1);
        contentValues1.put(supplier_product_description, supplier_product_description1);
        contentValues1.put(total_invoice, total_invoice1);
        contentValues1.put(total_payments_made, total_payments_made1);
        contentValues1.put(activity_date, activity_date1);
        contentValues1.put(COLUMN_STATUS, Stat);
        contentValues1.put(unitprice, unitprice1);
        long resultb = db.insert(Table_transactions, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;

    }



    public boolean addexistinguser(String full_name, String uusername, String upassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(supplier_name, full_name);
        contentValues1.put(userscol4, uusername);
        contentValues1.put(userscol5, upassword);
        contentValues1.put(COLUMN_STATUS, 1);
        long resultb = db.insert(Table_Users, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;
    }
    public boolean addexistingsupplier(String supplier_name1, String supplier_phone_number1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(supplier_name, supplier_name1);
        contentValues1.put(supplier_phone_number, supplier_phone_number);
        contentValues1.put(COLUMN_STATUS, 1);
        long resultb = db.insert(Table_Users, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;
    }
    public boolean addexistingtransaction(String transaction_code1, String supplier_product_description1, String total_invoice1,String total_payments_made1, String activity_date1) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(transaction_code, transaction_code1);
        contentValues1.put(supplier_product_description, supplier_product_description1);
        contentValues1.put(total_invoice, total_invoice1);
        contentValues1.put(total_payments_made, total_payments_made1);
        contentValues1.put(activity_date, activity_date1);
        contentValues1.put(COLUMN_STATUS, 1);
        long resultb = db.insert(Table_Users, null, contentValues1);
        db.close();
        if (resultb == -1)
            return false;
        else
            return true;
    }

    public Cursor retrieve(String searchTerm) {
        String[] columns = {main_id,supplier_name,supplier_phone_number,supplier_description};
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Table_suppliers + " WHERE " + supplier_name + " LIKE '%" + searchTerm + "%' or  + phone_number "+" LIKE '%" + searchTerm + "%' or "+"  supplier_description "+" like '%"+ searchTerm+"%'";

            c=db.rawQuery(sql,null);
            return c;
        }
        //GROUP BY account_num
        String sql = "SELECT _id,supplier_name,phone_number,supplier_description FROM " + Table_suppliers;

        c=db.rawQuery(sql,null);

        return c;

    }

    public Cursor retrieve_trans(String searchTerm) {
        String[] columns = {main_id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice};
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT _id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice FROM " + Table_transactions + " WHERE " + transaction_code + " LIKE '%" + searchTerm + "%' or  + supplier_name "+" LIKE '%" + searchTerm + "%' or "+"  supplier_product_description "+" like '%"+ searchTerm+"%' or "+"  activity_date "+" like '%"+ searchTerm+"%'";

            c=db.rawQuery(sql,null);
            return c;
        }
        //GROUP BY account_num
        String sql = "SELECT _id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice  FROM " + Table_transactions;

        c=db.rawQuery(sql,null);

        return c;

    }

    public Cursor retrieve_statement(String nm) {
        String[] columns = {main_id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice};
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();

        if (nm != null && nm.length() > 0) {
            String sql = "SELECT _id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice FROM " + Table_transactions + " WHERE " + supplier_name + " LIKE '%" + nm + "%' ";

            c=db.rawQuery(sql,null);
            return c;
        }
        //GROUP BY account_num
        String sql = "SELECT _id,transaction_code,supplier_name,supplier_product_description,total_invoice,total_payments_made,activity_date,unitprice  FROM " + Table_transactions + " WHERE " + supplier_name + " LIKE '%" + nm + "%' ";

        c=db.rawQuery(sql,null);

        return c;

    }
}
