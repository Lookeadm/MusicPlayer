package com.example.zingbr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zingbr.model.Music;
import com.example.zingbr.MusicViewHolder;
import com.example.zingbr.R;
import com.example.zingbr.Utils;

import java.io.IOException;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicViewHolder> {
    Context context;
    private ArrayList<Music> musicList;
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void onItemClick(Music music);
    }
    public MusicAdapter(Context context,ArrayList<Music> musicList, OnItemClickListener listener){
        this.context = context;
        this.musicList = musicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_viewholder_layout, parent, false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        String truncatedTitle = Utils.truncate(music.getTitle(), 40);
        String duration;
        try {
               duration = Utils.getDuration(context, music.getUri());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        holder.txtTitle.setText(truncatedTitle);
        holder.txtTime.setText(duration);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(music);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }
}
