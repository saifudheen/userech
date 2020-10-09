package com.gofreshuser.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.gofreshuser.Fragment.FragmentProductShow;
import com.gofreshuser.model.Show_sub_cat_product;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.tecmanic.ShowLogin;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class Show_sub_cat_adapter extends RecyclerView.Adapter<Show_sub_cat_adapter.MyViewHolder> {
    private List<Show_sub_cat_product> modelList;

    private Activity context;

    String productid;

    String message;
    String value;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    String get_check_store_id;

    SharedPreferences storeprefrences;

    String store_id, cart_id, qty_show;

    Session_management session_management;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, off, price, quant, priceor, add_to_cart, mrp_priceshow;

        public ImageView image, plus, minus;
        TextView quantity_check;

        LinearLayout show_rl, add_l1;

        Button addcart;

        public MyViewHolder(View view) {

            super(view);

            title = (TextView) view.findViewById(R.id.title);
            mrp_priceshow = (TextView) view.findViewById(R.id.mrp_priceshow);
            price = (TextView) view.findViewById(R.id.price);
            quant = (TextView) view.findViewById(R.id.quant);
            image = (ImageView) view.findViewById(R.id.topimage);
            add_l1 = view.findViewById(R.id.add_l1);
            quantity_check = view.findViewById(R.id.tv_subcat_contetiy);
            plus = view.findViewById(R.id.iv_subcat_plus);
            minus = view.findViewById(R.id.iv_subcat_minus);
            show_rl = (LinearLayout) view.findViewById(R.id.cart_l1);

            mrp_priceshow.setPaintFlags(mrp_priceshow.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            addcart = view.findViewById(R.id.add);
        }
    }

    public Show_sub_cat_adapter(List<Show_sub_cat_product> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Show_sub_cat_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_deal, parent, false);

        context = (Activity) parent.getContext();

        return new Show_sub_cat_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final Show_sub_cat_adapter.MyViewHolder holder, final int position) {

        final Show_sub_cat_product mList = modelList.get(position);

        progressDialog = new ProgressDialog(context);

        progressDialog.setMessage("Loading");

        productid = mList.getProduct_id();

        builder = new AlertDialog.Builder(context);
        session_management = new Session_management(context);

        storeprefrences = context.getSharedPreferences("sroreprefer", MODE_PRIVATE);

        store_id = storeprefrences.getString("store_id", "");
        value = "false";


        Glide.with(context)
                .load(Baseurl.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                Fragment fm = new FragmentProductShow();
                args.putString("productimage", mList.getProduct_image());
                args.putString("product_id", mList.getProduct_id());
                args.putString("price", mList.getPrice());
                args.putString("productname", mList.getProduct_name());
                args.putString("description", mList.getProduct_description());
                fm.setArguments(args);
                FragmentManager fragmentManager = context.getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });

        holder.quantity_check.setText("1");

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context,"Product quantity updating...", Toast.LENGTH_LONG).show();
                int qty;
                qty = 0;
                if (!holder.quantity_check.getText().toString().equalsIgnoreCase("")) {

                    qty = Integer.valueOf(holder.quantity_check.getText().toString());
                }

                if (qty > 0) {
                    qty = qty - 1;

                    //satr
                    final int NewQty;
                    NewQty=qty;
                    String tag_json_obj = "json_category_req";

                    Map<String, String> params = new HashMap<String, String>();


                    params.put("product_id", mList.getProduct_id());

                    params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));


                    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                            Baseurl.Check_cart, params, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("TAG", response.toString());

                            Log.d("TAG", "CHECK KART FROM MINUS----"+response.toString());

                            try {

                                boolean status = response.getBoolean("responce");


                                if (status) {


                                    cart_id = response.getString("cart_id");

                                    qty_show = response.getString("qty");


                                    Log.d("TAG", "CART ID @@@@@@@@@@ : "+cart_id);
                                    Log.d("TAG", "CART QTY @@@@@@@@@@ : "+qty_show);


                                    update(String.valueOf(NewQty), cart_id);
                                    holder.quantity_check.setText(String.valueOf(NewQty));


                                } else {




                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("TAG", "-----------------------------SAFE DEBUGGING ERROR ON CHECK KART FROM MINUS----------------------------------------");
                            VolleyLog.d("TAG", "Error SAFE: " + error.getMessage());
                            VolleyLog.d("TAG", "Error: " + error.getMessage());
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    AppController.getInstance().getRequestQueue().add(jsonObjReq);





                    //end


                }


                if (holder.quantity_check.getText().toString().equalsIgnoreCase("0")) {

                    deleteorder(session_management.getUserDetails().get(Baseurl.KEY_ID), cart_id, holder.add_l1, holder.show_rl);


                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Product quantity updating...", Toast.LENGTH_LONG).show();

                int qty = Integer.valueOf(holder.quantity_check.getText().toString());
                qty = qty + 1;

                //satr
                final int NewQty = qty;
                String tag_json_obj = "json_category_req";

                Map<String, String> params = new HashMap<String, String>();


                params.put("product_id", mList.getProduct_id());

                params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));


                CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                        Baseurl.Check_cart, params, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("TAG", response.toString());

                        Log.d("TAG", "CHECK KART FROM PLUS----"+response.toString());

                        try {

                            boolean status = response.getBoolean("responce");


                            if (status) {


                                cart_id = response.getString("cart_id");

                                qty_show = response.getString("qty");


                                Log.d("TAG", "CART ID @@@@@@@@@@ : "+cart_id);
                                Log.d("TAG", "CART QTY @@@@@@@@@@ : "+qty_show);


                                update(String.valueOf(NewQty), cart_id);

                                holder.quantity_check.setText(String.valueOf(NewQty));


                            }
                            else {




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("TAG", "-----------------------------SAFE DEBUGGING ERROR ON CHECK KART FROM PLUS----------------------------------------");
                        VolleyLog.d("TAG", "Error SAFE: " + error.getMessage());
                        VolleyLog.d("TAG", "Error: " + error.getMessage());
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AppController.getInstance().getRequestQueue().add(jsonObjReq);





                //end


            }
        });




        holder.addcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (session_management.isLoggedIn()) {
                    progressDialog.show();
                    value = "true";

                    checkcart(holder.addcart, mList.getProduct_id(), holder.add_l1, holder.show_rl, holder.quantity_check, value);


                } else {

                    Intent intent = new Intent(context, ShowLogin.class);

                    context.startActivity(intent);

                }

            }
        });

        holder.title.setText(mList.getProduct_name());

        holder.price.setText("Rs." + mList.getPrice());

        holder.quant.setText(mList.getUnit_value() + "" + mList.getUnit());

        holder.mrp_priceshow.setText("Rs." + mList.getMrp());

        checkcart(holder.addcart, mList.getProduct_id(), holder.add_l1, holder.show_rl, holder.quantity_check, value);

    }

    @Override
    public int getItemCount() {
        return modelList.size();

    }

    private void addtocart(final Button show_cart, final String id, String stid, final LinearLayout show_add_l1, final LinearLayout show_add_to_cartl1, final TextView qty_ty, final String check_value) {


        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));

        params.put("product_id", id);

        params.put("qty", "1");

        params.put("store_id", stid);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.URL_add_to_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    message = response.getString("msg");


                    if (response != null && response.length() > 0) {

                        MainActivity.countshow();

                        progressDialog.dismiss();

//                        checkcart(show_cart,id,show_add_l1,show_add_to_cartl1,qty_ty,value);

                        show_add_l1.setVisibility(View.VISIBLE);
                        show_add_to_cartl1.setVisibility(View.GONE);

                        if (check_value.contains("true")) {
                            show_cart.setEnabled(false);
                        } else {
                            show_cart.setEnabled(true);
                        }


                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } else {
                        progressDialog.dismiss();
                        show_cart.setEnabled(true);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void checkcart(final Button show_cart, final String id, final LinearLayout show_add, final LinearLayout show_addtocart, final TextView quantity_show, final String showvalue) {


        String tag_json_obj = "json_category_req";

        Map<String, String> params = new HashMap<String, String>();


        params.put("product_id", id);

        params.put("user_id", session_management.getUserDetails().get(Baseurl.KEY_ID));


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


                        cart_id = response.getString("cart_id");

                        qty_show = response.getString("qty");

                        quantity_show.setText(qty_show);

                        show_add.setVisibility(View.VISIBLE);

                        show_addtocart.setVisibility(View.GONE);


                    } else {

                        MainActivity.countshow();
                        show_add.setVisibility(View.GONE);

                        show_addtocart.setVisibility(View.VISIBLE);

                        if (showvalue.contains("true")) {
                            showcart(show_cart, id, store_id, show_add, show_addtocart, quantity_show, showvalue);


                        } else {


//              Toast.makeText(context, "No", Toast.LENGTH_SHORT).show();
                        }


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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void deleteorder(String userid, String cartid, final LinearLayout add_l11, final LinearLayout show_r11) {


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", userid);
        params.put("cart_id", cartid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Delete_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    if (response != null && response.length() > 0) {
                        String message = response.getString("msg");
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        MainActivity.countshow();
                        add_l11.setVisibility(View.GONE);
                        show_r11.setVisibility(View.VISIBLE);

                        value = "false";

//                            Fragment fm = new Cart_fragment();
//
//                          FragmentManager fragmentManager = getFragmentManager();
//
//                           fragmentManager.beginTransaction().replace(R.id.main_container, fm)
//                            .addToBackStack(null).commit();


                    } else {

                        String message = response.getString("msg");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void update(String qty, String cartid) {


        String tag_json_obj = "json_category_req";


        Map<String, String> params = new HashMap<String, String>();

        params.put("qty", qty);

        params.put("cart_id", cartid);


        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.Update_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {

                    if (response != null && response.length() > 0) {
                        String message = response.getString("msg");
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

                    } else {
//                        progressDialog.dismiss();
//                        animate.stopShimmer();
                        String message = response.getString("msg");

                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    private void showcart(final Button show_cartt, final String idd, final String stidd, final LinearLayout show_add_l11, final LinearLayout show_add_to_cartl11, final TextView qty_tyy, final String check) {


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


                            get_check_store_id = jsonObject.getString("user_id");

                            if (stidd.contains(get_check_store_id)) {
                                addtocart(show_cartt, idd, stidd, show_add_l11, show_add_to_cartl11, qty_tyy, check);

                            } else {
                                builder.setMessage("Ordering  products under different catagories  is not allowed in a single booking")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                progressDialog.show();
                                                clearcart();
//                        Toast.makeText(context,"you choose yes action for alertbox",
//                                Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
//                        Toast.makeText(context,"you choose no action for alertbox",
//                                Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("GoFresh");
                                alert.show();
                            }

                        } else {
                            addtocart(show_cartt, idd, stidd, show_add_l11, show_add_to_cartl11, qty_tyy, check);

                        }


                    } else {
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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
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
                        String message = response.getString("msg");
                        progressDialog.dismiss();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        MainActivity.countshow();


                    } else {
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
                    Toast.makeText(context, context.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
