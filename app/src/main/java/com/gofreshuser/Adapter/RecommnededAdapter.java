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
import com.gofreshuser.model.RecommendedModel;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class RecommnededAdapter extends RecyclerView.Adapter<RecommnededAdapter.MyViewHolder> {

    private List<RecommendedModel> modelList;

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

    public RecommnededAdapter(List<RecommendedModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public RecommnededAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recommend, parent, false);

        context = parent.getContext();

        return new RecommnededAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecommnededAdapter.MyViewHolder holder, final int position) {
        RecommendedModel mList = modelList.get(position);


        Glide.with(context)
                .load(Baseurl.IMG_PRODUCT_URL + mList.getProduct_image())
                .placeholder(R.drawable.icon)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image);



        holder.title.setText(mList.getProduct_name());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}

