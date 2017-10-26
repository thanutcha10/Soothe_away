package com.example.champ.soothe_away;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    EditText ed_email;
    EditText ed_password;
    EditText ed_conpassword;
    EditText ed_name;
    EditText ed_lastname;
    EditText ed_telephone;
    RadioButton radi_male;
    RadioButton radi_female;
    Button btn_register;
    String email;
    String password;
    String conpassword;
    String name;
    String lastname;
    String telephone;
    Boolean male;
    Boolean female;
    AlertDialog alertDialog;
    ProgressDialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ed_email = (EditText) findViewById(R.id.ed_email);
        ed_password = (EditText) findViewById(R.id.ed_password);
        ed_conpassword = (EditText) findViewById(R.id.ed_conpass);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_lastname = (EditText) findViewById(R.id.ed_lastname);
        ed_telephone = (EditText) findViewById(R.id.ed_telephone);
        radi_male = (RadioButton) findViewById(R.id.radi_male);
        radi_female = (RadioButton) findViewById(R.id.radi_famale);
        btn_register = (Button) findViewById(R.id.btn_register);
        alertDialog = new AlertDialog.Builder(this).create();
        loading = new ProgressDialog(this);

        init();
    }

    public void init() {
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = ed_email.getText().toString();
                password = ed_password.getText().toString();
                conpassword = ed_conpassword.getText().toString();
                name = ed_name.getText().toString();
                lastname = ed_lastname.getText().toString();
                telephone = ed_telephone.getText().toString();
                male = radi_male.isChecked();
                female = radi_female.isChecked();
                if (email.length() != 0 && password.length() != 0 && conpassword.length() != 0 && name.length() != 0 && lastname.length() != 0 && telephone.length() != 0 && (male || female)) {
                    if (password.equals(conpassword)) {
                        //TODO make value
                        doRegister(email, password, name, lastname, telephone);
                    } else {
                        setAlertDialog(Constant.PASSWORD_NOT_MATCH);
                    }
                } else {
                    setAlertDialog(Constant.FILED_REQUIRE);
                }

            }
        });
    }

    public void doRegister(String email, String password, String name, String lastname, String telephone) {
        if (loading != null && !loading.isShowing()) {
            loading.setMessage(Constant.PLEASE_WAIT);
            loading.show();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("name", name);
            jsonObject.put("lastname", lastname);
            jsonObject.put("telephone", telephone);
        } catch (Exception e) {
            Log.d("create json error", e.toString());
        }

        RequestBody formBody = RequestBody.create(Constant.JSON, jsonObject.toString());

        final Request request = new Request.Builder()
                .url(Constant.CREATE_MEMBER)
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
                            setAlertDialog(Constant.REGISTER_INCOMPLETE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMenu() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}

