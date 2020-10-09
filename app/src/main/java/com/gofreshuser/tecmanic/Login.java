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
import android.widget.TextView;
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
import com.gofreshuser.util.Session_management;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText email_add,password;

    Button login;
    Button back;
    TextView forget;
    ProgressDialog progressDialog;

    public static String TAG="Signin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_login);

        back=findViewById(R.id.back_button);
        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Please Wait");

        forget=findViewById(R.id.forget);
        login = findViewById(R.id.login);

        email_add = findViewById(R.id.email);

        password = findViewById(R.id.pass);

back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        onBackPressed();

    }
});
forget.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Login.this,Forget_Password.class);
        startActivity(intent);
    }
});
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_add.getText().toString().length()==0){
                    Toast.makeText(Login.this, "Please Enter Email Address", Toast.LENGTH_SHORT).show();
                }
                else if (password.getText().toString().length()==0)
                {
                    Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();

                }
                else {
                    attemptLogin();
                }

            }
        });


    }
    private void makeLoginRequest(String email, final String password) {

        // Tag used to cancel the request
        String tag_json_obj = "json_login_req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_email", email);
        params.put("password", password);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_SIGN_In, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        JSONObject obj = response.getJSONObject("data");
                        String user_id = obj.getString("user_id");
                        String user_fullname = obj.getString("user_fullname");
                        String user_email = obj.getString("user_email");
                        String user_phone = obj.getString("user_phone");
                        String user_image = obj.getString("user_image");
                        String wallet_ammount = obj.getString("wallet");
                        String reward_points = obj.getString("rewards");


                        Session_management sessionManagement = new Session_management(Login.this);
                        sessionManagement.createLoginSession(user_id, user_email, user_fullname, user_phone, user_image, wallet_ammount, reward_points, "", "", "", "", password);

                        progressDialog.dismiss();
                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
MyFirebaseRegister myFirebaseRegister=new MyFirebaseRegister(Login.this);
myFirebaseRegister.RegisterUser(user_id);
login.setEnabled(false);

                    } else {

                        progressDialog.dismiss();
                        login.setEnabled(true);
                        String error = response.getString("error");
                        Toast.makeText(Login.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Login.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


        private void attemptLogin () {


            String getpassword = password.getText().toString();
            String getemail = email_add.getText().toString();
            boolean cancel = false;
            View focusView = null;


            if (TextUtils.isEmpty(getemail)) {

                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
                focusView = email_add;
                cancel = true;

            } else if (!isEmailValid(getemail)) {
                Toast.makeText(this, "Enter valid Email", Toast.LENGTH_SHORT).show();
                focusView = email_add;
                cancel = true;
            } else if (TextUtils.isEmpty(getpassword)) {

                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
                focusView = password;
                cancel = true;

            } else if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                if (focusView != null)
                    focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                if (ConnectivityReceiver.isConnected()) {
                    progressDialog.show();

                    makeLoginRequest(getemail, getpassword);
                }
            }

        }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
