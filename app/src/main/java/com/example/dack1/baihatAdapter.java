//package com.example.dack1;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.squareup.picasso.Picasso;
//
//import java.util.List;
//
//public class baihatAdapter extends RecyclerView.Adapter<baihatAdapter.ViewHolder> {
//    private List<Music> musicList;
//    private Context mContext;
//
//    public baihatAdapter(List<Music> musicList, Context mContext) {
//        this.musicList = musicList;
//        this.mContext = mContext;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.itemsanpham, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Music music = musicList.get(position);
//        holder.musicName.setText(music.getName());
//        holder.musicAuthor.setText(music.getAuthor());
//        Picasso.get().load(music.getDownloadImg()).into(holder.musicImage);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, nghenhac.class);
//                intent.putExtra("musicName", music.getName());
//                intent.putExtra("musicAuthor", music.getAuthor());
//                intent.putExtra("DownloadImg", music.getDownloadImg());
//                intent.putExtra("downloadUrl", music.getDownloadUrl());
//                mContext.startActivity(intent);
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return musicList != null ? musicList.size() : 0;
//    }
//
//    public void updateData(List<Music> newMusicList) {
//        this.musicList = newMusicList;
//        notifyDataSetChanged();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView musicImage;
//        TextView musicName;
//        TextView musicAuthor;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            musicImage = itemView.findViewById(R.id.imageView);
//            musicName = itemView.findViewById(R.id.textView2);
//            musicAuthor = itemView.findViewById(R.id.textView3);
//        }
//    }
//}

package com.example.dack1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class baihatAdapter extends RecyclerView.Adapter<baihatAdapter.ViewHolder> {
    private List<Music> musicList;
    private Context mContext;

    public baihatAdapter(List<Music> musicList, Context mContext) {
        this.musicList = musicList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemsanpham, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.musicName.setText(music.getName());
        holder.musicAuthor.setText(music.getAuthor());
        Picasso.get().load(music.getDownloadImg()).into(holder.musicImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, nghenhac.class);
                intent.putExtra("musicName", music.getName());
                intent.putExtra("musicAuthor", music.getAuthor());
                intent.putExtra("DownloadImg", music.getDownloadImg());
                intent.putExtra("downloadUrl", music.getDownloadUrl());
                intent.putExtra("trackIndex", position);  // Truyền chỉ số bài hát qua Intent
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
    }

    public void updateData(List<Music> newMusicList) {
        this.musicList = newMusicList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView musicImage;
        TextView musicName;
        TextView musicAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicImage = itemView.findViewById(R.id.imageView);
            musicName = itemView.findViewById(R.id.textView2);
            musicAuthor = itemView.findViewById(R.id.textView3);
        }
    }
}

