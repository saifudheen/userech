package com.gofreshuser.tecmanic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Fragment.Thanks_fragment;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RazorPay extends AppCompatActivity implements PaymentResultListener {
    private Session_management sessionManagement;
    RelativeLayout confirm;

    private String getlocation_id = "";
    private String getstore_id = "";
    private String getvalue = "";
    private String get_wallet_ammount = "";
    private String gettime = "";
    private String getdate = "";
    private String usedamount = "";
    private String previousamount = "";
    private String coupon_code = "";
    private String getuser_id = "";
    String text;
    WebView webView;
    SharedPreferences delivercharge;
    ProgressDialog progressDialog;
    String delivery_getcharges;
    private String user_id = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_razor_pay);

        sessionManagement = new Session_management(RazorPay.this);

        delivercharge=getSharedPreferences("charges",0);
        delivery_getcharges=delivercharge.getString("delivery_charges","");

        String email = sessionManagement.getUserDetails().get(Baseurl.KEY_EMAIL);

        String mobile = sessionManagement.getUserDetails().get(Baseurl.KEY_MOBILE);

        String name = sessionManagement.getUserDetails().get(Baseurl.KEY_NAME);

        user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

        String total_rs = getIntent().getStringExtra("total");

        usedamount=getIntent().getStringExtra("usedamount");
        previousamount=getIntent().getStringExtra("previousamount");
        coupon_code=getIntent().getStringExtra("couponcode");
        getlocation_id = getIntent().getStringExtra("getlocationid");
        getstore_id = getIntent().getStringExtra("getstoreid");
        gettime = getIntent().getStringExtra("gettime");
        getvalue = getIntent().getStringExtra("getpaymentmethod");
        getdate = getIntent().getStringExtra("getdate");
        get_wallet_ammount = getIntent().getStringExtra("total_amount");
//        callInstamojoPay(email, mobile, total_rs, "official", name);
        sessionManagement = new Session_management(RazorPay.this);

        startPayment(name,total_rs,email,mobile);
    }
    public void startPayment(String name,String amount,String email,String phone) {
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

            options.put("amount", Double.valueOf(get_wallet_ammount)*100);

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
    @SuppressLint("JavascriptInterface")
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {

        attemptOrder();

        try {

            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Log.e("tag", "Exception in onPaymentSuccess", e);

        }


    }

    @Override
    public void onPaymentError(int i, String s) {
//        Intent intent = new Intent(RazorPay.this, OrderFail.class);
//        startActivity(intent);
//        finish();

    }





//

    private void attemptOrder() {

        getuser_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

        if (ConnectivityReceiver.isConnected()) {

            Log.e("tag", "from:" + gettime + "\ndate:" + getdate +
                    "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:");

            makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id);
        }

    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id) {

        String tag_json_obj = "json_add_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);

        params.put("time", gettime);

        params.put("user_id", userid);

        params.put("total_order_amount",get_wallet_ammount);
        params.put("used_amount",usedamount);
        params.put("previous_amount",previousamount);
        params.put("coupon_code",coupon_code);
        params.put("delivery_charge",delivery_getcharges);
        params.put("location", location);

        params.put("store_id", store_id);

        params.put("payment_method", getvalue);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());

                try {

                    Boolean status = response.getBoolean("responce");

                    if (status) {

                        String msg = response.getString("data");
                        Bundle args = new Bundle();
                        Fragment fm = new Thanks_fragment();
                        args.putString("msg", msg);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();
                    }
                    else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Tag", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(RazorPay.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
