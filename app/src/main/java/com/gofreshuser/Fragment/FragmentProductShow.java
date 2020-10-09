package com.gofreshuser.Fragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.tecmanic.ShowLogin;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProductShow extends Fragment {

  String productimagedata;

   String productid,product_des;

   String price,message,store_id;

    TextView productdetails,priceshow,text_product_name;

    Session_management session_management;
    AlertDialog.Builder builder;

    ProgressDialog progressDialog;
    Button add_to_cart;
SharedPreferences storeprefrences;
    ImageView productimage;

    public FragmentProductShow() {
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
        View view= inflater.inflate(R.layout.fragment_fragment_product_show, container, false);

        productimagedata= Baseurl.IMG_PRODUCT_URL+ getArguments().getString("productimage");

        MainActivity.countshow();
        add_to_cart=view.findViewById(R.id.button_add_cart);

        text_product_name=view.findViewById(R.id.text_product_name);
        text_product_name.setText(getArguments().getString("productname"));
        session_management=new Session_management(getActivity());

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading");
        price=getArguments().getString("price");
        builder = new AlertDialog.Builder(getActivity());
        storeprefrences=getActivity().getSharedPreferences("sroreprefer",MODE_PRIVATE);

        store_id = storeprefrences.getString("store_id","");

        productid=getArguments().getString("product_id");

        product_des=getArguments().getString("description");

        productimage= view.findViewById(R.id.pager);

        priceshow=view.findViewById(R.id.text_priceshow);

        productdetails=view.findViewById(R.id.des_show);

        Glide.with(getActivity())
                .load(productimagedata)
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(productimage);

          priceshow.setText("Rs."+price);

          productdetails.setText(product_des);


          add_to_cart.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if (add_to_cart.getText().toString().contains("Go to Cart")){
                      Fragment fm = new Cart_fragment();
                      FragmentManager fragmentManager = getActivity().getFragmentManager();
                      fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                              .addToBackStack(null).commit();

                  }
                  else {
                      if (session_management.isLoggedIn()){
                         progressDialog.show();
                         checkcart();
                      }
                      else {
                          Intent intent=new Intent(getActivity(), ShowLogin.class);
                          startActivity(intent);
                      }
                  }

              }
          });

        return view;
    }
    private void addtocart() {


        String tag_json_obj = "json_cat" +
                "egory_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));

        params.put("product_id", productid);
        params.put("store_id",store_id);
        params.put("qty","1");

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_add_to_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    message=response.getString("msg");


                    if (response != null && response.length() > 0) {

                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        MainActivity.countshow();

                        add_to_cart.setText("Go to Cart");
                        progressDialog.dismiss();


                    }
                    else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

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


    }

    private void showcart() {


        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,

                Baseurl.View_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", response.toString());
try {



                if (response != null && response.length() > 0) {

                    Boolean response_check = response.getBoolean("responce");

                    if (response_check) {

                        JSONObject jsonObject = response.getJSONObject("store_details");
                        String get_check_store_id = jsonObject.getString("user_id");

                        if (store_id.contains(get_check_store_id)) {
                            addtocart();

                        } else {
                            builder.setMessage("Ordering  products under different catagories  is not allowed in a single booking")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            clearcart();
//                                            Toast.makeText(getActivity(), "you choose yes action for alertbox",
//                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();
//                                            Toast.makeText(getActivity(), "you choose no action for alertbox",
//                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("GoFresh");
                            alert.show();
                        }


                    } else {
                        addtocart();
                    }


                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
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


    }
    private void clearcart() {


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Clear_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        String message=response.getString("msg");
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        MainActivity.countshow();



                    }
                    else {
                        progressDialog.dismiss();
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


    }

    private void checkcart() {


        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();


        params.put("product_id", productid);

        params.put("user_id",session_management.getUserDetails().get(Baseurl.KEY_ID));



        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Check_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {

                    boolean status = response.getBoolean("responce");


                    if (status) {

                        progressDialog.dismiss();

                        MainActivity.countshow();


                        Toast.makeText(getActivity(), "Already Added to Cart", Toast.LENGTH_SHORT).show();



                    } else {
                        showcart();

                        MainActivity.countshow();


                        progressDialog.dismiss();


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

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }
}

