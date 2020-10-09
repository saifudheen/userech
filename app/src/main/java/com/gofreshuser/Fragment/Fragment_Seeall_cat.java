package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Adapter.Cat_Adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Category_model;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Seeall_cat extends Fragment {

    ShimmerRecyclerView show_cat;
    private List<Category_model> list = new ArrayList<>();
    private Cat_Adapter adapter;
    SharedPreferences store_pref;
    SharedPreferences.Editor store_editor;

    String storeid;
    public Fragment_Seeall_cat() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment__seeall_cat, container, false);
        storeid=getArguments().getString("store_id");
        show_cat=view.findViewById(R.id.show_cat);



        LinearLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 2);
        show_cat.setLayoutManager(gridLayoutManager2);
        show_cat.setItemAnimator(new DefaultItemAnimator());
        show_cat.setNestedScrollingEnabled(false);
        show_cat.smoothScrollToPosition(0);

        show_cat.scrollBy(10,10);
        show_cat.scrollTo(10,10);
        MainActivity.countshow();
        show_cat.showShimmerAdapter();
        if (ConnectivityReceiver.isConnected()){
            catshow();

        }
        else {
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        }

        store_pref=getActivity().getSharedPreferences("store_pref",Context.MODE_PRIVATE);

        store_editor=store_pref.edit();

        show_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), show_cat, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//
//                Bundle args = new Bundle();
                Fragment fm = new Fragment_StoreSubcat();
                Bundle args = new Bundle();
                args.putString("title", list.get(position).getTitle());
//                store_editor.putString("title",list.get(position).getTitle());
                store_editor.commit();
                args.putString("store",storeid);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return view;

    }
    private void catshow(){
        list.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.GET_ALLCat, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {

                        show_cat.hideShimmerAdapter();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Category_model>>() {
                        }.getType();
                        list= gson.fromJson(response.getString("data"), listType);
                        adapter = new Cat_Adapter(list);
                        show_cat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        show_cat.hideShimmerAdapter();
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
    }

}
