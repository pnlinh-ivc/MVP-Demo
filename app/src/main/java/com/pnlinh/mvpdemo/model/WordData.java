package com.pnlinh.mvpdemo.model;


import android.os.Parcel;
import android.os.Parcelable;

public class WordData implements Parcelable {

    private String id;
    private String title;
    private String meaning;

    public WordData() {
    }

    public WordData(String title, String meanning) {
        this.title = title;
        this.meaning = meanning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getMeaning() {
        return meaning;
    }


    protected WordData(Parcel in) {
        id = in.readString();
        title = in.readString();
        meaning = in.readString();
    }

    public static final Creator<WordData> CREATOR = new Creator<WordData>() {
        @Override
        public WordData createFromParcel(Parcel in) {
            return new WordData(in);
        }

        @Override
        public WordData[] newArray(int size) {
            return new WordData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(meaning);
    }
}
