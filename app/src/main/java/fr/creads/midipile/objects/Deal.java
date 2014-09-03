package fr.creads.midipile.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class Deal implements Parcelable {

    private Integer id;
    private Integer quantite;
    private float valeur;

    private String nom;
    private String desription;
    private String descriptionBreve;
    private String societe;
    private String descriptionSociete;
    private String website;
    private String eshop;
    private String facebook;
    private String twitter;
    private String pinterest;
    private String appStore;
    private String playStore;

    private String logo;
    private List<String> images;

    private Integer fbAppId;
    private String fb;

    private String date_creation;
    private String date_lancement;
    private String date_fin;

    public Deal(){

    }

    public Deal(Parcel in) {
        id = in.readInt();
        quantite = in.readInt();
        valeur = in.readFloat();
        fbAppId = in.readInt();

        nom = in.readString();
        desription = in.readString();
        descriptionBreve = in.readString();
        societe = in.readString();
        descriptionSociete = in.readString();
        website = in.readString();
        eshop = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        pinterest = in.readString();
        appStore = in.readString();
        playStore = in.readString();
        logo = in.readString();

        in.readStringList(images);

        date_creation = in.readString();
        date_lancement = in.readString();
        date_fin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(nom);
        parcel.writeString(desription);
        parcel.writeString(descriptionBreve);
        parcel.writeString(societe);
        parcel.writeString(descriptionSociete);
        parcel.writeString(website);
        parcel.writeString(eshop);
        parcel.writeString(facebook);
        parcel.writeString(twitter);
        parcel.writeString(pinterest);
        parcel.writeString(appStore);
        parcel.writeString(appStore);
        parcel.writeString(appStore);
        parcel.writeString(playStore);
        parcel.writeString(logo);

        parcel.writeString(date_creation);
        parcel.writeString(date_lancement);
        parcel.writeString(date_fin);

        parcel.writeInt(id);
        parcel.writeInt(quantite);
        parcel.writeInt(fbAppId);
        parcel.writeFloat(valeur);

        parcel.writeStringList(images);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public Integer getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDesription() {
        return desription;
    }

    public String getDescriptionBreve() {
        return descriptionBreve;
    }

    public String getSociete() {
        return societe;
    }

    public String getDescriptionSociete() {
        return descriptionSociete;
    }

    public String getWebsite() {
        return website;
    }

    public String getEshop() {
        return eshop;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getPinterest() {
        return pinterest;
    }

    public String getAppStore() {
        return appStore;
    }

    public String getPlayStore() {
        return playStore;
    }

    public String getLogo() {
        return logo;
    }

    public List<String> getImages() {
        return images;
    }

    public Integer getFbAppId() {
        return fbAppId;
    }

    public String getDateFin() {
        return date_fin;
    }

    public String getDateLancement() {
        return date_lancement;
    }

    @Override
    public String toString() {
        return "Deal{" + nom + '\'' +
                ", id=" + id +
                '}';
    }
}
