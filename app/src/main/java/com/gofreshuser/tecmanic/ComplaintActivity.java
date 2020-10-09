package com.gofreshuser.tecmanic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.gofreshuser.Adapter.ComplainAdapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.ComplainModel;
import com.gofreshuser.util.CustomVolleyJsonArrayRequest;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.RecyclerTouchListener;
import com.gofreshuser.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplaintActivity extends AppCompatActivity {
    ShimmerRecyclerView rc_complain;
    ComplainModel complainModel;
    ComplainAdapter complainAdapter;
    Toolbar toolbar;
    int padding = 0;
    ImageView back_button;
    String complainid;
ProgressDialog progressDialog;
    List<ComplainModel> complainModels=new ArrayList<>();
    String complain;
    Session_management session_management;
    String store_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_complain);
        session_management=new Session_management(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Complain");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

progressDialog=new ProgressDialog(this);
progressDialog.setMessage("Loading");
        rc_complain=findViewById(R.id.rc_complain);
        LinearLayoutManager gridLayoutManagercat1 = new LinearLayoutManager(ComplaintActivity.this, LinearLayoutManager.VERTICAL, false);


        rc_complain.setLayoutManager(gridLayoutManagercat1);

        rc_complain.setItemAnimator(new DefaultItemAnimator());

        rc_complain.setNestedScrollingEnabled(false);

        rc_complain.smoothScrollToPosition(0);

        rc_complain.scrollBy(10, 10);

        rc_complain.scrollTo(10, 10);

        rc_complain.showShimmerAdapter();
        rc_complain.setHasFixedSize(true);


        rc_complain.addOnItemTouchListener(new RecyclerTouchListener(ComplaintActivity.this, rc_complain, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//
//
             complainid=complainModels.get(position).getComplainid();
progressDialog.show();
             getcompain(complainid);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        complain_ques();

    }
    private void complain_ques() {

        complainModels.clear();

        String tag_json_obj = "json_category_req";



        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.GET,
                Baseurl.Compalin, null, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {

                        rc_complain.hideShimmerAdapter();


                        JSONObject jsonObject=null;


                        int i = 0;

                        complainModels.clear(); //Clear your array list before adding new data in it

                        for (i=0;i<response.length();){
                            jsonObject=response.getJSONObject(i);

                            complain=jsonObject.getString("complain");

                            complainModel=new ComplainModel();

                            complainModel.setComplain(complain);
                            complainModel.setComplainid(jsonObject.getString("complain_id"));


                            complainModels.add(complainModel);

                            i++;


                        }





                        complainAdapter = new ComplainAdapter(complainModels);


                        rc_complain.setAdapter(complainAdapter);
                        complainAdapter.notifyDataSetChanged();



                    } else {
                        rc_complain.hideShimmerAdapter();

                        Toast.makeText(ComplaintActivity.this, "No Data", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(ComplaintActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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

    private void getcompain(String complain_idd){

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("complain_id",complain_idd);

        params.put("product_id",getIntent().getStringExtra("product_id"));

        params.put("user_id",session_management.getUserDetails().get(Baseurl.KEY_ID));

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Complain_check, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {

                        boolean response_value=response.getBoolean("responce");
                        if (response_value){
                            progressDialog.dismiss();
                            Intent intent=new Intent(ComplaintActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(ComplaintActivity.this, "Query Submit Successfully", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
//                            Toast.makeText(ComplaintActivity.this, "Query Submit Successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
//                        progressDialog.dismiss();
//                        animate.stopShimmer();
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
                    Toast.makeText(ComplaintActivity.this, getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);



    }

}
