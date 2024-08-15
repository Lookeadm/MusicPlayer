package com.example.zingbr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zingbr.model.Music;

public class MusicLayoutActivity extends AppCompatActivity {
    ImageView btnPlayPause;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.music_layout_fragment);
        btnPlayPause = findViewById(R.id.btnPlayML);

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicLayoutActivity.this, ServiceMusic.class);
                if (isPlaying) {
                    intent.setAction(ServiceMusic.ACTION_PAUSE);
                    btnPlayPause.setImageResource(R.drawable.baseline_pause_24);
                } else {
                    intent.setAction(ServiceMusic.ACTION_RESUME);
                    btnPlayPause.setImageResource(com.cloudinary.android.ui.R.drawable.play);
                }
                MusicLayoutActivity.this.startService(intent);
                isPlaying = !isPlaying;
            }
        });
    }
}
