package com.gofreshuser.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gofreshuser.Config.Baseurl;

import com.gofreshuser.model.Show_sub_cat_product;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class GetComplain_adapter extends RecyclerView.Adapter<GetComplain_adapter.MyViewHolder> {

    private List<Show_sub_cat_product> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title,description;
        public ImageView image;
        Button button;

        public MyViewHolder(View view) {

            super(view);
            title = (TextView) view.findViewById(R.id.name);
            image = (ImageView) view.findViewById(R.id.topimage);
            description=view.findViewById(R.id.description);

        }
    }

    public GetComplain_adapter(List<Show_sub_cat_product> modelList) {
        this.modelList = modelList;
    }

    @Override
    public GetComplain_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_getcomplain, parent, false);

        context = parent.getContext();

        return new GetComplain_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GetComplain_adapter.MyViewHolder holder, final int position) {
        Show_sub_cat_product mList = modelList.get(position);


        Glide.with(context)
                .load(Baseurl.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



        holder.title.setText(mList.getProduct_name());
        holder.title.setText(mList.getProduct_description());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
