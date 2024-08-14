package com.example.zingbr.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Music implements Parcelable {
    private long id;
    private String title;
    private Uri uri;

    public Music(long id, String title, Uri uri) {
        this.id = id;
        this.title = title;
        this.uri = uri;
    }
    protected Music(Parcel in){
        id = in.readLong();
        title = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
    }
    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel source) {
            return new Music(source);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeParcelable(uri, flags);
    }
}
