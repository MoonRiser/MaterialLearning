package com.example.xiewencai.material_learning.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewencai.material_learning.activity.HorosActivity;
import com.example.xiewencai.material_learning.Horoscope;
import com.example.xiewencai.material_learning.R;

import java.util.List;


/**
 * Created by Xie Wencai on 2017/8/22.
 */

public class HoroAdapter extends RecyclerView.Adapter<HoroAdapter.ViewHolder> {

    private Context mContext;
    private List<Horoscope>  mHoroList;

static class  ViewHolder extends  RecyclerView.ViewHolder{
    CardView cardView;
    TextView cnName;
    TextView enName;
    ImageView horoImage;
//viewHolder的构造方法
    public ViewHolder(View view){
        super(view);
        cardView=(CardView) view;
        horoImage=(ImageView) view.findViewById(R.id.horo_image);
        cnName=(TextView) view.findViewById(R.id.chinese_name);
        enName=(TextView) view.findViewById(R.id.english_name);
    }

}

//自定义适配器的构造方法
public  HoroAdapter(List<Horoscope> horoList){
    mHoroList=horoList;
}

public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
    if(mContext==null){
        mContext=parent.getContext();
    }
    View view= LayoutInflater.from(mContext).inflate(R.layout.horoscope_item,parent,false);
      final    ViewHolder   holder= new  ViewHolder(view);
    holder.cardView.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               int position= holder.getAdapterPosition();//获取holder所在得位置，以获取对应的绑定的数据
                                               Horoscope horoscope=   mHoroList.get(position);
                                               Intent intent= new Intent(mContext,HorosActivity.class);
                                               intent.putExtra(HorosActivity.EN_NAME,horoscope.geteNname());
                                               intent.putExtra(HorosActivity.IMAGE_ID,horoscope.getImageId());
                                               intent.putExtra(HorosActivity.CN_NAME,horoscope.getcNname());
                                              mContext.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity)mContext, holder.horoImage, "horoImg").toBundle());
                                            // mContext.startActivity(intent,ActivityOptions.makeSceneTransitionAnimation((Activity)mContext).toBundle());
                                          //    mContext.startActivity(intent);
                                           }
                                       }
    );
    return  holder;
}

public  void  onBindViewHolder(ViewHolder holder,int position){
    Horoscope horoscope=mHoroList.get(position);
    holder.enName.setText(horoscope.geteNname());
    holder.cnName.setText(horoscope.getcNname());
    Glide.with(mContext).load(horoscope.getImageId()).into(holder.horoImage);
}

public  int getItemCount(){
    return  mHoroList.size();
}

}
