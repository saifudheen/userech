package com.gofreshuser.tecmanic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Adapter.My_order_detail_adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.My_order_detail_model;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonArrayRequest;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderDetail extends AppCompatActivity {

    Session_management sessionManagement;

    private static String TAG = MyOrderDetail.class.getSimpleName();

    private TextView tv_date, tv_time, tv_total, tv_delivery_charge,tv_coupon_code,tv_used_amount;

    private RelativeLayout btn_cancle;

    private RecyclerView rv_detail_order;

    private String sale_id;

    ImageView back_button;

    Toolbar toolbar;

    private List<My_order_detail_model> my_order_detail_modelList = new ArrayList<>();

    public MyOrderDetail() {
        // Required empty public constructor
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order_detail);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Orders Detail");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyOrderDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });
        tv_date = (TextView) findViewById(R.id.tv_order_Detail_date);
        tv_coupon_code = (TextView) findViewById(R.id.tv_order_coupon_code);
        tv_used_amount = (TextView) findViewById(R.id.tv_order_coupon_code_used);

        tv_time = (TextView) findViewById(R.id.tv_order_Detail_time);
        tv_delivery_charge = (TextView) findViewById(R.id.tv_order_Detail_deli_charge);
        tv_total = (TextView) findViewById(R.id.tv_order_Detail_total);
        btn_cancle = (RelativeLayout) findViewById(R.id.btn_order_detail_cancle);
        rv_detail_order = (RecyclerView) findViewById(R.id.rv_order_detail);

        rv_detail_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sale_id = getIntent().getStringExtra("sale_id");
        String total_rs = getIntent().getStringExtra("total");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String status = getIntent().getStringExtra("status");
        String deli_charge = getIntent().getStringExtra("deli_charge");
        String coupon_code=getIntent().getStringExtra("coupon_code");
        String used_amont=getIntent().getStringExtra("used_amount");


        if (status.equals("0")) {
            btn_cancle.setVisibility(View.VISIBLE);
        } else {
            btn_cancle.setVisibility(View.GONE);
        }

        tv_total.setText(total_rs);
        tv_date.setText(getResources().getString(R.string.date) + date);
        tv_time.setText(getResources().getString(R.string.time) + time);
        tv_delivery_charge.setText(getResources().getString(R.string.delivery_charge) + deli_charge);
        tv_coupon_code.setText("Coupon Code : "+ coupon_code);
        tv_used_amount.setText("Coupon Used Amount : "+"₹ "+used_amont);

        // check internet connection
        if (ConnectivityReceiver.isConnected()) {
            makeGetOrderDetailRequest(sale_id);
        } else {
            Toast.makeText(MyOrderDetail.this, "Error Network Issues", Toast.LENGTH_SHORT).show();
            // ((MainActivity) getApplication()).onNetworkConnectionChanged(false);
        }

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog();
            }
        });

    }

    // alertdialog for cancle order
    private void showDeleteDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderDetail.this);
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                 sessionManagement = new Session_management(MyOrderDetail.this);
                String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {
                    makeDeleteOrderRequest(sale_id, user_id);
                }

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderDetailRequest(String sale_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_order_detail_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                Baseurl.ORDER_DETAIL_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                Gson gson = new Gson();
                Type listType = new TypeToken<List<My_order_detail_model>>() {
                }.getType();

                my_order_detail_modelList = gson.fromJson(response.toString(), listType);

                My_order_detail_adapter adapter = new My_order_detail_adapter(my_order_detail_modelList);
                rv_detail_order.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                if (my_order_detail_modelList.isEmpty()) {
                    Toast.makeText(MyOrderDetail.this, getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(MyOrderDetail.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeDeleteOrderRequest(String sale_id, String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_delete_order_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("sale_id", sale_id);
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.DELETE_ORDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("message");
                        Toast.makeText(MyOrderDetail.this, "" + msg, Toast.LENGTH_SHORT).show();

                        // ((MainActivity) getActivity()).onBackPressed();

                        Intent intent=new Intent(MyOrderDetail.this,My_Order_activity.class);
                        startActivity(intent);

                    } else {
                        String error = response.getString("error");
                        Toast.makeText(MyOrderDetail.this, "" + error, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MyOrderDetail.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
}
