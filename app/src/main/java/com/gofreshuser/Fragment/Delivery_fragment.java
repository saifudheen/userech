package com.gofreshuser.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.daimajia.swipe.util.Attributes;
import com.gofreshuser.Adapter.Delivery_get_address_adapter;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Delivery_address_model;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Rajesh Dabhi on 27/6/2017.
 */

public class Delivery_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Delivery_fragment.class.getSimpleName();

     TextView tv_afternoon, tv_morning, tv_total, tv_item, tv_socity;
    private TextView tv_date, tv_time;
    private EditText et_address;
    private RelativeLayout btn_checkout, tv_add_adress;
    private RecyclerView rv_address;

    private Delivery_get_address_adapter adapter;
    private List<Delivery_address_model> delivery_address_modelList = new ArrayList<>();



    private Session_management sessionManagement;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private String gettime = "";
    private String getdate = "";
    SharedPreferences storeprefrences;
    private String deli_charges;
    String store_id;
String total_item,total_price;
    public Delivery_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_delivery_time, container, false);

//        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.delivery));



        storeprefrences=getActivity().getSharedPreferences("sroreprefer",MODE_PRIVATE);
        store_id = storeprefrences.getString("store_id","");

        MainActivity.countshow();
        tv_date = (TextView) view.findViewById(R.id.tv_deli_date);
        tv_time = (TextView) view.findViewById(R.id.tv_deli_fromtime);
        tv_add_adress = (RelativeLayout) view.findViewById(R.id.tv_deli_add_address);
        tv_total = (TextView) view.findViewById(R.id.tv_deli_total);
        tv_item = (TextView) view.findViewById(R.id.tv_deli_item);
        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_deli_checkout);
        rv_address = (RecyclerView) view.findViewById(R.id.rv_deli_address);
        rv_address.setLayoutManager(new LinearLayoutManager(getActivity()));

        rv_address.setHasFixedSize(true);

        sessionManagement = new Session_management(getActivity());

        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_add_adress.setOnClickListener(this);
        btn_checkout.setOnClickListener(this);

        String date = sessionManagement.getdatetime().get(Baseurl.KEY_DATE);
        String time = sessionManagement.getdatetime().get(Baseurl.KEY_TIME);


        showcart();
        if (date != null && time != null) {

            getdate = date;
            gettime = time;

            try {
                String inputPattern = "yyyy-MM-dd";
                String outputPattern = "dd-MM-yyyy";
                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = inputFormat.parse(getdate);
                String str = outputFormat.format(date1);

                tv_date.setText(str);

            } catch (ParseException e) {
                e.printStackTrace();

                tv_date.setText(getdate);
            }

            tv_time.setText(time);
        }

        if (ConnectivityReceiver.isConnected()) {
            String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);
            makeGetAddressRequest(user_id);
        } else {
//            ((MainActivity) getActivity()).onNetworkConnectionChanged(false);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_deli_checkout) {
            attemptOrder();
        } else if (id == R.id.tv_deli_date) {

            getdate();
        } else if (id == R.id.tv_deli_fromtime) {

            if (TextUtils.isEmpty(getdate)) {

                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date), Toast.LENGTH_SHORT).show();

            } else {
                Bundle args = new Bundle();
                Fragment fm = new View_time_fragment();
                args.putString("date", getdate);
                fm.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();
            }
        } else if (id == R.id.tv_deli_add_address) {

            sessionManagement.updateSocity("", "");

            sessionManagement.updateSocity("", "");

            Fragment fm = new Add_delivery_address_fragment();

            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                    .addToBackStack(null).commit();

        }

    }

    private void getdate() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getdate = "" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                        tv_date.setText(getdate);

                        try {
                            String inputPattern = "yyyy-MM-dd";
                            String outputPattern = "dd-MM-yyyy";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = inputFormat.parse(getdate);
                            String str = outputFormat.format(date);

                            tv_date.setText(str);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            tv_date.setText(getdate);
                        }

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

    }

    private void attemptOrder() {

        //String getaddress = et_address.getText().toString();

        String location_id = "";
        String address = "";

        boolean cancel = false;

        if (TextUtils.isEmpty(getdate)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
            cancel = true;
        } else if (TextUtils.isEmpty(gettime)) {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_select_date_time), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        if (!delivery_address_modelList.isEmpty()) {

            if (adapter.ischeckd()) {

                location_id = adapter.getlocation_id();

                address = adapter.getaddress();


            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
                cancel = true;
            }
        } else {
            Toast.makeText(getActivity(), getResources().getString(R.string.please_add_address), Toast.LENGTH_SHORT).show();
            cancel = true;
        }

        /*if (TextUtils.isEmpty(getaddress)) {
            Toast.makeText(getActivity(), "Please add your address", Toast.LENGTH_SHORT).show();
            cancel = true;
        }*/

        if (!cancel) {
            //Toast.makeText(getActivity(), "date:"+getdate+"Fromtime:"+getfrom_time+"Todate:"+getto_time, Toast.LENGTH_SHORT).show();

       CheckAddress(location_id,store_id,address);
        }
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetAddressRequest(String user_id) {

        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.GET_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        delivery_address_modelList.clear();

                        Gson gson = new Gson();
                        Type listType = new TypeToken<List<Delivery_address_model>>() {
                        }.getType();

                        delivery_address_modelList = gson.fromJson(response.getString("data"), listType);

                        //RecyclerView.Adapter adapter1 = new Delivery_get_address_adapter(delivery_address_modelList);
                        adapter = new Delivery_get_address_adapter(delivery_address_modelList);
                        ((Delivery_get_address_adapter) adapter).setMode(Attributes.Mode.Single);
                        rv_address.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        if (delivery_address_modelList.isEmpty()) {
                            if (getActivity() != null) {
                                //Toast.makeText(getActivity(), getResources().getString(R.string.no_rcord_found), Toast.LENGTH_SHORT).show();
                            }
                        }

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
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);

    }
    private void CheckAddress(final String locationid, final String storeid, final String addressshow) {

        // Tag used to cancel the request
        String tag_json_obj = "json_get_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("location_id", locationid);
        params.put("store_id",storeid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Check_address, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        sessionManagement.cleardatetime();

                        Bundle args = new Bundle();
                        Fragment fm = new Payment_fragment();
                        args.putString("getdate", getdate);
                        args.putString("gettime", gettime);
                        args.putString("total_amount",total_price);
                        args.putString("getlocationid", locationid);
                        args.putString("address", addressshow);
                        args.putString("deli_charges", deli_charges);
                        args.putString("getstoreid", storeid);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();

                    }
                    else {
                        Toast.makeText(getActivity(), "Delivery Not Available for this Address", Toast.LENGTH_SHORT).show();
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
                    if (getActivity() != null) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
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

    @Override
    public void onPause() {
        super.onPause();
        // unregister reciver
//        getActivity().unregisterReceiver(mCart);
    }

    @Override
    public void onResume() {
        super.onResume();
        // register reciver
//        getActivity().registerReceiver(mCart, new IntentFilter("Grocery_delivery_charge"));
    }

    // broadcast reciver for receive data
//    private BroadcastReceiver mCart = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//
//            String type = intent.getStringExtra("type");
//
//            if (type.contentEquals("update")) {
//                //updateData();
//                deli_charges = intent.getStringExtra("charge");
//                //Toast.makeText(getActivity(), deli_charges, Toast.LENGTH_SHORT).show();
//
//                Double total = Double.parseDouble(db_cart.getTotalAmount()) + Integer.parseInt(deli_charges);
//
//                tv_total.setText("" + db_cart.getTotalAmount() + " + " + deli_charges + " = " + getActivity().getResources().getString(R.string.currency) + total);
//            }
//        }
//    };
    private void showcart() {


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", sessionManagement.getUserDetails().get(Baseurl.KEY_ID));


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.View_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    if (response != null && response.length() > 0) {




                        total_item= response.getString("total_item");
                        total_price= response.getString("total_amount");

                        if (total_item.contains("0")){

                            tv_item.setText("0");
                            tv_total.setText("0");


                        }else {

                            tv_item.setText(total_item);
                            tv_total.setText("Rs."+total_price);

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
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
