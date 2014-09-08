package fr.creads.midipile.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 08/09/14
 */
public class User implements Parcelable {

    private String email;
    private String civilite;
    private String nom;
    private String prenom;
    private String rue;
    private String rue_bis;
    private Integer code_postal;
    private String ville;
    private String pays;

    private Integer mobile;
    private Integer newsletter;

    private Double fid;

    private Integer chance;
    private Float credit;

    private List<Badge> badges = new ArrayList<Badge>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(email);
        parcel.writeString(civilite);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeString(rue);
        parcel.writeString(rue_bis);
        parcel.writeString(ville);
        parcel.writeString(pays);

        parcel.writeInt(newsletter);
        parcel.writeInt(code_postal);
        parcel.writeInt(mobile);
        parcel.writeInt(chance);
        parcel.writeDouble(fid);
        parcel.writeFloat(credit);

        parcel.writeTypedList(badges);
    }

    public User(){}

    public User(Parcel in) {
        email = in.readString();
        nom = in.readString();
        prenom = in.readString();
        rue = in.readString();
        rue_bis = in.readString();
        civilite = in.readString();
        ville = in.readString();
        pays = in.readString();

        newsletter = in.readInt();
        code_postal = in.readInt();
        mobile = in.readInt();
        chance = in.readInt();
        fid = in.readDouble();
        credit = in.readFloat();


        in.readTypedList(badges, Badge.CREATOR);
    }
}
