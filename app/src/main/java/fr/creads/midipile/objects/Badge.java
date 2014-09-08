package fr.creads.midipile.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class Badge implements Parcelable {

    private String nom;
    private String description;
    private Integer credit;

    public Badge(){}

    public Badge(Parcel in){
        credit = in.readInt();
        nom = in.readString();
        description = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nom);
        parcel.writeString(description);
        parcel.writeInt(credit);
    }

    public static final Parcelable.Creator<Badge> CREATOR = new Creator<Badge>() {
        public Badge createFromParcel(Parcel source) {
            Badge mTheme = new Badge();
            mTheme.nom = source.readString();
            mTheme.description = source.readString();
            mTheme.credit = source.readInt();
            return mTheme;
        }
        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

}
