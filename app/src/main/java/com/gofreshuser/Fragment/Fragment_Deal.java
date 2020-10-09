package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
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
import com.gofreshuser.Adapter.Deal_Adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Deal_model;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
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

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
    public class Fragment_Deal extends Fragment {

    ShimmerRecyclerView show_cat;

    String value,sorttext;
    private List<Deal_model> list = new ArrayList<>();

    private Deal_Adapter adapter;
     String productid;
      ProgressDialog progressDialog;
    SharedPreferences preferences,storeprefrences;
      String storeid,title;
      LinearLayout sort;
     SwipeRefreshLayout refresh_layout;
     TextView sorttextview;
     Handler handler = new Handler();

    Runnable timeCounter;

    public Fragment_Deal() {
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

        View view= inflater.inflate(R.layout.fragment_fragment__deal, container, false);
        preferences=getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
           sorttextview=view.findViewById(R.id.sorttext);
        value=preferences.getString("value","5");
                show_cat = view.findViewById(R.id.show_sub__cat);

                sort=view.findViewById(R.id.sort);
        storeprefrences=getActivity().getSharedPreferences("sroreprefer",MODE_PRIVATE);
        storeid = storeprefrences.getString("store_id","");

//        title=getArguments().getString("title");
        MainActivity.countshow();
sorttext=preferences.getString("sort","Sort by");

sorttextview.setText(sorttext);
        LinearLayoutManager gridLayoutManagercat1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        show_cat.setLayoutManager(gridLayoutManagercat1);
       show_cat.setHasFixedSize(true);
        show_cat.showShimmerAdapter();




//        show_cat.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(3), true));

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener()
//
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    Fragment fm = new Fragment_StoreCat();
//                    Bundle args = new Bundle();
//                    fm.setArguments(args);
//                    FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.main_container, fm)
//                            .addToBackStack(null).commit();
//
//                    return true;
//                }
//                return false;
//            }
//        });


        sort.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
                Fragment fm = new Fragment_FilterSort();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();


    }
});


        if (ConnectivityReceiver.isConnected()){
            dealshow();

        }
        else {
            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();
        }


        refresh_layout = view.findViewById(R.id.refresh_layout);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (handler != null && timeCounter != null) {
                    handler.removeCallbacks(timeCounter);
                }
                show_cat.showShimmerAdapter();

                dealshow();
                // getRestList();

                refresh_layout.setRefreshing(false);

            }
        });


        return view;

    }

    private void dealshow() {

        list.clear();

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("s_id", storeid);
        params.put("order", value);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_Deal_product, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {

                        show_cat.hideShimmerAdapter();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Deal_model>>() {
                        }.getType();
                        list= gson.fromJson(response.getString("Deal_of_the_day"), listType);
                        adapter = new Deal_Adapter(list);
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
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);


    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        //Defining retrofit api service

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            int position = parent.getChildAdapterPosition(view); // item position

            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {

                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}