package fr.creads.midipile.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class Badge implements Parcelable {

    private Integer id;
    private String nom;
    private String description;
    private Integer credit;
    private boolean isUserBadge = false;

    public Badge(){}

    public Badge(Parcel in){
        id = in.readInt();
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
        parcel.writeInt(id);
        parcel.writeInt(credit);
    }



    public static final Parcelable.Creator<Badge> CREATOR = new Creator<Badge>() {
        public Badge createFromParcel(Parcel source) {
            Badge mTheme = new Badge();
            mTheme.nom = source.readString();
            mTheme.description = source.readString();
            mTheme.credit = source.readInt();
            mTheme.id = source.readInt();
            return mTheme;
        }
        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public boolean isUserBadge() {
        return isUserBadge;
    }

    public void setUserBadge(boolean isUserBadge) {
        this.isUserBadge = isUserBadge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Badge badge = (Badge) o;

        if (!id.equals(badge.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
