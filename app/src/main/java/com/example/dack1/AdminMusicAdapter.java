package com.example.dack1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminMusicAdapter extends RecyclerView.Adapter<AdminMusicAdapter.ViewHolder> {
    private List<Music> musicList;
    private Context mContext;

    public AdminMusicAdapter(List<Music> list, Context context) {
        this.musicList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.itemsanphamadmin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.musicName.setText(music.getName());
        holder.musicAuthor.setText(music.getAuthor());
        Picasso.get().load(music.getDownloadImg()).into(holder.musicImage);

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xóa dữ liệu từ Realtime Database
                FirebaseDatabase.getInstance().getReference().child("Music").child(music.getId()).removeValue();
            }
        });

        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cập nhật thông tin bài hát
                // Bạn có thể thêm logic để mở một Activity cập nhật thông tin bài hát tại đây
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList != null ? musicList.size() : 0;
    }

    public void updateData(List<Music> newList) {
        musicList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView musicImage;
        TextView musicName;
        TextView musicAuthor;
        Button deleteButton;
        Button updateButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            musicImage = itemView.findViewById(R.id.imageView);
            musicName = itemView.findViewById(R.id.textView2);
            musicAuthor = itemView.findViewById(R.id.textView3);
            deleteButton = itemView.findViewById(R.id.button);
            updateButton = itemView.findViewById(R.id.button2);
        }
    }
}
