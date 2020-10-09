package com.gofreshuser.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.Config.SharedPref;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.PayPal;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.tecmanic.RazorPay;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;


public class Payment_fragment extends Fragment {
    RelativeLayout confirm;
    private Session_management sessionManagement;
    TextView order_ammount,payble_ammount;
    private String getlocation_id = "";
    private String getstore_id = "";
    private String gettime = "";
    private String getdate = "";
    private String getuser_id = "";
    SharedPreferences delivercharge;
    String Used_coupon_amount;
    String Ammount;
    ProgressDialog progressDialog;
    String delivery_getcharges;
    RadioButton rb_Store, rb_Cod, rb_card, rb_Netbanking;
    CheckBox checkBox_Wallet,checkBox_coupon;
    String getvalue;
    String total_amount;
    String order_total_amount,Wallet_amount,Used_Wallet_amount;
    RadioGroup radioGroup;
    String getwallet;

    TextView my_wallet_ammount,used_coupon_ammount,tv_delivery_charges;
    EditText et_Coupon;
    LinearLayout Promo_code_layout, Coupon_and_wallet;
    RelativeLayout Apply_Coupon_Code, Relative_used_wallet, Relative_used_coupon;

    public Payment_fragment() {

    }

    public static Payment_fragment newInstance(String param1, String param2) {
        Payment_fragment fragment = new Payment_fragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_payment_method, container, false);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.payment));
        tv_delivery_charges=view.findViewById(R.id.delivery_ammount);
        delivercharge=getActivity().getSharedPreferences("charges",0);
        delivery_getcharges=delivercharge.getString("delivery_charges","");
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Please Wait");
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                getvalue = radioButton.getText().toString();
            }
        });
        getwallet = SharedPref.getString(getActivity(), Baseurl.KEY_WALLET_Ammount);
        my_wallet_ammount = (TextView) view.findViewById(R.id.user_wallet);

        my_wallet_ammount.setText(getActivity().getString(R.string.currency) + getwallet);

        checkBox_Wallet = (CheckBox) view.findViewById(R.id.use_wallet);
        checkBox_coupon=view.findViewById(R.id.use_coupon);
        Relative_used_coupon = (RelativeLayout) view.findViewById(R.id.relative_used_coupon);
        tv_delivery_charges.setText("₹ "+delivery_getcharges);

        rb_Store = (RadioButton) view.findViewById(R.id.use_store_pickup);
        payble_ammount = (TextView) view.findViewById(R.id.payable_ammount);
        rb_Cod = (RadioButton) view.findViewById(R.id.use_COD);
        rb_card = (RadioButton) view.findViewById(R.id.use_card);
        rb_Netbanking = (RadioButton) view.findViewById(R.id.use_netbanking);
        et_Coupon = (EditText) view.findViewById(R.id.et_coupon_code);
        Promo_code_layout = (LinearLayout) view.findViewById(R.id.prommocode_layout);
        Apply_Coupon_Code = (RelativeLayout) view.findViewById(R.id.apply_coupoun_code);
//        Coupon_and_wallet = (LinearLayout) view.findViewById(R.id.coupon_and_wallet);
//
//        Relative_used_coupon = (RelativeLayout) view.findViewById(R.id.relative_used_coupon);

        Apply_Coupon_Code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Coupon_code();

            }
        });

//

        sessionManagement = new Session_management(getActivity());



        //Show  Wallet

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        used_coupon_ammount = (TextView) view.findViewById(R.id.used_coupon_ammount);


        order_total_amount = getArguments().getString("total_amount");

        payble_ammount.setText(getActivity().getString(R.string.currency) + order_total_amount);

        getdate = getArguments().getString("getdate");

        gettime = getArguments().getString("gettime");

        getlocation_id = getArguments().getString("getlocationid");

        getstore_id = getArguments().getString("getstoreid");

        Log.d("store_id",getstore_id);


        order_ammount = (TextView) view.findViewById(R.id.order_ammount);

        order_ammount.setText("Rs."+order_total_amount);




        checkBox_Wallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {

                    Use_Wallet_Ammont();

                }
                else {

                    order_total_amount = getArguments().getString("total_amount");
                    payble_ammount.setText(getActivity().getString(R.string.currency) + order_total_amount);
                    order_ammount.setText("Rs."+order_total_amount);
                    my_wallet_ammount.setText(getActivity().getString(R.string.currency) + getwallet);

                }

            }
        });
        checkBox_coupon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Promo_code_layout.setVisibility(View.VISIBLE);
//                    Coupon_and_wallet.setVisibility(View.VISIBLE);
                    Relative_used_coupon.setVisibility(View.VISIBLE);
                    if (rb_Store.isChecked() || rb_Cod.isChecked() || rb_card.isChecked() || rb_Netbanking.isChecked()) {
                        rb_Store.setChecked(false);
                        rb_Cod.setChecked(false);
                        rb_card.setChecked(false);
                        rb_Netbanking.setChecked(false);
//                        rb_paytm.setChecked(false);
                    }
                } else {
                    et_Coupon.setText("");
                    Relative_used_coupon.setVisibility(View.GONE);
                    Promo_code_layout.setVisibility(View.GONE);
                }
            }
        });


        confirm = (RelativeLayout) view.findViewById(R.id.confirm_order);
        confirm.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {

                if (rb_Cod.isChecked()|| rb_Store.isChecked()){

                    if (ConnectivityReceiver.isConnected()) {
                        progressDialog.show();
                        confirm.setEnabled(false);

                        attemptOrder();

                    }


                }
                else if (rb_card.isChecked()|| rb_Netbanking.isChecked()){

                    final Dialog dialog = new Dialog(getActivity());

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.setContentView(R.layout.dialog_payment);

                    dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                    dialog.show();


                    Button razorpay=dialog.findViewById(R.id.razorpay);

                    Button paypal=dialog.findViewById(R.id.paypal);

                    razorpay.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent=new Intent(getActivity(), RazorPay.class);
                                                        intent.putExtra("getdate", getdate);
                                                        intent.putExtra("gettime", gettime);
                                                        intent.putExtra("usedamount",Used_coupon_amount);
                                                        intent.putExtra("previousamount",Ammount);
                                                        intent.putExtra("couponcode",et_Coupon.getText().toString());
                                                        intent.putExtra("getlocationid", getlocation_id);
                                                        intent.putExtra("getstoreid", getstore_id);
                                                        intent.putExtra("getpaymentmethod", getvalue);
                                                        intent.putExtra("total_amount",order_total_amount);
                                                        startActivity(intent);
                                                    }
                                                }
                    );
                    paypal.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {

                                                      Intent intent=new Intent(getActivity(), PayPal.class);
                                                      intent.putExtra("getdate", getdate);
                                                      intent.putExtra("gettime", gettime);
                                                      intent.putExtra("usedamount",Used_coupon_amount);
                                                      intent.putExtra("previousamount",Ammount);
                                                      intent.putExtra("couponcode",et_Coupon.getText().toString());
                                                      intent.putExtra("getlocationid", getlocation_id);
                                                      intent.putExtra("getstoreid", getstore_id);
                                                      intent.putExtra("getpaymentmethod", getvalue);
                                                      intent.putExtra("total_amount",order_total_amount);
                                                      startActivity(intent);

                                                  }
                                              }
                    );


                }else if (checkBox_coupon.isChecked()){


                }



                else {

                    Toast.makeText(getActivity(), "Select Payment Method", Toast.LENGTH_SHORT).show();

                    confirm.setEnabled(true);

                }
            }
        });
        return view;
    }
    private void attemptOrder() {

        getuser_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

        if (ConnectivityReceiver.isConnected()) {

            Log.e(TAG, "from:" + gettime + "\ndate:" + getdate +
                    "\n" + "\nuser_id:" + getuser_id + "\n" + getlocation_id + getstore_id + "\ndata:");


            if (checkBox_coupon.isChecked()){
                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id,order_total_amount,Ammount,Used_coupon_amount,et_Coupon.getText().toString());
            }
            else {
                makeAddOrderRequest(getdate, gettime, getuser_id, getlocation_id, getstore_id,order_total_amount,"","","");

            }

        }

    }

    private void makeAddOrderRequest_new(String date, String gettime, String userid, String
            location, String store_id,String amount,String previousamount,String usedamount,String coupon_code) {

        String tag_json_obj = "json_add_order_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("date", date);

        params.put("time", gettime);

        params.put("user_id", userid);

        params.put("total_order_amount",amount);
        params.put("used_amount",usedamount);
        params.put("previous_amount",previousamount);
        params.put("coupon_code",coupon_code);
        params.put("delivery_charge",delivery_getcharges);
        params.put("location", location);

        params.put("store_id", store_id);

        params.put("payment_method", getvalue);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        progressDialog.dismiss();
                        String msg = response.getString("data");
                        Bundle args = new Bundle();
                        Fragment fm = new Thanks_fragment();
                        args.putString("msg", msg);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();
                    }
                    else {
                        progressDialog.dismiss();
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
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);




//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    private void makeAddOrderRequest(String date, String gettime, String userid, String
            location, String store_id,String amount,String previousamount,String usedamount,String coupon_code) {

        String tag_json_obj = "json_add_order_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("date", date);

        params.put("time", gettime);

        params.put("user_id", userid);

        params.put("total_order_amount",amount);
        params.put("used_amount",usedamount);
        params.put("previous_amount",previousamount);
        params.put("coupon_code",coupon_code);
        params.put("delivery_charge",delivery_getcharges);
        params.put("location", location);

        params.put("store_id", store_id);

        params.put("payment_method", getvalue);


        Log.d(TAG, "SAFE DBG ORDER STARTED : "+params.toString());


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.ADD_ORDER_URL, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Log.d(TAG, "SAFE DBG ORDER RESPONSE : "+response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        progressDialog.dismiss();
                        String msg = response.getString("data");
                        Bundle args = new Bundle();
                        Fragment fm = new Thanks_fragment();
                        args.putString("msg", msg);
                        fm.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();
                    }
                    else {
                        progressDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "SAFE DBG ORDER RESPONSE ERROR: "+error.getMessage());
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
    private void Use_Wallet_Ammont() {




        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest postReq = new StringRequest(Request.Method.POST, Baseurl.use_Wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("eclipse", "Response=" + response);
                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray Jarray = object.getJSONArray("final_amount");
                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject json_data = Jarray.getJSONObject(i);

                                Wallet_amount = json_data.getString("wallet");

                                Used_Wallet_amount = json_data.getString("used_wallet");

                                order_total_amount = json_data.getString("total");

                                payble_ammount.setText(getResources().getString(R.string.currency) + order_total_amount);

//
                                SharedPref.putString(getActivity(), Baseurl.WALLET_TOTAL_AMOUNT, order_total_amount);

                                my_wallet_ammount.setText(getResources().getString(R.string.currency) + Wallet_amount);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("wallet_amount", getwallet);
                params.put("total_amount", getArguments().getString("total_amount"));
                return params;
            }
        };
        rq.add(postReq);

    }
    private void Coupon_code() {
        Ammount = getArguments().getString("total_amount");
        final String Wallet_Ammount = getwallet;
        final String Coupon_code = et_Coupon.getText().toString();
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        StringRequest postReq = new StringRequest(Request.Method.POST, Baseurl.COUPON_CODE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("eclipse", "Response=" + response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            total_amount = obj.getString("Total_amount");
                            order_total_amount=total_amount;
                            Used_coupon_amount = obj.getString("coupon_value");
                            if (obj.optString("responce").equals("true")) {
                                payble_ammount.setText(getResources().getString(R.string.currency) + total_amount);
                                order_total_amount=total_amount;
                                SharedPref.putString(getActivity(), Baseurl.COUPON_TOTAL_AMOUNT, total_amount);
                                Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                used_coupon_ammount.setText("(" + getActivity().getResources().getString(R.string.currency) + Used_coupon_amount + ")");
                                Promo_code_layout.setVisibility(View.GONE);

                            } else {
                                Toast.makeText(getActivity(), obj.getString("msg"), Toast.LENGTH_SHORT).show();
                                et_Coupon.setText("");
                                used_coupon_ammount.setText("");
                                payble_ammount.setText(getResources().getString(R.string.currency) + total_amount);
                                Promo_code_layout.setVisibility(View.GONE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error [" + error + "]");
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("coupon_code", Coupon_code);
                params.put("store_id",getstore_id);
                if (checkBox_Wallet.isChecked()) {
                    params.put("payable_amount", Wallet_Ammount);
                } else {
                    params.put("payable_amount", Ammount);
                }
                return params;
            }
        };
        rq.add(postReq);

    }

}


