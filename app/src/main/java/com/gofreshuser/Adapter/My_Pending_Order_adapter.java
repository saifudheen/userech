package com.gofreshuser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gofreshuser.model.My_Pending_order_model;
import com.gofreshuser.tecmanic.MyOrderDetail;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.tecmanic.TrackMap;

import java.util.List;


public class My_Pending_Order_adapter extends RecyclerView.Adapter<My_Pending_Order_adapter.MyViewHolder> {

    private List<My_Pending_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;

    private Context context;

    public My_Pending_Order_adapter(Context context, List<My_Pending_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, relativetextstatus, tv_tracking_date,track;
        public TextView tv_pending_date, tv_pending_time,tv_storename, tv_confirm_date, tv_confirm_time, tv_delevered_date, tv_delevered_time, tv_cancel_date, tv_cancel_time;
        public View view1, view2, view3, view4, view5, view6;
        public RelativeLayout relative_background;
        public ImageView Confirm, Out_For_Deliverde, Delivered;
        CardView cardView;
        Button deatils;
        public TextView tv_methid1;
        public String method;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {

            super(view);
            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            tv_storename=view.findViewById(R.id.storename);
            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            track=view.findViewById(R.id.track_map);
            deatils=view.findViewById(R.id.deatils_order);
            cardView = view.findViewById(R.id.card_view);

            linearLayout=view.findViewById(R.id.l2);
//            //Payment Method
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            //Date And Time
            tv_pending_date = (TextView) view.findViewById(R.id.pending_date);
            tv_pending_time = (TextView) view.findViewById(R.id.pending_time);
            tv_confirm_date = (TextView) view.findViewById(R.id.confirm_date);
            tv_confirm_time = (TextView) view.findViewById(R.id.confirm_time);
            tv_delevered_date = (TextView) view.findViewById(R.id.delevered_date);
            tv_delevered_time = (TextView) view.findViewById(R.id.delevered_time);
            tv_cancel_date = (TextView) view.findViewById(R.id.cancel_date);
            tv_cancel_time = (TextView) view.findViewById(R.id.cancel_time);
            //Oredre Tracking
            view1 = (View) view.findViewById(R.id.view1);
            view2 = (View) view.findViewById(R.id.view2);
            view3 = (View) view.findViewById(R.id.view3);
            view4 = (View) view.findViewById(R.id.view4);
            view5 = (View) view.findViewById(R.id.view5);
            view6 = (View) view.findViewById(R.id.view6);
            relative_background = (RelativeLayout) view.findViewById(R.id.relative_background);

            Confirm = (ImageView) view.findViewById(R.id.confirm_image);
            Out_For_Deliverde = (ImageView) view.findViewById(R.id.delivered_image);
            Delivered = (ImageView) view.findViewById(R.id.cancal_image);

        }
    }

    public My_Pending_Order_adapter(List<My_Pending_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_Pending_Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pending_order, parent, false);
        context = parent.getContext();
        return new My_Pending_Order_adapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(My_Pending_Order_adapter.MyViewHolder holder, int position) {
        final My_Pending_order_model mList = modelList.get(position);

        holder.tv_orderno.setText(mList.getSale_id());

        holder.deatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                String sale_id = mList.getSale_id();
//                String date = mList.get(position).getOn_date();
//                String time = mList.get(position).getDelivery_time_from() + "-" + mList.get(position).getDelivery_time_to();
//                String total = mList.get(position).getTotal_amount();
//                String status = mList.get(position).getStatus();
//                String deli_charge = mList.get(position).getDelivery_charge();
                Intent intent=new Intent(context.getApplicationContext(), MyOrderDetail.class);
                intent.putExtra("sale_id", mList.getSale_id());
                intent.putExtra("previous_amount",mList.getPrevious_amount());
                intent.putExtra("used_amount",mList.getUsed_amount());
                intent.putExtra("coupon_code",mList.getCoupon_code());
                intent.putExtra("date", mList.getOn_date());
                intent.putExtra("time", mList.getDelivery_time_to());
                intent.putExtra("total", mList.getTotal_amount());
                intent.putExtra("status", mList.getStatus());
                intent.putExtra("deli_charge", mList.getDelivery_charge());

                context. startActivity(intent);
                
            }
        });
        if (mList.getStatus().equals("0")) {
            holder.tv_status.setText(context.getResources().getString(R.string.pending));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.pending));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (mList.getStatus().equals("1")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.orange));
            holder.Confirm.setImageResource(R.color.colorPrimary);
            holder.tv_status.setText(context.getResources().getString(R.string.confirm));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.confirm));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (mList.getStatus().equals("2")) {
            holder.view1.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.relative_background.setBackgroundColor(context.getResources().getColor(R.color.purple));
            holder.view2.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view3.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.view4.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            holder.Confirm.setImageResource(R.color.colorPrimary);
            holder.Out_For_Deliverde.setImageResource(R.color.colorPrimary);

            holder.track.setVisibility(View.VISIBLE);
            holder.tv_status.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.relativetextstatus.setText(context.getResources().getString(R.string.outfordeliverd));
            holder.tv_status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
           else if (mList.getStatus().equals("4")) {
holder.linearLayout.setVisibility(View.GONE);
        }

        if (mList.getStatus().contains("0")|| mList.getStatus().contains("1") || mList.getStatus().contains("3")){
            holder.track.setVisibility(View.GONE);
        }else {
            holder.track.setVisibility(View.VISIBLE);
        }

           holder.track.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   Log.d("sd", mList.getDelivery_boy_lat()+mList.getDelivery_boy_lng()+mList.getLat());



                       Intent intent = new Intent(context.getApplicationContext(), TrackMap.class);
                       intent.putExtra("user_lat",mList.getLat());
                       intent.putExtra("user_lng",mList.getLng());
                       intent.putExtra("user_lat",mList.getLat());
                       intent.putExtra("user_id",mList.getUser_id());

                       intent.putExtra("rider_lat",mList.getDelivery_boy_lat());
                       intent.putExtra("rider_lng",mList.getDelivery_boy_lng());
                       intent.putExtra("rider_name",mList.getDelivery_boy_name());
                       intent.putExtra("rider_phone",mList.getDelivery_number());
                       context.startActivity(intent);


                   }

           });



        if (mList.getPayment_method().equals("Store Pick Up")) {
            holder.tv_methid1.setText("Store Pick Up");
        } else if (mList.getPayment_method().equals("Cash On Delivery")) {
            holder.tv_methid1.setText("Cash On Delivery");
        } else if (mList.getPayment_method().equals("Debit / Credit Card")) {
            holder.tv_methid1.setText("PrePaid");
        } else if (mList.getPayment_method().equals("Net Banking")) {
            holder.tv_methid1.setText("PrePaid");
        }
        holder.tv_date.setText(mList.getOn_date());

        if (mList.getVandor_name().contains("null")){
            holder.tv_storename.setText("Not Available");
        }
        else {
            holder.tv_storename.setText(mList.getVandor_name());
        }

        holder.tv_tracking_date.setText(mList.getOn_date());
        holder.tv_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());
        holder.tv_pending_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_pending_date.setText(mList.getOn_date());
        holder.tv_confirm_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_confirm_date.setText(mList.getOn_date());
        holder.tv_delevered_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_delevered_date.setText(mList.getOn_date());
        holder.tv_cancel_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_cancel_date.setText(mList.getOn_date());
    }
    public void removeddata(int postion){
        modelList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion,getItemCount());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

