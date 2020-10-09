package com.gofreshuser.tecmanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    Button backbutton,next;

    EditText first_name,last_name,email_add,create_pass,mobile;

    ProgressDialog progressDialog;

    public static String TAG="Signup";

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        backbutton=findViewById(R.id.back_button);

        next=findViewById(R.id.next);

        first_name=findViewById(R.id.first_name);

        last_name=findViewById(R.id.last_name);

        email_add=findViewById(R.id.email_add);

        create_pass=findViewById(R.id.create_pass);

        mobile=findViewById(R.id.mobile_num);

        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Please Wait");


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegister();

            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void attemptRegister() {


        String getphone = mobile.getText().toString();
        String getname = first_name.getText().toString();
        String getlastname = last_name.getText().toString();
        String name = getname + " " + getlastname;
        String getpassword = create_pass.getText().toString();
        String getemail = email_add.getText().toString();

        boolean cancel = false;

        View focusView = null;

        if (TextUtils.isEmpty(getname)) {
            Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(getlastname)) {
            Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(getpassword)) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            focusView = create_pass;
            cancel = true;
        }
        else if (TextUtils.isEmpty(getphone)) {
            Toast.makeText(this, "Enter Mobile Number", Toast.LENGTH_SHORT).show();
            focusView = mobile;
            cancel = true;
        }

//
               else if (TextUtils.isEmpty(getemail)) {

                    Toast.makeText(this, "Enter Emailid", Toast.LENGTH_SHORT).show();

                    focusView = email_add;

                    cancel = true;
                }

//
           else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                if (ConnectivityReceiver.isConnected()) {
                    progressDialog.show();

                    makeRegisterRequest(name, getphone, getemail, getpassword);
                }
            }
        }




//    private boolean isEmailValid(String email) {
//        //TODO: Replace this with your own logic
//        return email.contains("@");
//    }
//
//    private boolean isPasswordValid(String password) {
//        //TODO: Replace this with your own logic
//        return password.length() > 6;
//    }
//
//    private boolean isPhoneValid(String phoneno) {
//        //TODO: Replace this with your own logic
//        return phoneno.length() > 9;
//    }

    private void makeRegisterRequest(String name, String mobile,
                                     String email, String password) {

        // Tag used to cancel the request
        String tag_json_obj = "json_register_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_name", name);
        params.put("user_mobile", mobile);
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_SIGN_UP, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        progressDialog.dismiss();

                        String msg = response.getString("message");
                        Toast.makeText(SignUp.this, "" + msg, Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(SignUp.this, ShowLogin.class);
                        startActivity(i);
                        finish();

                    } else {
                        progressDialog.dismiss();


                        String error = response.getString("error");
                        Toast.makeText(SignUp.this, "" + error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(SignUp.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


}
