package com.example.zingbr;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zingbr.adapter.MusicAdapter;
import com.example.zingbr.fragment.MusicLayoutFragment;
import com.example.zingbr.fragment.MusicListFragment;
import com.example.zingbr.model.Music;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MusicAdapter.OnItemClickListener{
    private static final int REQUEST_CODE_READ_MEDIA = 100;
    private MediaPlayer mediaPlayer;
    private ArrayList<Music> musicList;
    private TabLayout tabLayout;
    private ImageView btnPlay;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private LinearLayout musicBar;
    ServiceMusic serviceMusic = new ServiceMusic();
    TextView txtTitle;
    boolean isBound = false;
    boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabLayout);
        btnPlay = findViewById(R.id.btnPlay);
        frameLayout = findViewById(R.id.fMusicLayout);
        musicBar = findViewById(R.id.musicBar);
        txtTitle = findViewById(R.id.txtTitleMB);

        requestPermission();
        setupTabLayout();

        Intent serviceIntent = new Intent(this, ServiceMusic.class);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayPause();
            }
        });
        musicBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicLayoutActivity.class);
                startActivity(intent);
            }
        });
    }


    private void setupTabLayout(){
        tabLayout.addTab(tabLayout.newTab().setText("Music List"));
        tabLayout.addTab(tabLayout.newTab().setText("Another Fragment"));

        replaceFragment(new MusicListFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new MusicListFragment();
                        break;
                    case 1:
                        selectedFragment = new MusicLayoutFragment();
                        break;
                }if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceMusic.LocalBinder binder = (ServiceMusic.LocalBinder) service;
            serviceMusic = binder.getService();
            isBound = true;
            updateTitleTextView();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
    private void updateTitleTextView() {
        if (isBound && serviceMusic != null) {
            String lastPlayedTitle = serviceMusic.getLastPlayedTitle();
            Log.e("title", lastPlayedTitle);
            if(lastPlayedTitle!=null) {
                txtTitle.setText(lastPlayedTitle);
            }else {
                txtTitle.setText("No Music");
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ServiceMusic.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBound){
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fMusicLayout, fragment);
        fragmentTransaction.commit();
    }
    // Cấp quyền truy cập bộ nhớ
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_MEDIA_IMAGES,
                                Manifest.permission.READ_MEDIA_VIDEO,
                                Manifest.permission.READ_MEDIA_AUDIO
                        },
                        REQUEST_CODE_READ_MEDIA);
            } else {

                // Quyền đã được cấp, bạn có thể truy cập các tập tin media ở đây
                loadMusicList();
            }
        } else {
            // Sử dụng quyền cho các phiên bản Android thấp hơn
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ_MEDIA);
            } else {
                // Quyền đã được cấp, bạn có thể truy cập bộ nhớ ngoài ở đây
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_MEDIA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Cấp quyền thành công", Toast.LENGTH_SHORT).show();
                loadMusicList();
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadMusicList() {
        musicList = getMusicList();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("musicList", musicList);
        MusicListFragment musicListFragment = new MusicListFragment();
        musicListFragment.setArguments(bundle);
        replaceFragment(musicListFragment);
    }

    public ArrayList<Music> getMusicList() {
        ArrayList<Music> musicList = new ArrayList<>();
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA};

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        Cursor c = contentResolver.query(uri, projection, selection, null, null);

        if (c != null) {
            while (c.moveToNext()) {
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                String title = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String data = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                if (data.endsWith(".mp3") || data.endsWith(".wav") || data.endsWith(".m4a")) {
                    Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                    musicList.add(new Music(id, title, contentUri));
                }
            }
            c.close();
        }
        return musicList;
    }
    @Override
    public void onItemClick(Music music) {
        Intent playIntent = new Intent(this, ServiceMusic.class);
        playIntent.setAction(ServiceMusic.ACTION_PLAY);
        playIntent.putExtra(ServiceMusic.EXTRA_URI, music.getUri());
        playIntent.putExtra(ServiceMusic.EXTRA_TITLE, music.getTitle());
        startService(playIntent);
    }
    public void PlayPause(){
        Intent playIntent = new Intent(MainActivity.this, ServiceMusic.class);
        if(isPlaying){
            playIntent.setAction(ServiceMusic.ACTION_PAUSE);
            btnPlay.setImageResource(com.cloudinary.android.ui.R.drawable.play);
        }
        else {
            playIntent.setAction(ServiceMusic.ACTION_RESUME);
            btnPlay.setImageResource(R.drawable.baseline_pause_24);
        }
        startService(playIntent);
        isPlaying = !isPlaying;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

}