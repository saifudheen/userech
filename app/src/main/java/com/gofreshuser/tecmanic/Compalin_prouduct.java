package com.gofreshuser.tecmanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Adapter.GetComplain_adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Show_sub_cat_product;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.RecyclerTouchListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compalin_prouduct extends AppCompatActivity {
    ShimmerRecyclerView show_cat_product;

    private GetComplain_adapter show_subadapter;
    SwipeRefreshLayout refresh_layout;
    Toolbar toolbar;
    int padding = 0;
    ImageView back_button;

    ProgressDialog progressDialog;
    Handler handler = new Handler();

    Runnable timeCounter;

    private List<Show_sub_cat_product> list_subcat = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compalin_prouduct);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Products");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        show_cat_product = findViewById(R.id.show_sub_product);

        LinearLayoutManager gridLayoutManagercat1 = new LinearLayoutManager(Compalin_prouduct.this, LinearLayoutManager.VERTICAL, false);

        progressDialog = new ProgressDialog(this);

        show_cat_product.setLayoutManager(gridLayoutManagercat1);

        show_cat_product.setItemAnimator(new DefaultItemAnimator());

        show_cat_product.setNestedScrollingEnabled(false);

        show_cat_product.smoothScrollToPosition(0);

        show_cat_product.scrollBy(10, 10);

        show_cat_product.scrollTo(10, 10);

        show_cat_product.showShimmerAdapter();
        refresh_layout = findViewById(R.id.refresh_layout);
        showproducts(getIntent().getStringExtra("order_id"));
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (handler != null && timeCounter != null) {
                    handler.removeCallbacks(timeCounter);
                }
                showproducts(getIntent().getStringExtra("order_id"));
                show_cat_product.showShimmerAdapter();
                // getRestList();

                refresh_layout.setRefreshing(false);

            }
        });

        show_cat_product.addOnItemTouchListener(new RecyclerTouchListener(Compalin_prouduct.this, show_cat_product, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent=new Intent(Compalin_prouduct.this,ComplaintActivity.class);
                intent.putExtra("order",getIntent().getStringExtra("order_id"));
                intent.putExtra("product_id",list_subcat.get(position).getProduct_id());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void showproducts(String order_id) {

        list_subcat.clear();

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("order_id", order_id);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Get_product_complain, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {

                        progressDialog.dismiss();

                        show_cat_product.hideShimmerAdapter();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Show_sub_cat_product>>() {
                        }.getType();
                        list_subcat = gson.fromJson(response.getString("list"), listType);
                        show_subadapter = new GetComplain_adapter(list_subcat);
                        show_cat_product.setAdapter(show_subadapter);
                        show_subadapter.notifyDataSetChanged();


                    } else {
                        progressDialog.dismiss();
                        show_cat_product.hideShimmerAdapter();

                        Toast.makeText(Compalin_prouduct.this, "No Data", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Compalin_prouduct.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);


    }

}