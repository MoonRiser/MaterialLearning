package com.example.xiewencai.material_learning.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;


/**
 * Created by Xie Wencai on 2017/8/22.
 */

public class HoroAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL=0;
    private static final int HORIZON=1;
    private Context mContext;
    private List<Horoscope> mHoroList;
    private List<Album> albumList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cnName;
        TextView enName;
        ImageView horoImage;
        RecyclerView horizon;

        //viewHolder的构造方法
        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            horoImage = (ImageView) view.findViewById(R.id.horo_image);
            cnName = (TextView) view.findViewById(R.id.chinese_name);
            enName = (TextView) view.findViewById(R.id.english_name);
        }

    }

    //横向的viewHolder
    static class AlbumViewHolder extends RecyclerView.ViewHolder{
        RecyclerView horizon;
        public AlbumViewHolder(View view){
            super(view);
            horizon=(RecyclerView) view;
        }

    }


    //自定义适配器的构造方法
    public HoroAdapter(List<Horoscope> horoList,List albumList) {
        this.albumList=albumList;
        mHoroList = horoList;
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }

        if(viewType==HORIZON){
            View view = new RecyclerView(mContext);
            return new AlbumViewHolder(view);
        }


        View view = LayoutInflater.from(mContext).inflate(R.layout.horoscope_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   int position = holder.getAdapterPosition();//获取holder所在得位置，以获取对应的绑定的数据
                                                   Horoscope horoscope = mHoroList.get(position);
                                                   Intent intent = new Intent(mContext, HorosActivity.class);
                                                   intent.putExtra(HorosActivity.EN_NAME, horoscope.geteNname());
                                                   intent.putExtra(HorosActivity.IMAGE_ID, horoscope.getImageId());
                                                   intent.putExtra(HorosActivity.CN_NAME, horoscope.getcNname());
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

        if(holder instanceof  ViewHolder){
            Horoscope horoscope = mHoroList.get(position);
            ViewHolder horoVH=(ViewHolder)holder;
            horoVH.enName.setText(horoscope.geteNname());
            horoVH.cnName.setText(horoscope.getcNname());
            Glide.with(mContext).load(horoscope.getImageId()).into(horoVH.horoImage);
        }else if(holder instanceof  AlbumViewHolder){
            AlbumViewHolder AlbumVH=(AlbumViewHolder)holder;
            LinearLayoutManager manager=new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            AlbumVH.horizon.setLayoutManager(manager);
            AlbumAdapter albumAdapter= new AlbumAdapter(albumList);
            AlbumVH.horizon.setAdapter(albumAdapter);
        }



    }



    @Override
    public int getItemViewType(int position) {
        if(position==2){
            return HORIZON;
        }else {
            return NORMAL;
        }
    }

    public int getItemCount() {
        return mHoroList.size();
    }

}






