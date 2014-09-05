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
    private String description;
    private String descriptionBreve;
    private String societe;
    private String description_societe;
    private String website;
    private String eshop;
    private String facebook;
    private String twitter;
    private String pinterest;
    private String appStore;
    private String playStore;

    private String logo;
    private List<String> images;

    private String fb_app_id;
    private String fb;

    private String date_creation;
    private String date_lancement;
    private String date_fin;

    private List<PointOfSale> pdv;
    private Integer numberOfPdv;

    public Deal(){

    }

    public Deal(Parcel in) {
        id = in.readInt();
        quantite = in.readInt();
        valeur = in.readFloat();

        nom = in.readString();
        description = in.readString();
        descriptionBreve = in.readString();
        societe = in.readString();
        description_societe = in.readString();
        website = in.readString();
        eshop = in.readString();
        facebook = in.readString();
        twitter = in.readString();
        pinterest = in.readString();
        appStore = in.readString();
        playStore = in.readString();
        logo = in.readString();
        fb_app_id = in.readString();
        fb = in.readString();

        in.readStringList(images);

        date_creation = in.readString();
        date_lancement = in.readString();
        date_fin = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(nom);
        parcel.writeString(description);
        parcel.writeString(descriptionBreve);
        parcel.writeString(societe);
        parcel.writeString(description_societe);
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


        parcel.writeString(fb_app_id);
        parcel.writeString(fb);

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

    public String getDescription() {
        return description;
    }

    public String getDescriptionBreve() {
        return descriptionBreve;
    }

    public String getSociete() {
        return societe;
    }

    public String getDescriptionSociete() {
        return description_societe;
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

    public String getFbAppId() {
        if(null == fb_app_id && null != fb) {
            return fb;
        }
        return fb_app_id;
    }

    public String getDateFin() {
        return date_fin;
    }

    public String getDateLancement() {
        return date_lancement;
    }

    public Integer getQuantite(){
        return quantite;
    }

    public float getValeur(){
        return valeur;
    }

    public List<PointOfSale> getPdv(){
        return pdv;
    }

    public int getNumberOfPdv(){
        return numberOfPdv;
    }

    @Override
    public String toString() {
        return "Deal{" + nom + '\'' +
                ", id=" + id +
                '}';
    }
}
