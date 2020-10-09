package com.gofreshuser.Fragment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
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
import com.gofreshuser.Adapter.SearchAdapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.SearchDataModel;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.RecyclerTouchListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSearch extends Fragment {

    String getallstoreid;
    SearchView searchView;
    String search_item;
    String product_name,price,product_id,user_fullname;
    private List<SearchDataModel> recommend_list = new ArrayList<>();
    ArrayList<String> data=new ArrayList<>();
    SharedPreferences locationpre;
    SearchAdapter recommnededAdapter;
    RecyclerView storesearch_show;
    JSONArray jsonArray;
    String lat,lng;
    public FragmentSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.activity_search, container, false);
        searchView = view.findViewById(R.id.search);

        storesearch_show = view.findViewById(R.id.search_rc);

        jsonArray = new JSONArray();
      data=getArguments().getStringArrayList("getdata");

        LinearLayoutManager gridLayoutManagercat = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        storesearch_show.setLayoutManager(gridLayoutManagercat);

        storesearch_show.setHasFixedSize(true);

        storesearch_show.setItemAnimator(new DefaultItemAnimator());
        storesearch_show.setNestedScrollingEnabled(false);

        storesearch_show.smoothScrollToPosition(0);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint(Html.fromHtml("<font color = #aaaaaa>" + "Search for Store Products" + "</font>"));
        locationpre=getActivity().getSharedPreferences("location",MODE_PRIVATE);

        lat=locationpre.getString("lat","");

        lng=locationpre.getString("lng","");


        storesearch_show.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), storesearch_show, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//
                Bundle args = new Bundle();
                Fragment fm = new FragmentProductShow();
                args.putString("productimage", recommend_list.get(position).getProduct_image());
                args.putString("productname",recommend_list.get(position).getProduct_name());
                args.putString("product_id",recommend_list.get(position).getProduct_id());
                args.putString("description",recommend_list.get(position).getProduct_description());
                args.putString("price",recommend_list.get(position).getPrice());
                fm.setArguments(args);
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        try {
            JSONObject pnObj = new JSONObject();
            recommend_list.clear();

            for (int i=0;i<data.size();){
                pnObj.put("store_id",data.get(i));

                jsonArray.put(pnObj);


                i++;


            }






        } catch (JSONException e) {
            e.printStackTrace();

        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                if (s.length()> 0) {


                    search_item = s.replace(" ", "%20");
                    storesearch_show.setVisibility(View.VISIBLE);
                    searchshow(search_item);




                } else if (s.length()==0){
                    storesearch_show.setVisibility(View.GONE);

                }

                return false;
            }
        });
        return view;
    }
    private void searchshow(String s) {



        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("search", s);

        params.put("lat", lat);

        params.put("lng",lng);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                Baseurl.Suggestion_api, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());

                try {

                    if (response != null && response.length() > 0) {


                        JSONArray data=response.getJSONArray("data");
                        recommend_list.clear();
                        for (int i=0;i<data.length();){


                            JSONObject result=data.getJSONObject(i);

                            JSONArray product_array=result.getJSONArray("product");

                            if (product_array.length()>0 || product_array!=null){
//                                JSONObject result_obj=product_array.getJSONObject(i);
//                                product_name=result_obj.getString("product_name");


                                for (int j=0;j<product_array.length();){
//
//



                                        JSONObject result_obj = product_array.getJSONObject(j);
//
                                        product_name = result_obj.getString("product_name");
//
                                        price = result_obj.getString("price");

                                        product_id = result_obj.getString("product_id");

                                        user_fullname = result_obj.getString("user_fullname");


                                        SearchDataModel searchDataModel=new SearchDataModel();

                                        searchDataModel.setProduct_id(product_id);

                                        searchDataModel.setUser_fullname(user_fullname);

                                        searchDataModel.setProduct_name(product_name);
//
                                        searchDataModel.setPrice(price);

                                        searchDataModel.setProduct_image(result_obj.getString("product_image"));

                                        searchDataModel.setProduct_description(result_obj.getString("product_description"));
//
//
//
                                        recommend_list.add(searchDataModel);
                                    j++;

                                }
i++;

                                recommnededAdapter=new SearchAdapter(recommend_list);

                                storesearch_show.setAdapter(recommnededAdapter);

                                recommnededAdapter.notifyDataSetChanged();


                            }
                            else {
                                Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                            }



                        }

                    }
                    else {


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
