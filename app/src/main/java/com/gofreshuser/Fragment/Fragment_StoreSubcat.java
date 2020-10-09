package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Adapter.Show_sub_cat_adapter;
import com.gofreshuser.Adapter.Sub_CatAdapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Show_sub_cat_product;
import com.gofreshuser.model.Sub_catmodel;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.RecyclerTouchListener;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_StoreSubcat extends Fragment {

    ShimmerRecyclerView show_cat;

    ShimmerRecyclerView show_cat_product;

    String sub_id;

    private List<Sub_catmodel> list = new ArrayList<>();

    private List<Show_sub_cat_product> list_subcat = new ArrayList<>();

    private Show_sub_cat_adapter show_subadapter;

    private Sub_CatAdapter adapter;

    String value, sorttext;

    String cat_id;

    ProgressDialog progressDialog;

    String storeid, title;

    SharedPreferences preferences,store_pref;

    LinearLayout sort;

    SwipeRefreshLayout refresh_layout;

    TextView sorttextview;

    Handler handler = new Handler();
     String cat_first_id;
    Runnable timeCounter;

    public Fragment_StoreSubcat() {
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
        View view = inflater.inflate(R.layout.fragment_fragment__store_subcat, container, false);

        MainActivity.countshow();
        show_cat = view.findViewById(R.id.show_sub__cat);

        preferences=getActivity().getSharedPreferences("store_filter", Context.MODE_PRIVATE);
        store_pref=getActivity().getSharedPreferences("sroreprefer", Context.MODE_PRIVATE);

        sorttextview=view.findViewById(R.id.sorttext);
        value=preferences.getString("value","5");
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Please Wait");


        sorttextview = view.findViewById(R.id.sorttext);

        sort = view.findViewById(R.id.sort);

        sorttext=preferences.getString("sort","Sort by");

        sorttextview.setText(sorttext);
//

        storeid = store_pref.getString("store_id","");

        title = getArguments().getString("title");


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fm = new Fragment_storesort();
                Bundle args = new Bundle();
                fm.setArguments(args);

                args.putString("title", title);
//                store_editor.putString("title",list.get(position).getTitle());
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();


            }
        });


        sorttextview.setText(sorttext);


        show_cat_product = view.findViewById(R.id.show_sub_product);

//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


        show_cat_product.setLayoutManager(gridLayoutManager);


        show_cat_product.setHasFixedSize(true);

        show_cat_product.showShimmerAdapter();

        show_cat.showShimmerAdapter();

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        show_cat.setLayoutManager(gridLayoutManagercat);


        show_cat.setItemAnimator(new DefaultItemAnimator());

        show_cat.setNestedScrollingEnabled(false);

        show_cat.smoothScrollToPosition(0);

        show_cat.scrollBy(10, 10);

        show_cat.scrollTo(10, 10);


        show_cat.setHasFixedSize(true);






        show_cat.showShimmerAdapter();



        show_cat.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), show_cat, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                cat_id = list.get(position).getSub_id();
                showproducts(cat_id);
                progressDialog.show();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        if (ConnectivityReceiver.isConnected()) {
            catshow();

        } else {
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        }


//        refresh_layout = view.findViewById(R.id.refresh_layout);
//
//        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (handler != null && timeCounter != null) {
//                    handler.removeCallbacks(timeCounter);
//                }
//                catshow();
//                show_cat_product.showShimmerAdapter();
//                // getRestList();
//                show_cat.showShimmerAdapter();
//
//                refresh_layout.setRefreshing(false);
//
//            }
//        });


        return view;

    }

    private void catshow() {

        list.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("store_id", storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_Cat_store, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {


                        show_cat_product.hideShimmerAdapter();
                        JSONArray jsonArray = response.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String titleshow = jsonObject.getString("title");
                            Log.d("gdfgdf",titleshow);
                            if (title.contains(titleshow)) {

                                JSONArray subarray = jsonObject.getJSONArray("sub_cat");

                                for (int j = 0; j < subarray.length(); j++) {

                                    JSONObject subobject = subarray.getJSONObject(j);
                                    sub_id = subobject.getString("id");
                                    String title = subobject.getString("title");
                                    String image = subobject.getString("image");
                                    Sub_catmodel sub_catmodel = new Sub_catmodel();
                                    sub_catmodel.setSub_id(sub_id);

                                    sub_catmodel.setImage(image);
                                    sub_catmodel.setTitle(title);

                                    list.add(sub_catmodel);

                                }

                            }
                        }
                        adapter = new Sub_CatAdapter(list);
                        show_cat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                      show_cat.hideShimmerAdapter();
                        cat_first_id=list.get(0).getSub_id();
                        showproducts(cat_first_id);

                    } else {

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

    private void showproducts(String cat_id) {

        list_subcat.clear();

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("cat_id", cat_id);

        params.put("order",value);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.UGet_produtcs, params, new Response.Listener<JSONObject>() {

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
                            list_subcat = gson.fromJson(response.getString("data"), listType);
                            show_subadapter = new Show_sub_cat_adapter(list_subcat);
                            show_cat_product.setAdapter(show_subadapter);
                            show_subadapter.notifyDataSetChanged();


                        } else {
                            progressDialog.dismiss();
                            show_cat_product.hideShimmerAdapter();

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
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);


    }




}