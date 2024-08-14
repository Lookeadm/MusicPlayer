package com.example.zingbr.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zingbr.MainActivity;
import com.example.zingbr.R;
import com.example.zingbr.adapter.MusicAdapter;
import com.example.zingbr.model.Music;

import android.graphics.Path;

import java.util.ArrayList;

public class MusicLayoutFragment extends Fragment {
    TextView txtTitleML;
    SeekBar seekBar;
    int songPosn = 0;
    ImageView imgLogo;
    ArrayList<Music> musicList;
    MusicAdapter musicAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_layout_fragment, container, false);
        imgLogo = view.findViewById(R.id.imgLogo);

        Path path = new Path();
        path.addCircle(500, 500, 200, Path.Direction.CW);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imgLogo, "rotation", 0f, 360f);
        animator.setDuration(2000);  // Thời gian xoay (2 giây)
        animator.setRepeatCount(ObjectAnimator.INFINITE);  // Lặp lại vô hạn
        animator.setInterpolator(new LinearInterpolator());  // Đảm bảo xoay đều
        animator.start();

        musicList = ((MainActivity) getActivity()).getMusicList();
        MusicAdapter musicAdapter= new MusicAdapter(getContext(),musicList, (MusicAdapter.OnItemClickListener) getActivity());
        return view;
    }

}
