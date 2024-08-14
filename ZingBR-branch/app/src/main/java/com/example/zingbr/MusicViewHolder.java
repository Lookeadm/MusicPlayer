package com.example.zingbr;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MusicViewHolder extends RecyclerView.ViewHolder {
    public TextView txtTime, txtTitle, txtTitleML;
    public Button btnPre, btnNext;
    public ImageView btnPlay;
    public MusicViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTitle = itemView.findViewById(R.id.txtNameMusic);
        txtTime = itemView.findViewById(R.id.time);
        txtTitleML = itemView.findViewById(R.id.txtTitleML);
        btnNext = itemView.findViewById(R.id.btnNextML);
        btnPre = itemView.findViewById(R.id.btnPreviousML);
        btnPlay = itemView.findViewById(R.id.btnPlayML);
    }
}
