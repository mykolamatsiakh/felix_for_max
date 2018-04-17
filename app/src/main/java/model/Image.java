package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marvin on 2/16/2018.
 */

public class Image implements Parcelable {

    public String id;
    public String url;

    public Image(){

    }

    public Image(String id, String url){
        this.id = id;
        this.url = url;
    }

    protected Image(Parcel in) {
        id = in.readString();
        url = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(url);
    }
}