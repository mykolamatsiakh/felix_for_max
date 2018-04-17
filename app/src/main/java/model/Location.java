package model;

import android.os.Parcel;
import android.os.Parcelable;



/**
 * Created by Marvin on 2/16/2018.
 */

public class Location implements Parcelable {

    //   змінні -  рядок адреси, довгота і широта числом з плаваючою комою
    public String author;
    public double lat;
    public double lng;

    public Location(){

    }
    // введення значень
    protected Location(Parcel in) {
        author = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
    }
    // створюється нова локація
    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
    // методи, які повертають введені значення
    public String getAuthor() {
        return author;
    }

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }
    // локація виводиться в консоль
    @Override
    public String toString() {
        return "Location{" +
                "author='" + author + '\'' +
                ", latitude=" + lat +
                ", longitude=" + lng +
                '}';
    }
    // об'єднює всі методи
    @Override
    public int describeContents() {
        return 0;
    }
    // встановлюємо наш об'єкт в parcel
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(author);
        parcel.writeDouble(lat);
        parcel.writeDouble(lng);
    }


}
