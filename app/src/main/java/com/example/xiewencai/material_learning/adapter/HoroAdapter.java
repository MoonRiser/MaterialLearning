package com.example.xiewencai.material_learning.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewencai.material_learning.activity.HorosActivity;
import com.example.xiewencai.material_learning.bean.Album;
import com.example.xiewencai.material_learning.bean.Horoscope;
import com.example.xiewencai.material_learning.R;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Xie Wencai on 2017/8/22.
 */

public class HoroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL = 0;
    private static final int HORIZON = 1;
    private static final int BANNER = 2;
    private Context mContext;
    private List<Horoscope> mHoroList;
    private List<Album> albumList;
    public  BannerViewHolder bannerViewHolder;


    //普通的viewHolder
    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cnName;
        TextView enName;
        ImageView horoImage;
        CircleImageView circleImageView;
        TextView horoDate;

        //RecyclerView horizon;

        //viewHolder的构造方法
        ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            horoImage = view.findViewById(R.id.horo_image);
            cnName = view.findViewById(R.id.chinese_name);
            enName = view.findViewById(R.id.english_name);
            horoDate=view.findViewById(R.id.horo_date);
            circleImageView=view.findViewById(R.id.circleImg);
        }

    }

    //横向的viewHolder
    static class AlbumViewHolder extends RecyclerView.ViewHolder {
        RecyclerView horizon;

        AlbumViewHolder(View view) {
            super(view);
            horizon = (RecyclerView) view;
        }

    }

    //banner的viewHolder
    public static class BannerViewHolder extends RecyclerView.ViewHolder {

        public MZBannerView mzBannerView;

        BannerViewHolder(View view) {
            super(view);
            mzBannerView = (MZBannerView) view;
          //  mzBannerView.start();//开始轮播
        }
    }

    //banner自带的viewholder
    static class BViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }


    //自定义适配器的构造方法
    public HoroAdapter(List<Horoscope> horoList, List albumList) {
        this.albumList = albumList;
        mHoroList = horoList;
    }


    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        if (viewType == HORIZON) {
            View view = new RecyclerView(mContext);
            return new AlbumViewHolder(view);
        }
        if (viewType == BANNER) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.banner, parent, false);
            bannerViewHolder=new BannerViewHolder(view);
            return bannerViewHolder;
        }

       // View view = LayoutInflater.from(mContext).inflate(R.layout.horoscope_item, parent, false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_horoscope_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   int position = holder.getAdapterPosition();//获取holder所在得位置，以获取对应的绑定的数据
                                                   Horoscope horoscope;
                                                   switch (position){
                                                       case 1: horoscope = mHoroList.get(position-1);break;
                                                       default: horoscope = mHoroList.get(position-2);break;
                                                   }
                                                   Intent intent = new Intent(mContext, HorosActivity.class);
                                                   intent.putExtra(HorosActivity.EN_NAME, horoscope.geteNname());
                                                   intent.putExtra(HorosActivity.IMAGE_ID, horoscope.getImageId());
                                                   intent.putExtra(HorosActivity.CN_NAME, horoscope.getcNname());
                                                   //加shareElement转场过渡动画
                                                   mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, holder.horoImage, "horoImg").toBundle());
                                                   // mContext.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation((Activity)mContext).toBundle());
                                                   //    mContext.startActivity(intent);
                                               }
                                           }
        );
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Horoscope horoscope;
            switch (position){
                case 1: horoscope = mHoroList.get(position-1);break;
                default: horoscope = mHoroList.get(position-2);break;
            }

            ViewHolder horoVH = (ViewHolder) holder;
            horoVH.enName.setText(horoscope.geteNname());
            horoVH.cnName.setText(horoscope.getcNname());
            horoVH.horoDate.setText(horoscope.getDateHoros());
            Glide.with(mContext).load(horoscope.getCircleImg()).into(horoVH.circleImageView);
            Glide.with(mContext).load(horoscope.getImageId()).into(horoVH.horoImage);
        } else if (holder instanceof AlbumViewHolder) {
            AlbumViewHolder AlbumVH = (AlbumViewHolder) holder;
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            AlbumVH.horizon.setLayoutManager(manager);
            AlbumAdapter albumAdapter = new AlbumAdapter(albumList);
            AlbumVH.horizon.setAdapter(albumAdapter);
        } else if (holder instanceof BannerViewHolder) {
            MZBannerView mzBannerView = ((BannerViewHolder) holder).mzBannerView;

            List<Integer> list= new ArrayList<>();
            int[] blist=new int[]{R.drawable.b1,R.drawable.b2,R.drawable.b3,R.drawable.b4,R.drawable.b5};
            for (int i:blist) {
                list.add(i);
            }

            mzBannerView.setPages(list, new MZHolderCreator<BViewHolder>() {
                @Override
                public BViewHolder createViewHolder() {
                    return new BViewHolder();
                }
            });
            mzBannerView.setDuration(1000);
            mzBannerView.setDelayedTime(3000);
            mzBannerView.start();
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return BANNER;
            case 2:
                return HORIZON;
            default:
                return NORMAL;
        }
    }

    public int getItemCount() {
        return mHoroList.size()+2;
    }

}






