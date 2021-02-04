package com.softpala.supplierpaymentsapp_;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.Line;
import com.softpala.supplierpaymentsapp_.ui.notifications.NotificationsFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class ShopStatement extends AppCompatActivity {
    private static final String TAG = "PdfCreatorActivity";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 111;
    private File pdfFile;
    ImageView imgdownload;
    Context context;

    DatabaseHelper myDabas;
    UserSessionManager session;
    SQLiteDatabase db;

    ArrayList<SiftThroughStores> stores=new ArrayList<>();

    String supplier_name,supplier_phone,supplier_decription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_statement);
        imgdownload = findViewById(R.id.downloadpdf);
        context = this;
        myDabas=new DatabaseHelper(this);

        supplier_name=getIntent().getExtras().getString("supplier_name");
        supplier_phone = getIntent().getExtras().getString("supplier_phone");
        supplier_decription = getIntent().getExtras().getString("supplier_decription");

        Toast.makeText(this,"client name: "+supplier_name,Toast.LENGTH_LONG).show();

        imgdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
/*
                     supplier_name=supplier_name1;
                     supplier_phone = supplier_phone1;
                     supplier_decription = supplier_decription2;
*/

                    createPdfWrapper();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void createPdfWrapper() throws FileNotFoundException, DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        } else {
            createPdf();
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.i(TAG, "Created a new directory for PDF");
        }




        String pdfname = supplier_name+"_ShopStatement.pdf";
        pdfFile = new File(docsFolder.getAbsolutePath(), pdfname);
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document(PageSize.A4);
        PdfPTable table = new PdfPTable(new float[]{3, 3, 3, 3, 3});
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setFixedHeight(50);
        table.setTotalWidth(PageSize.A4.getWidth());
        table.setWidthPercentage(100);
        table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);
        table.addCell("Description");
        table.addCell("Transaction Type");
        table.addCell("Date");
        table.addCell("DR");
        table.addCell("CR");
        table.setHeaderRows(1);
        PdfPCell[] cells = table.getRow(0).getCells();
        for (int j = 0; j < cells.length; j++) {
            cells[j].setBackgroundColor(BaseColor.LIGHT_GRAY);
        }

        stores.clear();

        Cursor c=myDabas.retrieve_statement(supplier_name);
        double deli= getTotalDeliveries(supplier_name);
        double pay=getTotalPayments(supplier_name);
        double bal=deli-pay;

        DecimalFormat twoDForm = new DecimalFormat("#.00");
        String balabance=twoDForm.format(bal);

        while (c.moveToNext())
        {



            String id=c.getString(0);
            String transaction_code=c.getString(1);
            String supplier_name1=c.getString(2);
            String supplier_product_description=c.getString(3);
            String total_invoice=c.getString(4);
            String total_payments_made=c.getString(5);
            String activity_date=c.getString(6);
            String unitprice=c.getString(7);

            String transtyp="";

            Toast.makeText(this,"Successfully Exported Statement for check in Documents Folder: ",Toast.LENGTH_LONG).show();
            String payments="";
            String invoice="";
            if(total_invoice.equals(null))
            {
                transtyp="Receipt";
                payments="USD$ "+total_payments_made;
            }
            else
            {
                transtyp="Invoice";
                invoice="USD$ "+total_invoice;
            }

            table.addCell(supplier_product_description);
            table.addCell(transtyp);
            table.addCell(activity_date);
            table.addCell(payments);
            table.addCell(invoice);

        }
        int contentsize=1;
        for (int i = 0; i < contentsize; i++) {


            table.addCell("Balance");
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("USD$ "+balabance);

        }

//        System.out.println("Done");
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = formatter.format(currentDate);


        PdfWriter.getInstance(document, output);
        document.open();
        Font f = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);
        Font g = new Font(Font.FontFamily.TIMES_ROMAN, 20.0f, Font.BOLD, BaseColor.BLACK);
        Font h = new Font(Font.FontFamily.TIMES_ROMAN, 12.0f, Font.BOLD, BaseColor.BLACK);
        String format = "                                                      ";
        document.add(new Paragraph("Shop Statement \n\n", f));
        document.add(new Paragraph("Name: \t"+supplier_name+ format +"\t Statement Date: \t" + strDate+ "\n\n", h));

      //  document.add(new Paragraph("Statement Date: \t\t"+strDate+"\n\n", h));
        document.add(table);

//        for (int i = 0; i < MyList1.size(); i++) {
//            document.add(new Paragraph(String.valueOf(MyList1.get(i))));
//        }
        document.close();
        Log.e("timothy", "Timoty Code");
        previewPdf();
    }

    private void previewPdf() {

        PackageManager packageManager = context.getPackageManager();
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        testIntent.setType("application/pdf");
        List list = packageManager.queryIntentActivities(testIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (list.size() > 0) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Download a PDF Viewer to see the generated PDF", Toast.LENGTH_SHORT).show();
        }
    }



    public double getTotalDeliveries( String supplier_name ) {
        db = myDabas.getReadableDatabase();



        Cursor c  = db.rawQuery("SELECT SUM(total_invoice) FROM  transactions WHERE supplier_name='"+ supplier_name +"'",null);

        c.moveToFirst();
        double iii = c.getDouble(0);
        DecimalFormat twoDForm = new DecimalFormat("#.00");
        String ii=twoDForm.format(iii);
        double i=Double.parseDouble(ii);
        c.close();
        return i;
    }
    public double getTotalPayments( String supplier_name ) {
        db = myDabas.getReadableDatabase();



        Cursor c  = db.rawQuery("SELECT SUM(total_payments_made) FROM  transactions WHERE supplier_name='"+ supplier_name +"'",null);

        c.moveToFirst();
        double iii = c.getDouble(0);
        DecimalFormat twoDForm = new DecimalFormat("#.00");
        String ii=twoDForm.format(iii);
        double i=Double.parseDouble(ii);
        c.close();
        return i;
    }

}
