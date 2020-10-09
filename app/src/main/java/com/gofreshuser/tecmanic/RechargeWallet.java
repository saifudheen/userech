package com.gofreshuser.tecmanic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Config.SharedPref;
import com.gofreshuser.util.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RechargeWallet extends AppCompatActivity implements PaymentResultListener {
    private Session_management sessionManagement;
    EditText Wallet_Ammount;
    RelativeLayout Recharge_Button;
    String ammount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_wallet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Recharge Wallet");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RechargeWallet.this, MainActivity.class);
                startActivity(intent);
            }
        });
        sessionManagement = new Session_management(RechargeWallet.this);
        final String email = sessionManagement.getUserDetails().get(Baseurl.KEY_EMAIL);
        final String mobile = sessionManagement.getUserDetails().get(Baseurl.KEY_MOBILE);
        final String name = sessionManagement.getUserDetails().get(Baseurl.KEY_NAME);
        Wallet_Ammount = (EditText) findViewById(R.id.et_wallet_ammount);
        Recharge_Button = (RelativeLayout) findViewById(R.id.recharge_button);

        Recharge_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ammount = Wallet_Ammount.getText().toString();
                //Recharge_wallet();
                startPayment(name, ammount, email, mobile);

            }
        });
    }

    public void startPayment(String name, String amount, String email, String phone) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */


        final Activity activity = this;

        final Checkout co = new Checkout();

        try {

            JSONObject options = new JSONObject();

            options.put("name", name);
            options.put("description", "Shopping Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");

            options.put("amount", Integer.parseInt(amount) * 100);

            JSONObject preFill = new JSONObject();

            preFill.put("email", email);

            preFill.put("contact", phone);

            options.put("prefill", preFill);

            co.open(activity, options);

        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Recharge_wallet();
        try {



            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
    }

    private void Recharge_wallet() {

        final String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

            RequestQueue rq = Volley.newRequestQueue(this);
            StringRequest postReq = new StringRequest(Request.Method.POST, Baseurl.Recharge_wallet,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("eclipse", "Response=" + response);
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.optString("success").equalsIgnoreCase("success")) {
                                    Intent intent = new Intent(RechargeWallet.this, RechargeWallet.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    String wallet_amount = object.getString("wallet_amount");
                                    SharedPref.putString(RechargeWallet.this, Baseurl.KEY_WALLET_Ammount, wallet_amount);
                                } else {
                                    // Toast.makeText(DashboardPage.this, "" + jObj.optString("msg"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error [" + error + "]");
                }
            }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", user_id);
                    params.put("wallet_amount", ammount);
                    return params;
                }
            };
            rq.add(postReq);


    }
}
