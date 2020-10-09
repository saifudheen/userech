package com.gofreshuser.tecmanic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Fragment.Thanks_fragment;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {

    private String getlocation_id = "";

    private String getstore_id = "";

    private String gettime = "";

    private String getdate = "";
    SharedPreferences delivercharge;
    ProgressDialog progressDialog;
    String delivery_getcharges;
    private String getuser_id = "";

    String payment_method,totalamount;

    Session_management session_management;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirmation);

        session_management=new Session_management(this);

        getuser_id=session_management.getUserDetails().get(Baseurl.KEY_ID);

        getdate=getIntent().getStringExtra("getdate");

        delivercharge=getSharedPreferences("charges",0);
        delivery_getcharges=delivercharge.getString("delivery_charges","");

        gettime= getIntent().getStringExtra("gettime");

        getlocation_id=getIntent().getStringExtra("getlocationid");

        getstore_id=getIntent().getStringExtra("getstoreid");

        payment_method=getIntent().getStringExtra("getpaymentmethod");

        totalamount=getIntent().getStringExtra("PaymentAmount");

        makeAddOrderRequest(getdate,gettime,getuser_id,getlocation_id,getstore_id,payment_method);
    }
    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id,String method) {

        String tag_json_obj = "json_add_order_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("date", date);

        params.put("time", gettime);

        params.put("user_id", userid);

        params.put("location", location);

        params.put("store_id", store_id);
        params.put("total_order_amount",totalamount);
        params.put("delivery_charge",delivery_getcharges);

        params.put("payment_method", method);

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
                    Toast.makeText(ConfirmationActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
