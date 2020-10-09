package com.gofreshuser.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.gofreshuser.model.My_Past_order_model;
import com.gofreshuser.tecmanic.Compalin_prouduct;
import com.gofreshuser.tecmanic.MyOrderDetail;
import com.gofreshuser.tecmanic.R;
import com.gofreshuser.tecmanic.ReorderActivty;

import java.util.List;


public class My_Past_Order_adapter extends RecyclerView.Adapter<My_Past_Order_adapter.MyViewHolder> {

    private List<My_Past_order_model> modelList;
    private LayoutInflater inflater;
    private Fragment currentFragment;
    ProgressDialog progressDialog;

    private Context context;

    public My_Past_Order_adapter(Context context, List<My_Past_order_model> modemodelList, final Fragment currentFragment) {

        this.context = context;
        this.modelList = modelList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentFragment = currentFragment;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_orderno, tv_status, tv_date, tv_time, tv_price, tv_item, tv_tracking_date,tv_vendorname;
        public Button help,bt_status;
        public ImageView Confirm, Out_For_Deliverde, Delivered;
        CardView cardView;
        public TextView tv_methid1;
        public String method;
           Button order_details;
Button re_order;
        public MyViewHolder(View view) {
            super(view);

            tv_orderno = (TextView) view.findViewById(R.id.tv_order_no);
            tv_status = (TextView) view.findViewById(R.id.tv_order_status);
            bt_status=view.findViewById(R.id.bt_status);
//            relativetextstatus = (TextView) view.findViewById(R.id.status);
            tv_tracking_date = (TextView) view.findViewById(R.id.tracking_date);
            tv_date = (TextView) view.findViewById(R.id.tv_order_date);
            tv_time = (TextView) view.findViewById(R.id.tv_order_time);
            tv_price = (TextView) view.findViewById(R.id.tv_order_price);
            tv_item = (TextView) view.findViewById(R.id.tv_order_item);
            tv_vendorname=view.findViewById(R.id.store_name);
            cardView = view.findViewById(R.id.card_view);
                 order_details=view.findViewById(R.id.deatils_order);
                 re_order=view.findViewById(R.id.re_order);
help=view.findViewById(R.id.help);
//            //Payment Method
            tv_methid1 = (TextView) view.findViewById(R.id.method1);
            //Date And Time

//            Confirm = (ImageView) view.findViewById(R.id.confirm_image);
//            Out_For_Deliverde = (ImageView) view.findViewById(R.id.delivered_image);
//            Delivered = (ImageView) view.findViewById(R.id.cancal_image);

        }
    }

    public My_Past_Order_adapter(List<My_Past_order_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public My_Past_Order_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_my_past_order_rv, parent, false);
        context = parent.getContext();
        return new My_Past_Order_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final My_Past_order_model mList = modelList.get(position);

        holder.tv_orderno.setText(mList.getSale_id());
        progressDialog=new ProgressDialog(context);



        holder.order_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sale_id = mList.getSale_id();
                String date = mList.getOn_date();
                String time = mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to();
                String total = mList.getTotal_amount();
                String status = mList.getStatus();
                String deli_charge = mList.getDelivery_charge();
                Intent intent=new Intent(context, MyOrderDetail.class);
                intent.putExtra("sale_id", sale_id);
                intent.putExtra("date", date);
                intent.putExtra("previous_amount",mList.getPrevious_amount());
                intent.putExtra("used_amount",mList.getUsed_amount());
                intent.putExtra("coupon_code",mList.getCoupon_code());
                intent.putExtra("time", time);
                intent.putExtra("total", total);
                intent.putExtra("status", status);
                intent.putExtra("deli_charge", deli_charge);
                context.startActivity(intent);


            }
        });
        holder.re_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ReorderActivty.class);
                intent.putExtra("order_id", mList.getSale_id());
                context.startActivity(intent);

            }
        });
        holder.help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Compalin_prouduct.class);
                intent.putExtra("order_id", mList.getSale_id());
                context.startActivity(intent);

            }
        });
if (mList.getStatus().contains("3")){
    holder.bt_status.setText("Cancled");
}
else {
    holder.bt_status.setText("DELIVERED");
}

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
        holder.tv_tracking_date.setText(mList.getOn_date());
        holder.tv_time.setText(mList.getDelivery_time_from() + "-" + mList.getDelivery_time_to());
        holder.tv_price.setText(context.getResources().getString(R.string.currency) + mList.getTotal_amount());
        holder.tv_item.setText(context.getResources().getString(R.string.tv_cart_item) + mList.getTotal_items());

        if (mList.getVandor_name().contains("null")){
            holder.tv_vendorname.setText("Not Available");
        }
        else {
            holder.tv_vendorname.setText(mList.getVandor_name());
        }


    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }


}
