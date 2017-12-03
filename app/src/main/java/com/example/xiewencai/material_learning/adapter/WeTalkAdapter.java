package com.example.xiewencai.material_learning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewencai.material_learning.Msg;
import com.example.xiewencai.material_learning.R;

import java.util.List;

/**
 * Created by Xie Wencai on 2017/8/30.
 */

public class WeTalkAdapter extends RecyclerView.Adapter<WeTalkAdapter.ViewHolder> {

    private Context mContext;
    private List<Msg> msgList;

    public  WeTalkAdapter(List<Msg> msgList){
        this.msgList =msgList;
    }

    static class  ViewHolder extends RecyclerView.ViewHolder{

    private TextView leftMsg;
    private  TextView rightMsg;
        private ImageView leftImg;
        private ImageView rightImg;



    public ViewHolder(View view){
        super(view);
        leftImg= (ImageView) view.findViewById(R.id.girlImage);
        rightImg=(ImageView) view.findViewById(R.id.boyImage) ;
        leftMsg=(TextView) view.findViewById(R.id.leftMsg);
        rightMsg=(TextView) view.findViewById(R.id.rightMsg);
    }
}

    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        if(mContext==null){
            mContext=parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.we_talk_item,parent,false);
        return  new ViewHolder(view);
    }

    public int getItemCount(){
        return  msgList.size();
    }

    public  void onBindViewHolder(ViewHolder viewHolder,int position){

        Msg msg= msgList.get(position);
        if(msg.getType()==Msg.TYPE_RECEIVED){
            viewHolder.leftMsg.setVisibility(View.VISIBLE);
            viewHolder.leftImg.setVisibility(View.VISIBLE);
            viewHolder.rightImg.setVisibility(View.GONE);
            viewHolder.rightMsg.setVisibility(View.GONE);
            Glide.with(mContext).load(R.drawable.girl).into(viewHolder.leftImg);
            viewHolder.leftMsg.setText(msg.getContent());
        }else{
            viewHolder.leftMsg.setVisibility(View.GONE);
            viewHolder.leftImg.setVisibility(View.GONE);
            viewHolder.rightMsg.setVisibility(View.VISIBLE);
            viewHolder.rightImg.setVisibility(View.VISIBLE);
            viewHolder.rightMsg.setText(msg.getContent());
            Glide.with(mContext).load(R.drawable.boy).into(viewHolder.rightImg);
        }

    }


}
