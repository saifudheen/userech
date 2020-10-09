package com.gofreshuser.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gofreshuser.model.Charges_model;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class Charges_adapter extends RecyclerView.Adapter<Charges_adapter.MyViewHolder> {

    private List<Charges_model> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView cart_value,cart_charges;

        public MyViewHolder(View view) {

            super(view);
            cart_value = (TextView) view.findViewById(R.id.cart_value);
            cart_charges = (TextView) view.findViewById(R.id.charges);

        }
    }

    public Charges_adapter(List<Charges_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Charges_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_delivery_charges, parent, false);

        context = parent.getContext();

        return new Charges_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Charges_adapter.MyViewHolder holder, final int position) {
        Charges_model mList = modelList.get(position);





        holder.cart_value.setText("Rs. "+mList.getCart_from()+"-"+mList.getCart_to());
        holder.cart_charges.setText("Rs. "+mList.getCharge_amount());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
