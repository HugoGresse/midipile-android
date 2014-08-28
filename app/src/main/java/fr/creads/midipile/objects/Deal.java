package fr.creads.midipile.objects;

import java.util.List;

/**
 * Author : Hugo Gresse
 * Date : 27/08/14
 */
public class Deal {

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

    private String dateCreation;
    private String dateLancement;
    private String dateFin;

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
        return dateFin;
    }

    public String getDateLancement() {
        return dateLancement;
    }

    @Override
    public String toString() {
        return "Deal{" + nom + '\'' +
                ", id=" + id +
                '}';
    }
}
