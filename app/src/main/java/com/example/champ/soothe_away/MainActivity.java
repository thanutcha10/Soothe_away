package com.example.champ.soothe_away;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ImageButton bt_logout;
    ImageButton bt_search;
    Button bt_info;
    Button bt_masshop;
    Button bt_content;
    TextView txt_username;
    EditText ed_search;
    AlertDialog alertDialog;
    String id;
    String email;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_logout = (ImageButton) findViewById(R.id.btn_logout);
        bt_search = (ImageButton) findViewById(R.id.btn_search);
        bt_info = (Button) findViewById(R.id.btn_info);
        bt_masshop = (Button) findViewById(R.id.btn_masshop);
        bt_content = (Button) findViewById(R.id.btn_content);
        ed_search = (EditText) findViewById(R.id.ed_search);
        txt_username = (TextView) findViewById(R.id.txt_username);

        alertDialog = new AlertDialog.Builder(this).create();

        prefs = getSharedPreferences(Constant.MY_PREFERENCES, Context.MODE_PRIVATE);
        init();


    }

    public void init() {

        id = prefs.getString(Constant.USER_ID, null);
        email = prefs.getString(Constant.EMAIL, null);
        if (email != null){
            txt_username.setText("logged in : " +email);
        }

        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (prefs.getString(Constant.USER_ID, null) == null) {
                    //login
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //logout
                    txt_username.setText("");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();
                }
            }
        });

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Member_Activity.class);
                startActivity(intent);
            }
        });
        bt_masshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Shop_Activity.class);
                startActivity(intent);
            }
        });
        bt_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Content_Activity.class);
                startActivity(intent);
            }
        });
    }

}
