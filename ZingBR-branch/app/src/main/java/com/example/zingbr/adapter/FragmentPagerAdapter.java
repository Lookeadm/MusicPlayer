package com.example.zingbr.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.zingbr.fragment.DownloadMusicFragment;
import com.example.zingbr.fragment.MusicListFragment;

public class FragmentPagerAdapter extends FragmentStateAdapter {
    public FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MusicListFragment();
            case 1:
                return new DownloadMusicFragment();
            default:
                return new MusicListFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
