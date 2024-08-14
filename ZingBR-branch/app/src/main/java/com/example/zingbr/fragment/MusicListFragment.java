package com.example.zingbr.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zingbr.MainActivity;
import com.example.zingbr.ServiceMusic;
import com.example.zingbr.model.Music;
import com.example.zingbr.R;
import com.example.zingbr.SpaceItem;
import com.example.zingbr.adapter.MusicAdapter;

import java.util.ArrayList;

public class MusicListFragment extends Fragment {
    ArrayList<Music> musicList;
    RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.music_list_fragment, container, false);

        recyclerView = view.findViewById(R.id.rvMusicList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        int space = getResources().getDimensionPixelOffset(R.dimen.item_space);
        recyclerView.addItemDecoration(new SpaceItem(space));

        loadMusicList();
        return view;
    }
    private void loadMusicList() {
        // Ensure that the activity implements the interface to get the music list
        if (getActivity() instanceof MainActivity) {
            musicList = ((MainActivity) getActivity()).getMusicList();
            musicAdapter = new MusicAdapter(getContext(),musicList, new MusicAdapter.OnItemClickListener(){
                @Override
                public void onItemClick(Music music) {
                    Intent playIntent = new Intent(getActivity(), ServiceMusic.class);
                    playIntent.setAction(ServiceMusic.ACTION_PLAY);
                    playIntent.putExtra(ServiceMusic.EXTRA_URI, music.getUri());
                    getActivity().startService(playIntent);
                }
            });

            recyclerView.setAdapter(musicAdapter);
        }
    }
}
