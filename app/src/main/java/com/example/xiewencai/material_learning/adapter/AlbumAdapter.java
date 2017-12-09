package com.example.xiewencai.material_learning.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.xiewencai.material_learning.R;
import com.example.xiewencai.material_learning.bean.Album;

import java.util.List;

/**
 * Created by Xie Wencai on 2017/12/9.
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private Context context;
    private List<Album> albumList;

    public AlbumAdapter(List<Album> list) {
        albumList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView albumDetail;
        TextView TXsinger;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.album_img);
            albumDetail = view.findViewById(R.id.album_name);
            TXsinger = view.findViewById(R.id.singer);
        }

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (parent != null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = albumList.get(position);
        Glide.with(context).load(album.getAlbumImage()).into(holder.img);
        holder.albumDetail.setText(album.getAlbumName() + " (" + album.getAlbumDate() + ")");
        holder.TXsinger.setText(album.getSinger());
    }
}
