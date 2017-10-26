package com.example.champ.soothe_away;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText ed_username;
    EditText ed_password;
    Button bt_login;
    Button bt_register;
    String username;
    String password;
    AlertDialog alertDialog;
    ProgressDialog loading;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ed_username = (EditText) findViewById(R.id.ed_user);
        ed_password = (EditText) findViewById(R.id.ed_password);
        bt_login = (Button) findViewById(R.id.btn_login);
        bt_register = (Button) findViewById(R.id.btn_register);

        alertDialog = new AlertDialog.Builder(this).create();
        loading = new ProgressDialog(this);

        init();
    }

    public void init() {

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = ed_username.getText().toString();
                password = ed_password.getText().toString();

                if (username.length() == 0) {
                    setAlertDialog(Constant.USERNAME_REQUIRE);
                } else if (password.length() == 0) {
                    setAlertDialog(Constant.PASSWORD_REQUIRE);
                } else if (username.length() != 0 && password.length() != 0) {
                    doLogin(username, password);
                }
            }
        });

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    public void doLogin(String username, String password) {
        if (loading != null && !loading.isShowing()) {
            loading.setMessage(Constant.PLEASE_WAIT);
            loading.show();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", username);
            jsonObject.put("password", password);
        } catch (Exception e) {
            Log.d("create json error", e.toString());
        }

        RequestBody formBody = RequestBody.create(Constant.JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(Constant.LOGIN)
                .post(formBody)
                .build();

        Task task = new Task();
        task.setWebserviceListener(new Task.Callback() {
            JSONObject result;

            @Override
            public void Complete(Task task) {
                if (loading.isShowing())
                    loading.dismiss();

                //{"message":"","data":{"id":2,"email":"member@example.com",
                // "created_at":"2017-08-30 11:20:59","updated_at":"2017-08-30 11:20:59"}}
                if (task.getResult() != null) {
                    try {
                        result = new JSONObject(task.getResult().toString());
                        if (result.getJSONObject("data") == null) {
                            ed_password.setText("");
                            setAlertDialog(Constant.LOGIN_INCOMPLETE);
                        } else if (result.getJSONObject("data") != null) {
                            JSONObject data = result.getJSONObject("data");

                            if (data != null) {
                                String id = data.getString("id");
                                String email = data.getString("email");


                                SharedPreferences.Editor editor = getSharedPreferences(Constant.MY_PREFERENCES, Context.MODE_PRIVATE).edit();
                                editor.putString(Constant.USER_ID, id);
                                editor.putString(Constant.EMAIL, email);
                                editor.apply();

                                goToMenu();

                            } else {
                                ed_password.setText("");
                                setAlertDialog(Constant.LOGIN_INCOMPLETE);
                            }
                        } else {
                            ed_password.setText("");
                            setAlertDialog(Constant.LOGIN_INCOMPLETE);
                        }

                    } catch (Exception e) {
                        ed_password.setText("");
                        setAlertDialog(Constant.LOGIN_INCOMPLETE);
                    }
                }
            }

            @Override
            public void Error(String errorMsg) {
                if (loading.isShowing())
                    loading.dismiss();
                ed_password.setText("");
                setAlertDialog(Constant.LOGIN_INCOMPLETE);
            }
        });
        task.execute(request);
    }

    public void setAlertDialog(String msg) {
        alertDialog.setMessage(msg);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, Constant.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void goToMenu() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
