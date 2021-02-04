package com.softpala.supplierpaymentsapp_;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static String username;
    private static EditText username1;
    private static EditText password;
    private static TextView attempts;
    private static Button login_btn;
    int attempt_counter = 5;

    DatabaseHelper myDabas;
    UserSessionManager session;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new UserSessionManager(getApplicationContext());

        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(UserSessionManager.KEY_User);
        String access1 = user.get(UserSessionManager.KEY_access);



        Boolean stat = session.isUserLoggedIn();
        if (stat.equals(true)) {
            Toast.makeText(getApplicationContext(),
                    "Successfully logged in as: " + username,
                    Toast.LENGTH_LONG).show();
            if(access1.equals("Admin"))
            {
                Intent i = new Intent(getApplicationContext(), MainNav.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        "Session of user not recognised ",
                        Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplicationContext(),
                    "Please Login here and proceed ",
                    Toast.LENGTH_LONG).show();
        }



        LoginButton();
    }

    public void LoginButton() {
        username1 = (EditText) findViewById(R.id.editText_user);
        password = (EditText) findViewById(R.id.editText_password);
        attempts = (TextView) findViewById(R.id.textView_attemt_Count);
        login_btn = (Button) findViewById(R.id.button_login);

        attempts.setText(Integer.toString(attempt_counter));

        login_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username1.getText().toString().equals("Jerahuni") &&
                                password.getText().toString().equals("#pass123")) {
                            Toast.makeText(MainActivity.this, "Successful",
                                    Toast.LENGTH_SHORT).show();
                            session.createUserLoginSession(username,
                                    password.getText().toString(),"Admin");
                            Intent i = new Intent(getApplicationContext(), MainNav.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "User and Password is not correct"+username1.getText().toString()+"pass"+password.getText().toString(),
                                    Toast.LENGTH_SHORT).show();
                            attempt_counter--;
                            attempts.setText(Integer.toString(attempt_counter));
                            if (attempt_counter == 0) {
                                login_btn.setEnabled(false);
                            }
                        }

                    }
                }
        );
    }


}