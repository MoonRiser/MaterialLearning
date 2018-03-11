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
import android.widget.TextView;

import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.activity.NoteDetailActivity;
import com.example.xiewencai.material_learning.db.Note;
import com.example.xiewencai.material_learning.fragment.NoteFragment;

import java.util.List;

/**
 * Created by Xie Wencai on 2018/3/1.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<Note> noteList;
    private int lastPosition=-1;

    public NoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView title;
        TextView content;
        ViewHolder(View itemView) {
            super(itemView);
            content= itemView.findViewById(R.id.note_item_content);
            title=itemView.findViewById(R.id.note_item_title);
            cardView=(CardView) itemView;
        }
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context==null){
            context=parent.getContext();
        }
        View view=LayoutInflater.from(context).inflate(R.layout.note_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);

            holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//设置卡片的点击事件
                int position=holder.getAdapterPosition();
                Intent intent=new Intent(context, NoteDetailActivity.class);
                intent.putExtra("position",position);
                lastPosition=position;
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) context, holder.cardView , "note_detail").toBundle());
               //  context.startActivity(intent);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder holder, final int position) {
        Note note=noteList.get(position);
        holder.title.setText(note.getTitle());
        holder.content.setText(note.getContent());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public  int getLastPosition(){

    return lastPosition;
    }



}
