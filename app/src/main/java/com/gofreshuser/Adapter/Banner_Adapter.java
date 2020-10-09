package com.gofreshuser.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gofreshuser.model.Sildermodel;
import com.gofreshuser.Config.Baseurl;
import com.gofreshuser.model.Sildermodel;
import com.gofreshuser.tecmanic.R;

import java.util.List;

public class Banner_Adapter extends PagerAdapter {

    private List<Sildermodel> sildermodellist;
    Context context;
    public Banner_Adapter(Context context, List<Sildermodel> sildermodellist) {
        this.sildermodellist = sildermodellist;
        this.context=context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.image_list,container,false);
        ImageView banner=view.findViewById(R.id.imageView);
        Glide.with(context)
                .load(Baseurl.IMG_SLIDER_URL + sildermodellist.get(position).getBanner())
                .crossFade()
                .placeholder(R.drawable.icon)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(banner);
//        banner.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, BannerActivity.class);
//
//                intent.putExtra("bannerlink",sildermodellist.get(position).getBannerlink());
//
//                context.startActivity(intent);
//            }
//        });
        container.addView(view,0);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return sildermodellist.size();
    }
}


