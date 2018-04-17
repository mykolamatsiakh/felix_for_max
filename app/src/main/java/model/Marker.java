package model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;



import net.sharewire.googlemapsclustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvin on 2/16/2018.
 */

public class Marker implements Parcelable, ClusterItem {


    /*Це для того щоб кожна лікарня мала своє id,
    потім за нею будем витягувати госпіталь з серверу*/
    public String id;
    /*Це для того щоб кожна лікарня мала своє ім'я*/
    public String name;
    /*Це для того щоб кожна лікарня мала свій опис*/
    public String description;

    public String date;
    /*Це для того щоб кожна лікарня мала своє фото*/
    public Image image;
    /*Це для того щоб кожна лікарня мала своє місце знаходження*/
    public Location location;


    public Marker() {
    }

    /*Використовуючи змінну "in" можемо отримати значення яке ми писали в 'Parcelable' це
       як правило приватний конструктор, так що тільки поле `CREATOR` може отримати доступ*/
    protected Marker(Parcel in) {
        /*Щоб отримати id*/
        id = in.readString();
        /*Щоб отримати ім'я*/
        name = in.readString();
        /*Щоб отримати опис*/
        description = in.readString();
        date = in.readString();
        image = in.readParcelable(Image.class.getClassLoader());
        location = in.readParcelable(Location.class.getClassLoader());

    }

    /*Це поле необхідно для Android, щоб мати можливість створювати нові об'єкти,
    окремо або в масивах. Це також означає, що ви можете використовувати за замовчуванням*/
    public static final Creator<Marker> CREATOR = new Creator<Marker>() {
        @Override

        public Marker createFromParcel(Parcel in) {return new Marker(in);
        }

        @Override
        public Marker[] newArray(int size) {
            return new Marker[size];
        }
    };

    /*Щоб одержувати інформацію для всіх змінних з класу Hospital*/
    public String getDescription() {
        return description;
    }

    /*Одержання id*/
    public String getId() {
        return id;
    }

    public String getDate(){return date;}

    /*Отримання фото*/
    public Image getImage() {
        return image;
    }

    /*Отримання місця знаходження*/
    public Location getLocation() {
        return location;
    }

    /*Отримання імені*/
    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Marker{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", image=" + image +
                ", location=" + location +

                '}';
    }

    @Override
    /*У переважній більшості випадків ви можете просто повернутися для цього*/
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(date);
        parcel.writeParcelable(image, i);
        parcel.writeParcelable(location, i);

    }


    @Override
    public double getLatitude() {
        return location.lat;
    }

    @Override
    public double getLongitude() {
        return location.lng;
    }

    @Nullable
    @Override
    public String getTitle() {
        return null;
    }

    @Nullable
    @Override
    public String getSnippet() {
        return null;
    }
}
