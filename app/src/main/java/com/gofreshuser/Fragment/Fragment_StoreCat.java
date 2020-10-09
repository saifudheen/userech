package com.gofreshuser.Fragment;


import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Adapter.Banner_Adapter;
import com.gofreshuser.Adapter.Cat_Adapter;
import com.gofreshuser.Adapter.Charges_adapter;
import com.gofreshuser.Adapter.RecommnededAdapter;
import com.gofreshuser.Adapter.SilderAdapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Category_model;
import com.gofreshuser.model.Charges_model;
import com.gofreshuser.model.RecommendedModel;
import com.gofreshuser.model.Sildermodel;
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
import java.util.Timer;
import java.util.TimerTask;

import static com.google.android.gms.plus.PlusOneDummyView.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_StoreCat extends Fragment {

    ShimmerRecyclerView show_cat,recommend_product;
    private List<Category_model> list = new ArrayList<>();
    private List<RecommendedModel> recommend_list = new ArrayList<>();
    ImageView center_iv,full_iv;
    private Cat_Adapter adapter;
    RecommnededAdapter recommnededAdapter;
    SharedPreferences preferences,store_idpref,store_pref;
    SharedPreferences.Editor editor,store_editor;
    ViewPager image_slider,banner_slider;
    String storeid,count;
     Button discount,freedelivery;
     ShimmerRecyclerView rc_cartcharges;
    Charges_adapter charges_adapter;
     ProgressDialog progressDialog;


    List<Charges_model> charges_modelList=new ArrayList<>();
    private List<Sildermodel> sildermodelslist,bannermodelist;

    TextView seeall;


    private static int currentPage = 0;
    private static int currentPage1 = 0;
    private static int NUM_PAGES = 0;
    private static int NUM_PAGES1 = 0;
    SwipeRefreshLayout refresh_layout;
    Handler handler = new Handler();
    Runnable timeCounter;

    public Fragment_StoreCat() {
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

        View view= inflater.inflate(R.layout.fragment_fragment__store_cat, container, false);
       ((MainActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        MainActivity.countshow();
        recommend_product=view.findViewById(R.id.recoomend_product);
        seeall=view.findViewById(R.id.seeall);
        show_cat=view.findViewById(R.id.show_cat);
        discount=view.findViewById(R.id.discount);
        center_iv=view.findViewById(R.id.center_iv);
        full_iv=view.findViewById(R.id._full_iv);
        freedelivery=view.findViewById(R.id.free);
        preferences=getActivity().getSharedPreferences("filter", Context.MODE_PRIVATE);
        store_idpref=getActivity().getSharedPreferences("sroreprefer",Context.MODE_PRIVATE);
        store_pref=getActivity().getSharedPreferences("store_pref",Context.MODE_PRIVATE);
store_editor=store_pref.edit();
        image_slider = (ViewPager) view.findViewById(R.id.home_img_slider);
          banner_slider=(ViewPager) view.findViewById(R.id.banner_slider);
        storeid = store_idpref.getString("store_id","");
         store_editor.putString("storeid",storeid);
        sildermodelslist=new ArrayList<>();

        bannermodelist=new ArrayList<>();

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        show_cat.setLayoutManager(gridLayoutManagercat);

        show_cat.setHasFixedSize(true);

        show_cat.setItemAnimator(new DefaultItemAnimator());

        show_cat.setHasFixedSize(true);
        show_cat.smoothScrollToPosition(0);
        show_cat.scrollBy(10,10);
        show_cat.scrollTo(10,10);

        show_cat.setNestedScrollingEnabled(false);
        show_cat.showShimmerAdapter();

        Glide.with(getActivity())
                .load(Baseurl.IMG_PROFILE_URL + getArguments().getString("store_image"))
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(center_iv);

        Glide.with(getActivity())
                .load(Baseurl.IMG_PROFILE_URL + getArguments().getString("banner_image"))
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(full_iv);

        LinearLayoutManager gridLayoutManagercrec = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recommend_product.setLayoutManager(gridLayoutManagercrec);

        recommend_product.setHasFixedSize(true);

        recommend_product.setItemAnimator(new DefaultItemAnimator());

        recommend_product.setHasFixedSize(true);

        recommend_product.smoothScrollToPosition(0);

        recommend_product.scrollBy(10,10);

        recommend_product.scrollTo(10,10);

        recommend_product.setNestedScrollingEnabled(false);

        recommend_product.showShimmerAdapter();

        setHasOptionsMenu(true);

        editor=preferences.edit();

        discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.clear();
                Fragment fm = new Fragment_Deal();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });


        freedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fragment_delivery_charrges);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                dialog.show();



                rc_cartcharges=dialog.findViewById(R.id.rc_cartcharges);
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



        });

seeall.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Fragment fm = new Fragment_Seeall_cat();
        Bundle args = new Bundle();
        args.putString("store_id",storeid);
        fm.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                .addToBackStack(null).commit();

    }
});
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

        recommend_product.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recommend_product, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//
//                Bundle args = new Bundle();
                Bundle args = new Bundle();
                Fragment fm = new FragmentProductShow();
                args.putString("productimage", recommend_list.get(position).getProduct_image());
                args.putString("productname",recommend_list.get(position).getProduct_name());
                args.putString("product_id",recommend_list.get(position).getProduct_id());
                args.putString("description",recommend_list.get(position).getProduct_description());
                args.putString("price",recommend_list.get(position).getPrice());
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        if (ConnectivityReceiver.isConnected()){

            catshow();

            makeGetSliderRequest();

            getbanner();
            recommendshow();

        }
        else {

            Toast.makeText(getActivity(), "No Connection", Toast.LENGTH_SHORT).show();


        }

        refresh_layout = view.findViewById(R.id.refresh_layout);

        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(handler!=null && timeCounter!=null){
                    handler.removeCallbacks(timeCounter);
                }
                show_cat.showShimmerAdapter();


                catshow();

                makeGetSliderRequest();

                getbanner();

                recommendshow();
                refresh_layout.setRefreshing(false);

            }
        });


        return view;

    }
    private void catshow(){
        list.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_Cat_store, params, new Response.Listener<JSONObject>() {

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
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);

    }
    private void recommendshow(){
        recommend_list.clear();
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Reccmmend_product, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    if (response != null && response.length() > 0) {

                        recommend_product.hideShimmerAdapter();
                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<RecommendedModel>>() {
                        }.getType();
                        recommend_list= gson.fromJson(response.getString("recommended_product"), listType);
                        recommnededAdapter = new RecommnededAdapter(recommend_list);
                        recommend_product.setAdapter(recommnededAdapter);
                        recommnededAdapter.notifyDataSetChanged();
                    }
                    else {
                        recommend_product.hideShimmerAdapter();
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

    private void makeGetSliderRequest() {
        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.GET_SLIDER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                           count=response.getString("count");
                           JSONArray jsonArray=response.getJSONArray("data");
                           for (int i=0;i<jsonArray.length();i++){
                               JSONObject jsonObject=jsonArray.getJSONObject(i);
                               String slider_image=jsonObject.getString("slider_image");
                               sildermodelslist.add(new Sildermodel(slider_image, slider_image));


                           }
                    NUM_PAGES = sildermodelslist.size();

                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            image_slider.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 20000, 20000);
if (count.contains("0")){
    image_slider.setVisibility(View.GONE);
}
else {
    SilderAdapter silderAdapter1 = new SilderAdapter(getActivity(), sildermodelslist);

    image_slider.setAdapter(silderAdapter1);

}





                } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    private void getbanner() {

        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.GET_BANNER_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {
                    count=response.getString("count");
                    JSONArray jsonArray=response.getJSONArray("data");
                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String slider_image=jsonObject.getString("slider_image");

                        bannermodelist.add(new Sildermodel(slider_image, slider_image));


                    }

                    NUM_PAGES1 = bannermodelist.size();

                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {

                            if (currentPage1 == NUM_PAGES1) {
                                currentPage1 = 0;
                            }

                            banner_slider.setCurrentItem(currentPage1++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 10000, 10000);
                    if (count.contains("0")){
                        banner_slider.setVisibility(View.GONE);
                    }
                    else {

                        Banner_Adapter silderAdapter2 = new Banner_Adapter(getActivity(), bannermodelist);

                        banner_slider.setAdapter(silderAdapter2);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

}
