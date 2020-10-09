package com.gofreshuser.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Adapter.My_Pending_Order_adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.My_Pending_order_model;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonArrayRequest;
import com.gofreshuser.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class My_Pending_Order extends Fragment {

    private static String TAG = My_Pending_Order.class.getSimpleName();

    private RecyclerView rv_myorder;

    private List<My_Pending_order_model> my_order_modelList = new ArrayList<>();
    TabHost tHost;

    public My_Pending_Order() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_pending_order, container, false);


        // handle the touch event if true
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // check user can press back button or not
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {



                    Intent intent=new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

//                    Fragment fm = new Home_fragment();
//                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm)
//                            .addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });

        rv_myorder = (RecyclerView) view.findViewById(R.id.rv_myorder);
        rv_myorder.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_myorder.setHasFixedSize(true);

        Session_management sessionManagement = new Session_management(getActivity());
        String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

        // check internet connection
        if (ConnectivityReceiver.isConnected())

        {
            makeGetOrderRequest(user_id);
        } else

        {
//            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }


        return view;
    }

    /**
     * Method to make json array request where json response starts wtih
     */
    private void makeGetOrderRequest(String userid) {

        my_order_modelList.clear();

        String tag_json_obj = "json_socity_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                Baseurl.GET_ORDER_URL, params, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());

                for (int i =0;i<response.length();i++){

                    try {

                        JSONObject jsonObject = response.getJSONObject(i);

                        String sale_id = jsonObject.getString("sale_id");

                        String user_id = jsonObject.getString("user_id");

                        String on_date= jsonObject.getString("on_date");
                          String coupon_code=jsonObject.getString("coupon_code");
                          String previous_coupon=jsonObject.getString("previous_amount");
                          String used_amount=jsonObject.getString("used_amount");
                        String delivery_time_from= jsonObject.getString("delivery_time_from");
                        String delivery_time_to= jsonObject.getString("delivery_time_to");
                        String status = jsonObject.getString("status");
                        String note =jsonObject.getString("note");
                        String is_paid = jsonObject.getString("is_paid");
                        String total_amount = jsonObject.getString("total_amount");
                         String delivery_boy_name = jsonObject.getString("delivery_boy_name");
                        String total_kg = jsonObject.getString("total_kg");
                        String total_items = jsonObject.getString("total_items");
                        String socity_id= jsonObject.getString("socity_id");
                        String delivery_address = jsonObject.getString("delivery_address");
                        String location_id = jsonObject.getString("location_id");
                        String delivery_charge = jsonObject.getString("delivery_charge");
                        String delivery_number = jsonObject.getString("delivery_boy_phone");
                        String assign_to = jsonObject.getString("assign_to");
                        String payment_method = jsonObject.getString("payment_method");

                        String delivery_boy_lat = jsonObject.getString("delivery_boy_lat");

                        String delivery_boy_lng = jsonObject.getString("delivery_boy_lng");
                       String vandorname=jsonObject.getString("vendor_name");
                        String lat = jsonObject.getString("lat");

                        String lng = jsonObject.getString("lng");
                        if(!total_items.equals("0")) {

                        My_Pending_order_model model = new My_Pending_order_model();

                        model.setNote(note);
                        model.setCoupon_code(coupon_code);
                        model.setUsed_amount(used_amount);
                        model.setPrevious_amount(previous_coupon);


                        model.setAssgin_to(assign_to);
                        model.setDelivery_address(delivery_address);
                        model.setDelivery_charge(delivery_charge);
                        model.setDelivery_time_to(delivery_time_to);
                        model.setDelivery_time_from(delivery_time_from);
                        model.setIs_paid(is_paid);
                        model.setLocation_id(location_id);
                        model.setLat(lat);
                        model.setVandor_name(vandorname);
                        model.setLng(lng);
                        model.setStatus(status);
                        model.setSale_id(sale_id);
                        model.setSocity_id(socity_id);
                        model.setPayment_method(payment_method);
                        model.setOn_date(on_date);
                        model.setTotal_amount(total_amount);
                        model.setTotal_items(total_items);
                        model.setTotal_kg(total_kg);
                        model.setUser_id(user_id);

                        if (delivery_number!=null && !delivery_number.isEmpty() && !delivery_number.equals("null")){

                            model.setDelivery_number(delivery_number);

                        }
                        else {
                            model.setDelivery_number("");

                        }
                        if(delivery_boy_name!=null){

                            model.setDelivery_boy_name(delivery_boy_name);

                        }
                        else {
                            model.setDelivery_boy_name("");

                        }
                        if (delivery_boy_lat!=null){

                            model.setDelivery_boy_lat(delivery_boy_lat);

                        }
                        else {
                            model.setDelivery_boy_lat("");

                        }

                        if (delivery_boy_lng!=null){

                            model.setDelivery_boy_lng(delivery_boy_lng);
                        }
                        else {
                            model.setDelivery_boy_lng("");
                        }

                        my_order_modelList.add(model);

                        My_Pending_Order_adapter myPendingOrderAdapter = new My_Pending_Order_adapter(my_order_modelList);

                        rv_myorder.setAdapter(myPendingOrderAdapter);

                        myPendingOrderAdapter.notifyDataSetChanged();
                        }

                        if (my_order_modelList.isEmpty()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                        }
                        

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

//                Gson gson = new Gson();
//                Type listType = new TypeToken<List<My_Pending_order_model>>() {
//                }.getType();
//
//                my_order_modelList = gson.fromJson(response.toString(), listType);
//                My_Pending_Order_adapter myPendingOrderAdapter = new My_Pending_Order_adapter(my_order_modelList);
//                rv_myorder.setAdapter(myPendingOrderAdapter);
//                myPendingOrderAdapter.notifyDataSetChanged();//

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
