package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Adapter.Charges_adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Charges_model;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Delivery_charrges extends Fragment {
ShimmerRecyclerView rc_cartcharges;
Charges_adapter charges_adapter;
ProgressDialog progressDialog;
SharedPreferences store_idpref;
String storeid;
List<Charges_model> charges_modelList=new ArrayList<>();

    public Delivery_charrges() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_delivery_charrges, container, false);
        store_idpref=getActivity().getSharedPreferences("sroreprefer", Context.MODE_PRIVATE);
      storeid=store_idpref.getString("store_id","");
        rc_cartcharges=view.findViewById(R.id.rc_cartcharges);
progressDialog=new ProgressDialog(getActivity());
progressDialog.setMessage("Loading");
progressDialog.show();
        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rc_cartcharges.setLayoutManager(gridLayoutManagercat);

        rc_cartcharges.setHasFixedSize(true);

        rc_cartcharges.setItemAnimator(new DefaultItemAnimator());

        rc_cartcharges.setHasFixedSize(true);
        rc_cartcharges.smoothScrollToPosition(0);
        rc_cartcharges.scrollBy(10,10);
        rc_cartcharges.scrollTo(10,10);

        rc_cartcharges.setNestedScrollingEnabled(false);
        rc_cartcharges.showShimmerAdapter();
               getcharges();
        return view;

    }
    private void getcharges(){
        charges_modelList.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Get_delivery_chargesapi, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {
progressDialog.dismiss();
                        rc_cartcharges.hideShimmerAdapter();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Charges_model>>() {
                        }.getType();
                        charges_modelList= gson.fromJson(response.getString("charges"), listType);
                        charges_adapter = new Charges_adapter(charges_modelList);
                        rc_cartcharges.setAdapter(charges_adapter);
                        charges_adapter.notifyDataSetChanged();
                    }
                    else {
                        progressDialog.dismiss();
                        rc_cartcharges.hideShimmerAdapter();
                        Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
