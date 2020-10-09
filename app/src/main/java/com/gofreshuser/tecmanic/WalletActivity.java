package com.gofreshuser.tecmanic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Config.SharedPref;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WalletActivity extends AppCompatActivity {

    private static String TAG = WalletActivity.class.getSimpleName();

    TextView Wallet_Ammount;

    SwipeRefreshLayout refresh_layout;

    RelativeLayout Recharge_Wallet;
    Handler handler = new Handler();
    Runnable timeCounter;
    Toolbar toolbar;

    private Session_management sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_wallet_ammount);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Orders");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();

            }
        });

        sessionManagement = new Session_management(this);
        //   String getwallet = sessionManagement.getUserDetails().get(BaseURL.KEY_WALLET_Ammount);
        Wallet_Ammount = (TextView) findViewById(R.id.wallet_ammount);
        refresh_layout=findViewById(R.id.refresh_layout);


        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(handler!=null && timeCounter!=null){
                    handler.removeCallbacks(timeCounter);
                }
              getRefresrh();
                refresh_layout.setRefreshing(false);

            }
        });

        //     Wallet_Ammount.setText(getwallet);
        Recharge_Wallet = (RelativeLayout) findViewById(R.id.recharge_wallet);
        Recharge_Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletActivity.this, RechargeWallet.class);
                startActivity(intent);
            }
        });
        if (ConnectivityReceiver.isConnected()) {
            getRefresrh();
        }




    }

    public void getRefresrh() {
        final String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest strReq = new StringRequest(Request.Method.POST, Baseurl.WALLET_REFRESH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            String wallet_amount = jObj.getString("wallet");
                            Wallet_Ammount.setText(wallet_amount);
                            SharedPref.putString(WalletActivity.this, Baseurl.KEY_WALLET_Ammount, wallet_amount);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }

        };
        rq.add(strReq);
    }
}