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

import com.gofreshuser.model.Category_model;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class Cat_Adapter extends RecyclerView.Adapter<Cat_Adapter.MyViewHolder> {

    private List<Category_model> modelList;

    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        Button button;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);

        }
    }

    public Cat_Adapter(List<Category_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Cat_Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_show_cat, parent, false);

        context = parent.getContext();

        return new Cat_Adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Cat_Adapter.MyViewHolder holder, final int position) {
        Category_model mList = modelList.get(position);


        Glide.with(context)
                .load(Baseurl.IMG_CATEGORY_URL + mList.getImage())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



        holder.title.setText(mList.getTitle());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
