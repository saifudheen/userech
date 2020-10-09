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

public class Forget_Password extends AppCompatActivity {
    EditText forget_password;

    Button reset;
    Button back;
    ProgressDialog progressDialog;
    public static String TAG="Forget";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget__password);
        back=findViewById(R.id.back_button);
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Please Wait");

        reset = findViewById(R.id.reset);

        forget_password = findViewById(R.id.reset_pass);



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (forget_password.getText().toString().length()==0){
                    Toast.makeText(Forget_Password.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                }


                else {

                    attemptLogin();
                }

            }
        });


    }
    private void makeforgetrequest(String email) {

        // Tag used to cancel the request
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.FORGOT_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    String error = response.getString("error");

                    if (status) {



                        progressDialog.dismiss();
                        Intent i = new Intent(Forget_Password.this, Login.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(Forget_Password.this, "" + error, Toast.LENGTH_SHORT).show();


                        reset.setEnabled(false);

                    } else {

                        progressDialog.dismiss();
                        reset.setEnabled(true);

                        Toast.makeText(Forget_Password.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Forget_Password.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void attemptLogin () {


        String getpassword = forget_password.getText().toString();
        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(getpassword)) {

            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            focusView = forget_password;
            cancel = true;

        } else if (!isEmailValid(getpassword)) {
            Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
            focusView = forget_password;
            cancel = true;
        }
         else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {
                progressDialog.show();
                makeforgetrequest(getpassword);
            }
        }

    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}