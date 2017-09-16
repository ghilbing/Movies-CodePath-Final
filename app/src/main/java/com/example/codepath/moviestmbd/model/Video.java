package com.example.codepath.moviestmbd.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gretel on 9/6/17.
 */

public class Video implements Parcelable {

    public static final String YOUTUBE = "YouTube";
    public static final String TRAILER = "Trailer";

    @SerializedName("id")
    String id;

    @SerializedName("iso_639_1")
    String iso;

   @SerializedName("key")
    String key;

    @SerializedName("name")
    String name;

    @SerializedName("site")
    String site;

    @SerializedName("type")
    String type;

    @SerializedName("size")
    int size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString(){
        return "Video{" + "key='" + key + '\'' + ", name='" + name + '\'' + ", site='" + site + '\'' + ", type='" + type + '\'' + '}';
    }



    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.iso);
        parcel.writeString(this.key);
        parcel.writeString(this.name);
        parcel.writeString(this.site);
        parcel.writeInt(this.size);
        parcel.writeString(this.type);
    }

    protected Video(Parcel in){
        this.id = in.readString();
        this.iso = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    public boolean isTrailer(){
        return (site.toLowerCase().equals("youtube") && type.toLowerCase().equals("trailer"));
    }

    public Uri getYoutubeURL() {
        return Uri.parse("http://www.youtube.com/watch?v=" + getKey());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel parcel) {
            return new Video(parcel);
        }

        public Video[] newArray(int size) {
            return new Video[size];
        }

    };

    public static class VideoResult {

        public long id;
        @SerializedName("results")
        private List<Video> results;

        public List<Video> getResults() {
            return results;
        }

    }

}
