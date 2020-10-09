package com.gofreshuser.tecmanic;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.util.ConnectivityReceiver;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by subhashsanghani on 12/21/16.
 */

public class MyFirebaseRegister extends Application {

    Activity _context;
    public SharedPreferences settings;
    ConnectivityReceiver cd;
    @Override
    public void onCreate() {
        super.onCreate();
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            FirebaseApp.initializeApp(this);

    }

    public MyFirebaseRegister(Activity context) {
        this._context = context;
        FirebaseApp.initializeApp(context);
        settings = context.getSharedPreferences(Baseurl.PREFS_NAME, 0);
        cd=new ConnectivityReceiver();

    }
    public void RegisterUser(String user_id){
        // [START subscribe_topics]

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("token",token);
        FirebaseMessaging.getInstance().subscribeToTopic("gofresh");

        checkLogin(user_id, token);
    }





    private void checkLogin(String user_id, String token) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_login_req";

        //showpDialog();

        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("token", token);
        params.put("device","android");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.JSON_RIGISTER_FCM, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    Boolean status = response.getBoolean("responce");
                    if (status) {

                        JSONObject obj = new JSONObject();



                        //onBackPressed();


                    }else{
                        String error = response.getString("error");
                        Toast.makeText(_context, ""+error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(_context,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                //hidepDialog();
            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



}
