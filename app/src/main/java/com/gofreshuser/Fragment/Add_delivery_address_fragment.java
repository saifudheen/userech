package com.gofreshuser.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Rajesh Dabhi on 6/7/2017.
 */

public class Add_delivery_address_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Add_delivery_address_fragment.class.getSimpleName();

    private EditText et_phone, et_name, et_pin, et_house,et_landmark,et_address;
    private RelativeLayout btn_update;
    private TextView tv_phone, tv_name, tv_pin, tv_house, tv_socity,btn_socity,tv_landmark,tv_address;
    private String getsocity = "";
     String lat,lng;
    private Session_management sessionManagement;

    SharedPreferences placePref,locationpre;
    private boolean isEdit = false;
    Geocoder geocoder;
    List<Address> addresses;
    ProgressDialog progressDialog;
    private String getlocation_id;
    public  static  String value="false";
 String address;
    public Add_delivery_address_fragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_delivery_address, container, false);

        ((MainActivity) getActivity()).setTitle("Address");

        sessionManagement = new Session_management(getActivity());
        placePref = getActivity().getSharedPreferences("getlatlng",MODE_PRIVATE);

        locationpre=getActivity().getSharedPreferences("location",MODE_PRIVATE);
progressDialog=new ProgressDialog(getActivity());
progressDialog.setMessage("Loading");
        et_phone = (EditText) view.findViewById(R.id.et_add_adres_phone);
        et_name = (EditText) view.findViewById(R.id.et_add_adres_name);
        tv_phone = (TextView) view.findViewById(R.id.tv_add_adres_phone);
        tv_name = (TextView) view.findViewById(R.id.tv_add_adres_name);
        tv_pin = (TextView) view.findViewById(R.id.tv_add_adres_pin);
        et_pin = (EditText) view.findViewById(R.id.et_add_adres_pin);
        et_house = (EditText) view.findViewById(R.id.et_add_adres_home);
        et_address=view.findViewById(R.id.et_add_street);
        et_landmark=view.findViewById(R.id.et_add_landmark);
        tv_house = (TextView) view.findViewById(R.id.tv_add_adres_home);
        tv_socity = (TextView) view.findViewById(R.id.tv_add_adres_socity);
        btn_update = (RelativeLayout) view.findViewById(R.id.btn_add_adres_edit);
        btn_socity = (TextView) view.findViewById(R.id.btn_add_adres_socity);
tv_landmark=view.findViewById(R.id.landmark);
tv_address=view.findViewById(R.id.tv_street);
        String getsocity_name = sessionManagement.getUserDetails().get(Baseurl.KEY_SOCITY_NAME);
        String getsocity_id = sessionManagement.getUserDetails().get(Baseurl.KEY_SOCITY_ID);
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        Bundle args = getArguments();

        value=placePref.getString("value","false");

        lat=locationpre.getString("lat","");

        lng=locationpre.getString("lng","");




    try {

        addresses = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
         address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        btn_socity.setText(address);
    } catch (IOException e) {
        e.printStackTrace();
    }






        if (args != null) {

            getlocation_id = getArguments().getString("location_id");
            String get_name = getArguments().getString("name");
            String get_phone = getArguments().getString("mobile");
            String get_pine = getArguments().getString("pincode");
            String get_socity_id = getArguments().getString("socity_id");
            String get_socity_name = getArguments().getString("address");
            String get_house = getArguments().getString("house");

            if (TextUtils.isEmpty(get_name) && get_name == null) {
                isEdit = false;
            } else {
                isEdit = true;

//                Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();

                et_name.setText(get_name);
                et_phone.setText(get_phone);
                et_pin.setText(get_pine);
                et_house.setText(get_house);
                btn_socity.setText(get_socity_name);

                sessionManagement.updateSocity(get_socity_name, get_socity_id);
            }
        }

        if (!TextUtils.isEmpty(getsocity_name)) {

            btn_socity.setText(getsocity_name);
            sessionManagement.updateSocity(getsocity_name, getsocity_id);
        }

        btn_update.setOnClickListener(this);
        btn_socity.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if (id == R.id.btn_add_adres_edit) {

            attemptEditProfile();

        }
    }

    private void attemptEditProfile() {

        tv_phone.setText(getResources().getString(R.string.receiver_mobile_number));
        tv_pin.setText(getResources().getString(R.string.tv_reg_pincode));
        tv_name.setText(getResources().getString(R.string.receiver_name_req));
        tv_house.setText(getResources().getString(R.string.tv_reg_house));
        tv_socity.setText(getResources().getString(R.string.tv_reg_socity));

        tv_name.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_phone.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_pin.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_house.setTextColor(getResources().getColor(R.color.dark_gray));
        tv_socity.setTextColor(getResources().getColor(R.color.dark_gray));

        String getphone = et_phone.getText().toString();
        String getname = et_name.getText().toString();
        String getlandmark=et_landmark.getText().toString();
        String getAddress=et_address.getText().toString();
        String getpin = et_pin.getText().toString();
        String gethouse = et_house.getText().toString();
        String getsocity = sessionManagement.getUserDetails().get(Baseurl.KEY_SOCITY_ID);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(getphone)) {

            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;

        } else if (!isPhoneValid(getphone)) {
            tv_phone.setText(getResources().getString(R.string.phone_too_short));
            tv_phone.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_phone;
            cancel = true;
        }

        if (TextUtils.isEmpty(getname)) {
            tv_name.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(getpin)) {
            tv_pin.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_pin;
            cancel = true;
        }

        if (TextUtils.isEmpty(gethouse)) {
            tv_house.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_house;
            cancel = true;
        }

        if (TextUtils.isEmpty(getAddress)) {
            tv_address.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_address;
            cancel = true;
        }
        if (TextUtils.isEmpty(getlandmark)) {
            tv_landmark.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = et_landmark;
            cancel = true;
        }

        if (TextUtils.isEmpty(getsocity) && getsocity == null) {
            tv_socity.setTextColor(getResources().getColor(R.color.colorPrimary));
            focusView = btn_socity;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            if (focusView != null)
                focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (ConnectivityReceiver.isConnected()) {

                String user_id = sessionManagement.getUserDetails().get(Baseurl.KEY_ID);

                // check internet connection
                if (ConnectivityReceiver.isConnected()) {

                    progressDialog.show();
                    if (isEdit) {

                        makeEditAddressRequest(getlocation_id, getpin, getsocity, et_house.getText().toString()+","+getAddress+","+getlandmark, getname, getphone);
                    } else {
                        makeAddAddressRequest(user_id, getpin, et_house.getText().toString()+","+getAddress+","+getlandmark, getname, getphone);
                    }
                }
            }
        }
    }

    private boolean isPhoneValid(String phoneno) {
        //TODO: Replace this with your own logic
        return phoneno.length() > 9;
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeAddAddressRequest(String user_id, String pincode, String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_add_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("pincode", pincode);
        params.put("house_no", house_no);
        params.put("address",address);
        params.put("lat",lat);
        params.put("lng",lng);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.ADD_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {
                        Fragment fm = new Delivery_fragment();

                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();

//                        ((MainActivity) getActivity()).onBackPressed();
                        progressDialog.dismiss();
                            btn_update.setEnabled(false);
                    }
                    else {
                        progressDialog.dismiss();
                        btn_update.setEnabled(true);

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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeEditAddressRequest(String location_id, String pincode, String socity_id,
                                        String house_no, String receiver_name, String receiver_mobile) {

        // Tag used to cancel the request
        String tag_json_obj = "json_edit_address_req";

        Map<String, String> params = new HashMap<String, String>();
        params.put("lat", lat);
        params.put("lng",lng);
        params.put("location_id",location_id);
        params.put("address",address);
        params.put("pincode", pincode);
        params.put("house_no", house_no);
        params.put("receiver_name", receiver_name);
        params.put("receiver_mobile", receiver_mobile);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.EDIT_ADDRESS_URL, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        String msg = response.getString("data");
                        Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();

                        Fragment fm = new Delivery_fragment();

                        FragmentManager fragmentManager = getFragmentManager();

                        fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                                .addToBackStack(null).commit();
                         btn_update.setEnabled(false);
                         progressDialog.dismiss();

                    }
                    else {
                        progressDialog.dismiss();
                        btn_update.setEnabled(true);
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

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

}
