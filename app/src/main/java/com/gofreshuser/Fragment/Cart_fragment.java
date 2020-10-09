
package com.gofreshuser.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Cart_model;
import com.gofreshuser.util.CustomVolleyJsonRequest;
import com.gofreshuser.util.Session_management;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gofreshuser.tecmanic.AppController;
import com.gofreshuser.tecmanic.MainActivity;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.util.ConnectivityReceiver;
import org.json.JSONArray;
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
public class Cart_fragment extends Fragment implements View.OnClickListener {

    private static String TAG = Cart_fragment.class.getSimpleName();

    private ShimmerRecyclerView rv_cart;
    private TextView tv_clear, tv_total, tv_item;
    private RelativeLayout btn_checkout;
    ArrayList<Cart_model> list=new ArrayList<>();
    String message;
    String user_id,store_id;
    Cart_adapter adapter;
    SharedPreferences storeprefrences;
    String total_item,total_price;

    Session_management session_management;
   ProgressDialog progressDialog;
   SharedPreferences deliveryprefrence;
   SharedPreferences.Editor editor;
   Float price ;
   int cartfromnt,carttoint,chargesamountint;
   String cartfrom,cartto,chargesamount;
    public Cart_fragment() {
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
        View view= inflater.inflate(R.layout.fragment_cart_fragment, container, false);

        MainActivity.countshow();

        deliveryprefrence=getActivity().getSharedPreferences("charges",0);
          editor=deliveryprefrence.edit();
        progressDialog=new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading");

        tv_clear=view.findViewById(R.id.clear);
        storeprefrences=getActivity().getSharedPreferences("sroreprefer",MODE_PRIVATE);

        store_id=storeprefrences.getString("store_id","");

        MainActivity.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fm = new FragmentHome();

                FragmentManager fragmentManager = getFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                        .addToBackStack(null).commit();

            }
        });

        tv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                       clearcart();
            }
        });
        session_management=new Session_management(getActivity());
        user_id = session_management.getUserDetails().get(Baseurl.KEY_ID);
        Log.d("user",user_id);

//        tv_clear = (TextView) view.findViewById(R.id.tv_cart_clear);

        tv_total = (TextView) view.findViewById(R.id.tv_cart_total);
        tv_item = (TextView) view.findViewById(R.id.tv_cart_item);

        btn_checkout = (RelativeLayout) view.findViewById(R.id.btn_cart_checkout);
        rv_cart = (ShimmerRecyclerView) view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_cart.setHasFixedSize(true);
        rv_cart.showShimmerAdapter();

        updateData();

//        tv_clear.setOnClickListener(this);

        btn_checkout.setOnClickListener(this);

        showcart();

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener()
//
//        {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    Fragment fm = new FragmentHome();
//                    Bundle args = new Bundle();
//                    fm.setArguments(args);
//                    FragmentManager fragmentManager = getFragmentManager();
//
//                    fragmentManager.beginTransaction().replace(R.id.main_container, fm)
//                            .addToBackStack(null).commit();
//
//                    return true;
//                }
//                return false;
//            }
//        });

        return view;
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();

       if (id == R.id.btn_cart_checkout) {

            Fragment fm = new Delivery_fragment();
           Bundle args = new Bundle();
           args.putString("total_item",total_item);
           args.putString("total_price",total_price);
           fm.setArguments(args);
            FragmentManager fragmentManager = getFragmentManager();
            if(fragmentManager!=null) {
                 fragmentManager.beginTransaction().replace(R.id.main_container, fm)
                .addToBackStack(null).commit();
             }
          

        }
    }
    private void updateData() {
//        tv_total.setText("" + db.getTotalAmount());
//        tv_item.setText("" + db.getCartCount());
//        ((MainActivity) getActivity()).setCartCounter("" + db.getCartCount());
    }

    private void showClearDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setMessage("Are you sure!\nyou want to delete all cart product");
        alertDialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear cart data
//                Cart_adapter adapter = new Cart_adapter(getActivity(), map);
//                rv_cart.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//
//                updateData();

                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void showcart() {


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                Baseurl.View_cart, params, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", response.toString());
                try {
                    if (response != null && response.length() > 0) {




                        total_item= response.getString("total_item");

if (total_item.contains("0")
){
    rv_cart.hideShimmerAdapter();

    Fragment fm = new Empty_cart_fragment();
    FragmentManager fragmentManager = getFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.main_container, fm)
            .addToBackStack(null).commit();
}
else {
    total_price= response.getString("total_amount");

    price= Float.parseFloat(total_price);

    getcharges(price);
    tv_item.setText(total_item);
    tv_total.setText(total_price);
    rv_cart.hideShimmerAdapter();
    Gson gson = new Gson();
//
    Type listType = new TypeToken<List<Cart_model>>() {
    }.getType();
    list= gson.fromJson(response.getString("data"), listType);

    adapter = new Cart_adapter(getActivity(), list);
    rv_cart.setAdapter(adapter);
    adapter.notifyDataSetChanged();

}

                    }

                    else {
//                        progressDialog.dismiss();
//                        animate.stopShimmer();
                        rv_cart.hideShimmerAdapter();
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
    private void clearcart() {


        String tag_json_obj = "json_category_req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id", user_id);


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

                     showcart();

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
    public class Cart_adapter extends RecyclerView.Adapter<Cart_adapter.ProductHolder> {
        ArrayList<Cart_model> list;
        String cart_id;
        Activity activity;
        AlertDialog.Builder builder;

        public Cart_adapter(Activity activity, ArrayList<Cart_model> list) {
            this.list = list;
            this.activity = activity;


        }

        @Override
        public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_rv, parent, false);
            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(final ProductHolder holder, final int position) {
            final Cart_model cart_model = list.get(position);

            Glide.with(activity)
                    .load(Baseurl.IMG_PRODUCT_URL + cart_model.getProduct_image())
                    .centerCrop()
                    .placeholder(R.drawable.icon)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(holder.iv_logo);

            holder.tv_title.setText(cart_model.getProduct_name());
//        holder.tv_reward.setText(cart_model.getRewards());
          holder.tv_price.setText(cart_model.getUnit_value()+" "+cart_model.getUnit());
          holder.tv_total.setText(cart_model.getPrice());
            holder.tv_contetiy.setText(cart_model.getQty());

            holder.iv_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int qty = 0;
                    if (!holder.tv_contetiy.getText().toString().equalsIgnoreCase("")) {

                        qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                    }

                    if (qty > 0) {
                        qty = qty - 1;
                        update(String.valueOf(qty),cart_model.getCart_id());
                        holder.tv_contetiy.setText(String.valueOf(qty));
                        showcart();
                    }



                    if (holder.tv_contetiy.getText().toString().equalsIgnoreCase("0")) {
                                        deleteorder( session_management.getUserDetails().get(Baseurl.KEY_ID),cart_model.getCart_id());
//                                        addtocart();



                    }
                }
            });

            holder.iv_plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int qty = Integer.valueOf(holder.tv_contetiy.getText().toString());
                    qty = qty + 1;
                      update(String.valueOf(qty),cart_model.getCart_id());
                    holder.tv_contetiy.setText(String.valueOf(qty));
                    showcart();
                }
            });

//        holder.tv_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                updateintent();
//            }
//        });

//        holder.iv_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                dbHandler.removeItemFromCart(map.get("product_id"));
//                list.remove(position);
//                notifyDataSetChanged();
//
//                updateintent();
//            }
//        });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ProductHolder extends RecyclerView.ViewHolder {
            public TextView tv_title, tv_price, tv_reward, tv_total, tv_contetiy, tv_add,
                    tv_unit, tv_unit_value;
            public ImageView iv_logo, iv_plus, iv_minus, iv_remove;

            public ProductHolder(View view) {
                super(view);

                tv_title = (TextView) view.findViewById(R.id.tv_subcat_title);
                tv_price = (TextView) view.findViewById(R.id.tv_subcat_price);
            tv_total = (TextView) view.findViewById(R.id.tv_subcat_total);
//            tv_reward = (TextView) view.findViewById(R.id.tv_reward_point);
                tv_contetiy = (TextView) view.findViewById(R.id.tv_subcat_contetiy);
//            tv_add = (TextView) view.findViewById(R.id.tv_subcat_add);
                iv_logo = (ImageView) view.findViewById(R.id.iv_subcat_img);
                iv_plus = (ImageView) view.findViewById(R.id.iv_subcat_plus);
                iv_minus = (ImageView) view.findViewById(R.id.iv_subcat_minus);
//            iv_remove = (ImageView) view.findViewById(R.id.iv_subcat_remove);

//            tv_add.setText(R.string.tv_pro_update);

            }
        }

        private void updateintent() {
            Intent updates = new Intent("Grocery_cart");
            updates.putExtra("type", "update");
            activity.sendBroadcast(updates);
        }
        private void deleteorder(String userid,String cartid) {


            String tag_json_obj = "json_category_req";
            Map<String, String> params = new HashMap<String, String>();

            params.put("user_id", userid);
            params.put("cart_id",cartid);


            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    Baseurl.Delete_cart, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", response.toString());
                    try {
                        if (response != null && response.length() > 0) {
                            String message=response.getString("msg");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                            MainActivity.countshow();

                         showcart();
//                            Fragment fm = new Cart_fragment();
//
//                          FragmentManager fragmentManager = getFragmentManager();
//
//                           fragmentManager.beginTransaction().replace(R.id.main_container, fm)
//                            .addToBackStack(null).commit();


                        }
                        else {

                            String message=response.getString("msg");

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(activity, activity.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


        }
        private void update(String qty,String cartid) {


            String tag_json_obj = "json_category_req";


            Map<String, String> params = new HashMap<String, String>();

            params.put("qty", qty);

            params.put("cart_id",cartid);


            CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                    Baseurl.Update_cart, params, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d("TAG", response.toString());
                    try {

                        if (response != null && response.length() > 0) {
                            String message=response.getString("msg");
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

                        }
                        else {
//                        progressDialog.dismiss();
//                        animate.stopShimmer();
                            String message=response.getString("msg");

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(activity, activity.getResources().getString(R.string.connection_time_out), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


        }

    }

private  void getcharges(final Float pricecheck){

    String tag_json_obj = "json_category_req";

    Map<String, String> params = new HashMap<String, String>();
    params.put("user_id",store_id);

    CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
            Baseurl.Get_delivery_chargesapi, params, new Response.Listener<JSONObject>() {

        @Override
        public void onResponse(JSONObject response) {
            Log.d("TAG", response.toString());

            try {

                if (response != null && response.length() > 0) {


                    JSONArray jsonArray=response.getJSONArray("charges");
                    if (jsonArray.length()!=0){
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            cartfrom=jsonObject.getString("cart_from");
                            cartto=jsonObject.getString("cart_to");
                            cartfromnt= Integer.parseInt(cartfrom);
                            carttoint= Integer.parseInt(cartto);

                            if (pricecheck>=cartfromnt && pricecheck<=carttoint){
                                chargesamount=jsonObject.getString("charge_amount");
                                editor.putString("delivery_charges",chargesamount);
                                editor.commit();
                            }
                        }

                    }
                    else {
                        editor.putString("delivery_charges","0");
                        editor.commit();
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
//    AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(1000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    AppController.getInstance().getRequestQueue().getCache().clear();
    AppController.getInstance().getRequestQueue().add(jsonObjReq);


}
    }



